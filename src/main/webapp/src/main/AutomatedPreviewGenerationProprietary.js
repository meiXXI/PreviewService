import React from 'react';

class AutomatedPreviewGenerationProprietary extends React.Component {

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
     * Return HTML snippet
     */
    render() {

        // final output
        return (
            <div>
                <div className="container">
                    <div className="row">
                        <div className="col-12">
                            <h1>Automated Preview Generation (Proprietary Interface)</h1>
                            <p>
                                Of course, the preview service also provides a propietary interface.
                            </p>
                            <p>
                                Here is a sample PDF: <a href={process.env.PUBLIC_URL + '/assets/tudublin.pdf'}>Flyer TU Dublin (tudublin.pdf)</a>
                            </p>
                            <p>
                                This is the appropriate CLI Command to send an XJMF Package to the preview generation:
                            </p>
                            <div className='font-monospace border border-dark p-2 bg-secondary bg-opacity-25 rounded'>
                                curl -i --data-binary "@tudublin.png" http://localhost:8080/v1/preview --output tudublin.png --insecure
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default AutomatedPreviewGenerationProprietary;