import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';


@Component({
  selector: 'app-preview-generator',
  templateUrl: './preview-generator.component.html',
  styleUrls: ['./preview-generator.component.css']
})
export class PreviewGeneratorComponent implements OnInit {

  currentFile: File = null;

  /**
   * Default constructor.
   */
  constructor(
    private http: HttpClient
  ) { }

  ngOnInit() {
  }

  /**
   * Process the current file.
   */
  processFile(event) {
    if (this.currentFile) {
      let formData: FormData = new FormData();
      formData.append('file', this.currentFile, this.currentFile.name);


      let httpOptions = {
        headers: new HttpHeaders({
          'Content-Type': 'multipart/form-data',
          'Accept': 'application/json'
        })
      };

      this.http.post('/preview', formData)
        .subscribe(
          data => console.log(data),
          error => console.log(error)
        )
    }
  }

  /**
   * Reset the users input.
   */
  resetInput(event) {
    this.currentFile = null;
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
