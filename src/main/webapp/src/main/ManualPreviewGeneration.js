import React from 'react';

import moment from 'moment/moment';
import { saveAs } from 'file-saver';

class ManualPreviewGeneration extends React.Component {

    /**
     * Custom constructor.
     * @param {*} props 
     */
    constructor(props) {
        super(props);

        this.state = {
            currentFile: null,
            preview: null,
            previewUrl: null
        };

        this.refFileInput = React.createRef();

        this.handleFileChange = this.handleFileChange.bind(this);
        this.handleProcessFile = this.handleProcessFile.bind(this);
        this.handleResetInput = this.handleResetInput.bind(this);
        this.handleDownloadPreview = this.handleDownloadPreview.bind(this);
    }

    /**
     * Handler for file upload.
     * @param {*} e 
     */
    handleFileChange(e) {
        this.setState({
            currentFile: e.target.files[0]
        });
    }

    /**
     * Handler to process the current file.
     */
    handleProcessFile() {

        // create form
        var formData = new FormData();
        formData.append('file', this.state.currentFile, this.state.currentFile.name);

        fetch('/v1/preview', {
            method: 'POST',
            body: formData
        })
            .then((response) => response.arrayBuffer())
            .then((bytes) => {
                const preview = new Blob([bytes], { type: "image/png" });
                const previewUrl = window.URL.createObjectURL(preview);

                this.setState({
                    preview: preview,
                    previewUrl: previewUrl
                });
            });
    }

    /**
     * Handler to download the preview.
     */
    handleDownloadPreview() {
        var file = new File([this.state.preview], "preview-" + (moment().format("YYYYMMDD-HHmmss") + ".png"));
        saveAs(file);
    }

    /**
     * Handler to reset the users input.
     */
    handleResetInput() {
        this.refFileInput.current.value = "";

        this.setState({
            currentFile: null,
            preview: null,
            previewUrl: null
        });
    }

    /**
     * Format bytes to a human readable string.
     * Source: Arainty https://stackoverflow.com/a/20732091
     */
    humanFileSize(size) {
        var i = size === 0 ? 0 : Math.floor(Math.log(size) / Math.log(1024));
        return (size / Math.pow(1024, i)).toFixed(2) * 1 + ' ' + ['B', 'kB', 'MB', 'GB', 'TB'][i];
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
                            <h1>Manual Preview Generation</h1>
                            <p>
                                This section is for the manual generation of preview images using the Preview Service. Please select a PDF Document and click on the button 'Process File'. Afterwards, the preview images is shown on the right for download.
                            </p>
                            <p>
                                In case you need a sample PDF, here you have one: <a href={process.env.PUBLIC_URL + '/assets/tudublin.pdf'}>Flyer TU Dublin (tudublin.pdf)</a>
                            </p>
                        </div>
                    </div>
                    <div className="row">
                        <div className="col-6">
                            <input className="form-control" type="file" id="formFile" accept='application/pdf' onChange={this.handleFileChange} ref={this.refFileInput} />

                            <table className="table table-sm table-borderless my-4">
                                <tbody>
                                    <tr>
                                        <th scope="row" className='w-25'>File Name:</th>
                                        <td>{this.state.currentFile ? this.state.currentFile.name : ''}</td>
                                    </tr>
                                    <tr>
                                        <th scope="row" className='w-25'>Last Modified:</th>
                                        <td>{this.state.currentFile ? moment(this.state.currentFile.lastModified).format('llll') : ''}</td>
                                    </tr>
                                    <tr>
                                        <th scope="row" className='w-25'>File Size:</th>
                                        <td>{this.state.currentFile ? this.humanFileSize(this.state.currentFile.size) : ''}</td>
                                    </tr>
                                    <tr>
                                        <th scope="row" className='w-25'>File Type:</th>
                                        <td>{this.state.currentFile ? this.state.currentFile.type : ''}</td>
                                    </tr>
                                </tbody>
                            </table>

                            <div className="btn-toolbar" role="toolbar">
                                <button type="button" className="btn btn-outline-primary me-3" onClick={this.handleProcessFile}>Process File</button>
                                <button type="button" className="btn btn-outline-secondary" onClick={this.handleResetInput}>Reset Input</button>
                            </div>
                        </div>

                        <div className="col-1">
                            <b>Preview:</b>
                        </div>
                        <div className="col-3">
                            {
                                this.state.previewUrl ? <div><img className='w-100 border' src={this.state.previewUrl} alt="" /></div> : <div className='text-muted'><i>not availabe</i></div>
                            }
                        </div>
                        <div className="col-2">
                            {this.state.preview ?
                                <div>
                                    <table className="table table-sm table-borderless">
                                        <tbody>
                                            <tr>
                                                <th scope="row">Type:</th>
                                                <td>{this.state.preview ? this.state.preview.type : ''}</td>
                                            </tr>
                                            <tr>
                                                <th scope="row">Size:</th>
                                                <td>{this.state.preview ? this.humanFileSize(this.state.preview.size) : ''}</td>
                                            </tr>
                                        </tbody>
                                    </table>

                                    <button type="button" className="btn btn-outline-secondary btn-sm" onClick={this.handleDownloadPreview}>Download </button>
                                </div>
                                : <div></div>
                            }
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default ManualPreviewGeneration;