import React from 'react';

import { saveAs } from 'file-saver';
import moment from 'moment/moment';

class AutomatedPreviewGenerationXJdf extends React.Component {

    /**
     * Custom constructor.
     * @param {*} props 
     */
    constructor(props) {
        super(props);

        this.state = {
        };
    }

    /**
     * Handler to generate and download a demo pacakge
     */
    handleGenerateAndDownloadDemo() {
        fetch("/v1/xjdf/example")
            .then(response => {
                if (200 === response.status) {
                    return response.arrayBuffer();
                } else {
                    return null;
                }
            })
            .then(bytes => {
                const blob = new Blob([bytes], { type: "image/png" });
                var file = new File([blob], "preview-demo.xjmf.zip");
                saveAs(file);
            });
    }

    /**
     * Return HTML snippet
     */
    render() {

        // final output
        return (
            <div>
                <div className="container">
                    <div className="row">
                        <div className="col-12">
                            <h1>Automated Preview Generation (XJDF)</h1>
                            <p>
                                This section provides information and materials for the automated generation of preview images using the
                                XJDF Interface. The service has implemented an XJDF-based Application Programming Interface (API) to achieve 
                                this.The simplest way to try out the API is to download the demo package and use the command line tool "curl" 
                                to send it to the service.
                            </p>
                            <p>
                                In case you are interested in how the XJDF works in detail, feel free to download the demo package, unzip it and analyze the files included in the package.
                            </p>
                            <p>
                                Here is a demo XJDF Package: <a href="/#" onClick={this.handleGenerateAndDownloadDemo}>Generate & Download</a>
                            </p>
                            <p>
                                This is the appropriate CLI Command to send an XJMF Package to the preview generation:
                            </p>
                            <div className='font-monospace border border-dark p-2 bg-secondary bg-opacity-25 rounded'>
                                curl --data-binary @preview-demo.xjmf.zip -H "Content-Type: application/octet-stream" http://localhost:8080/v1/xjdf --output preview.xjmf.zip
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default AutomatedPreviewGenerationXJdf;