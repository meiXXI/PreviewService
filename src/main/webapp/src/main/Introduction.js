import React from 'react';

class Introduction extends React.Component {

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
                            <h1>Introduction</h1>
                            <p>
                                This Preview Service generates a preview image of the first page of a PDF Document. The service can either be consumed manually, using this graphical user interface, or automatically, using the API as described below.
                            </p>
                            <p>
                                The service has been implemented as an Open Source Project. All sources are available on github: <a href="https://github.com/meiXXI/preview-service">https://github.com/meiXXI/PreviewService</a>.
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default Introduction;