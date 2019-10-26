import { Component, OnInit } from '@angular/core';



@Component({
  selector: 'app-api-info',
  templateUrl: './api-info.component.html',
  styleUrls: ['./api-info.component.css']
})
export class ApiInfoComponent implements OnInit {

  cmd: String;

  constructor() { }

  /**
   * This method is called on initializiation.
   */
  ngOnInit() {
    this.cmd = 'curl -i -H "Content-Type: application/octet-stream" --data-binary "@demo.xjmf.zip" ' + location.origin +'/xjmf --output result.xjmf.zip --insecure';
  }


}
