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
            version: null
        };

        this.updateVersion = this.updateVersion.bind(this);
    }

    /**
     * ReactJS: Method is invoked immediately after a component is mounted.
     */
    componentDidMount() {

        // trigger continuous updates
        this.updateVersion();

        this.interval = setInterval(() => {
            this.updateVersion();
        }, 5000);
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


        // define content
        var versionDetails = null;

        if (this.state.version) {
            versionDetails = (
                <div>
                    <div className="row row-cols-2">
                        <div className="col">
                            <small>v{this.state.version.version}<span className='d-none d-md-inline'>-{this.state.version.commitId}</span> · ({this.state.version.buildTime})</small>
                        </div>
                        <div className="col text-end">
                            <small>Web-Client: v{packageJson.version}</small>
                        </div>
                    </div>
                </div>
            );
        } 
        

        // final output
        return (
            <div className="footer fixed-bottom bg-dark p-2 text-light">
                <div className="container-fluid">
                    <div className="row row-cols-2">
                        <div className="col">
                            <small>Preview Service</small>
                        </div>
                        <div className="col text-end">
                            <small>Apache License 2.0</small>
                        </div>
                    </div>
                    { versionDetails ? versionDetails : '' }
                </div>
            </div>
        );
    }
}

export default Footer;