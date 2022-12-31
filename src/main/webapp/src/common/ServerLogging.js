import React from 'react';

import moment from 'moment/moment';

class ServerLogging extends React.Component {

    /**
     * Custom constructor.
     * @param {*} props 
     */
    constructor(props) {
        super(props);

        this.state = {
            showLogs: true,
            logEvents: [],
            interval: null
        };

        this.refLogs = React.createRef();

        this.handleCollapse = this.handleCollapse.bind(this);
        this.updateServerLogs = this.updateServerLogs.bind(this);
    }

    /**
     * ReactJS: Method is invoked immediately after a component is mounted.
     */
    componentDidMount() {

        // trigger continuous updates
        this.updateServerLogs();

        if (!this.interval && this.state.interval === null) {
            this.interval = setInterval(() => {
                this.updateServerLogs();
            }, 2150);

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
     * Handling the collapse functionality.
     */
    handleCollapse() {
        this.setState({
            showLogs: !this.state.showLogs
        })
    }

    /**
     * Update the server logs.
     */
    updateServerLogs() {
        fetch("/v1/logging")
            .then(response => {
                if (200 === response.status) {
                    return response.json();
                } else {
                    return null;
                }
            })
            .then(logEvents => {

                // update logs
                this.setState({
                    logEvents: logEvents
                }, () => {
                    // scrol to bottom
                    this.refLogs.current.scrollTop = this.refLogs.current.scrollHeight
                });

                
                
            });
    }

    /**
     * Return HTML snippet
     */
    render() {

        // prepare log events
        const logEvents = this.state.logEvents ? this.state.logEvents.reverse() : []


        // final output
        return (
            <div>
                <div className="container-fluid mt-2">
                    <div className="row">
                        <div className="col-6">
                            <b className='ms-2'>Server Logs:</b>
                        </div>
                        <div className="col-6 text-end">
                            <button className="btn btn-link text-muted btn-sm text-decoration-none me-2" type="button" onClick={this.handleCollapse}>
                                {this.state.showLogs === true ? 'hide server logs >>' : '<< show server logs'}
                            </button>
                        </div>
                    </div>
                </div>
                <div className={'collapse border border-dark bg-secondary bg-opacity-75 ' + (this.state.showLogs === true ? 'show' : '')} id="serverLogs">
                    <div className='overflow-auto' ref={this.refLogs} style={{ height: '275px' }}>
                        <div className="container">
                            <div className="row">
                                <div className="col-12">
                                    <table className="table table-sm table-hover table-responsive table-borderless">
                                        <tbody>
                                            {logEvents.map((logEvent, idx) => {

                                                    // define text color
                                                    var textColor;

                                                    switch (logEvent.level) {
                                                        case "INFO":
                                                            textColor = "text-white";
                                                            break;
                                                        case "WARN":
                                                            textColor = "text-warning";
                                                            break;
                                                        default:
                                                            textColor = "text-danger";
                                                    }

                                                    // render line
                                                    return (<tr key={idx}>
                                                        <td className={textColor + ' font-monospace text-nowrap'} title={"Timestamp: " + logEvent.timeStamp}>{moment(logEvent.timeStamp).format("YYYY-MM-DD HH:mm:ss.SSS")}</td>
                                                        <td className={textColor + ' font-monospace text-nowrap'} title={"Log Level: " + logEvent.level}>{logEvent.level}</td>
                                                        <td className={textColor + ' font-monospace text-nowrap'} title={"ThreadName: " + logEvent.threadName}>{logEvent.threadName}</td>
                                                        <td className={textColor + ' font-monospace text-nowrap'} title={"LoggerName: " + logEvent.loggername}>{logEvent.loggerName.substring(logEvent.loggerName.lastIndexOf(".") + 1)}</td>
                                                        <td className={textColor + ' font-monospace text-truncate'} style={{ maxWidth: '500px' }} title={"Message: " + logEvent.formattedMessage}>{logEvent.formattedMessage}</td>
                                                    </tr>)
                                                })
                                            }
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default ServerLogging;