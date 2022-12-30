import React from 'react';

import packageJson from '../../package.json';

class Footer extends React.Component {

    /**
     * Custom constructor.
     * @param {*} props 
     */
    constructor(props) {
        super(props);

        this.state = {
            version: null,
            interval: null
        };

        this.updateVersion = this.updateVersion.bind(this);
    }

    /**
     * ReactJS: Method is invoked immediately after a component is mounted.
     */
    componentDidMount() {
        // prevent second call
        if (this.isCalled) {
            return;
        }
        this.isCalled = true;

        // trigger continuous updates
        this.updateVersion();

        var interval = setInterval(() => {
            this.updateVersion();
        }, 15000);

        this.setState({ interval: interval });
    }

    /**
     * ReactJS: Method is called when a component is being removed from the DOM.
     */
    componentWillUnmount() {
        clearInterval(this.state.interval);
    }

    /**
     * Update version
     */
    updateVersion() {

        fetch("/version")
            .then(response => {
                if (200 === response.status) {
                    return response.json();
                } else {
                    return null;
                }
            })
            .then(version => {

                this.setState({
                    version: version
                });
            });
    }


    /**
     * Return HTML snippet
     */
    render() {

        return (
            <div className="bg-white">

                <div>
                    <div className='bg-dark p-2 text-light'>
                        <div className="container-fluid">
                            <div className="row row-cols-2">
                                <div className="col">
                                    <small>Author: Stefan Meissner <i>(<a className='text-light' href="https://www.linkedin.com/in/meiXXI">meiXXI</a>)</i></small>
                                </div>
                                <div className="col text-end">
                                    <small>Apache License 2.0</small>
                                </div>
                            </div>
                            <div>
                                <div className="row row-cols-2">
                                    <div className="col">
                                        PhD Candidate at TU Dublin
                                    </div>
                                    <div className="col text-end">
                                        <small>{this.state.version ? <small>v{this.state.version.version}<span className='d-none d-md-inline'>-{this.state.version.commitId}</span> · ({this.state.version.buildTime})</small> : 'n. a.'} · webapp: v{packageJson.version}</small>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default Footer;