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
                                url --data-binary "@tudublin.pdf"  -H "Content-Type: application/pdf" http://localhost:8080/v1/preview --output tudublin-new.png
                            </div>
                            <p className='mt-3'>
                                The following call allows to customize the resultion (here: 300 dpi). Feel free to change '300' in the url to any other value.
                            </p>
                            <div className='font-monospace border border-dark p-2 bg-secondary bg-opacity-25 rounded'>
                                curl --data-binary "@tudublin.pdf"  -H "Content-Type: application/pdf" http://localhost:8080/v1/preview/resolution/300 --output tudublin-new.png
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default AutomatedPreviewGenerationProprietary;