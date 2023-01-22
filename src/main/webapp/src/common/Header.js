import React from 'react';


class Header extends React.Component {

    /**
     * Custom constructor.
     * @param {*} props 
     */
    constructor(props) {
        super(props);

        this.state = {
            status: null,
            interval: null
        };

        this.updateStatus = this.updateStatus.bind(this);
    }

    /**
     * ReactJS: Method is invoked immediately after a component is mounted.
     */
    componentDidMount() {

        // trigger continuous updates
        this.updateStatus();

        if (!this.interval && this.state.interval === null) {
            this.interval = setInterval(() => {
                this.updateStatus();
            }, 2500);

            this.setState({ interval: this.interval });
        }
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
    updateStatus() {

        fetch("/status")
            .then(response => {
                if (200 === response.status) {
                    return response.json();
                } else {
                    return null;
                }
            })
            .then(status => {

                this.setState({
                    status: status
                });
            });
    }

    /**
     * Shut down the current service.
     */
    shutdownService() {
        fetch("/system/shutdown",
            {method: 'POST'}
        )
            .then(response => {
                alert("System is shut down...");
            })
    }

    /**
     * Return HTML snippet
     */
    render() {

        // final output
        return (
            <div>
                <nav className="navbar bg-light">
                    <div className="container-fluid">
                        <a className="navbar-brand" href="/">Preview Service</a>
                        <button className="navbar-toggler" type="button" data-bs-toggle="offcanvas" data-bs-target="#offcanvasNavbar" aria-controls="offcanvasNavbar">
                            <span className="navbar-toggler-icon"></span>
                        </button>
                        <div className="offcanvas offcanvas-end" tabIndex="-1" id="offcanvasNavbar" aria-labelledby="offcanvasNavbarLabel">
                            <div className="offcanvas-header">
                                <h5 className="offcanvas-title" id="offcanvasNavbarLabel">Preview Service</h5>
                                <button type="button" className="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
                            </div>
                            <div className="offcanvas-body">
                                <ul className="navbar-nav justify-content-end flex-grow-1 pe-3">
                                    <li className="nav-item">
                                        <button type="button" class="btn btn-link nav-link" onClick={this.shutdownService}>Shutdown Service</button>
                                    </li>
                                    <li className="nav-item">
                                        &nbsp;
                                    </li>
                                    <li className="nav-item">
                                        <a className="nav-link" aria-current="page" href="https://github.com/meixxi/preview-service">Project Sources <small><i>(github.com)</i></small></a>
                                    </li>
                                    <li className="nav-item">
                                        <a className="nav-link" aria-current="page" href="https://www.linkedin.com/in/meiXXI">About the Author <small><i>(linkedin.com)</i></small></a>
                                    </li>
                                    <li className="nav-item">
                                        &nbsp;
                                    </li>
                                    <li className="nav-item">
                                        <a className="nav-link" aria-current="page" href="https://www.tudublin.ie">TU Dublin</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </nav>
                <div className='container-fluid'>
                    <div className='row'>
                        <div className='col-6'>
                            <i><small className='text-muted'>
                                <span className='me-2'>Status: {this.state.status ? this.state.status.status : 'n. a.'}</span>
                                ·
                                <span className='mx-2'>Hostname: {this.state.status ? this.state.status.hostname : 'n. a.'}</span>
                                ·
                                <span className='mx-2'>Instance Id: {this.state.status ? this.state.status.instanceId : 'n. a.'}</span>
                            </small></i>
                        </div>
                        <div className='col-6 text-end'>
                            <i><small className='text-muted'>
                                <span className='mx-2'>Duration: {this.state.status ? this.state.status.durationReadable : 'n. a.'}</span>
                                ·
                                <span className='mx-2'>Start Time: {this.state.status ? this.state.status.startTimeReadable : 'n. a.'}</span>
                            </small></i>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default Header;