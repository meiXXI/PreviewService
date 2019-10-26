import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Version } from './version';

@Component({
    selector: 'app-docker',
    templateUrl: './docker.component.html',
    styleUrls: ['./docker.component.css']
})
export class DockerComponent implements OnInit {

    cmd: String;

    /**
     * Custom constructor.
     */
    constructor(
        private http: HttpClient
    ) { }

    /**
     * This method is called on initializiation.
     */
    ngOnInit() {

        // load version
        this.http.get<Version>("/version")
            .subscribe((version: Version) => {
                // create command
                this.cmd = 'docker run -p 8080:8080 ricebean/preview-service:' + version.version;
            });
    }
}
