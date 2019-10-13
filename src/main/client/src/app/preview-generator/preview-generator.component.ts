import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { saveAs } from 'file-saver';


@Component({
  selector: 'app-preview-generator',
  templateUrl: './preview-generator.component.html',
  styleUrls: ['./preview-generator.component.css']
})
export class PreviewGeneratorComponent implements OnInit {

  currentFile: File = null;

  previewUrl: SafeUrl = null;
  preview = null;

  /**
   * Default constructor.
   */
  constructor(
    private http: HttpClient,
    private sanitizer: DomSanitizer
  ) { }

  ngOnInit() {
  }

  /**
   * Process the current file.
   */
  processFile() {
    if (this.currentFile) {

      // create form
      let formData: FormData = new FormData();
      formData.append('file', this.currentFile, this.currentFile.name);

      // post
      this.http.post('/preview', formData, { responseType: 'blob' })
        .subscribe(
          data => {
            this.preview = data;

            let objectURL = URL.createObjectURL(data);       
            this.previewUrl = this.sanitizer.bypassSecurityTrustUrl(objectURL);
          },
          error => console.log(error)
        )
    }
  }

  /**
   * Download the current preview.
   */
  downloadPreview() {
    saveAs(this.preview, this.currentFile.name + "-preview.png")
  }


  /**
   * Reset the users input.
   */
  resetInput() {
    this.currentFile = null;
    this.preview = null;
    this.previewUrl = null;
  }

  /**
   * File selection has been changed
   */
  updateCurrentFile(event) {
    let fileList: FileList = event.target.files;

    if (fileList.length == 1) {
      this.currentFile = fileList[0];
    } else {
      this.currentFile = null;
    }

    console.log(fileList);
  }

}
