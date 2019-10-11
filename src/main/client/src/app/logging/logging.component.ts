import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LogEvent } from './log-event';

@Component({
  selector: 'app-logging',
  templateUrl: './logging.component.html',
  styleUrls: ['./logging.component.css']
})
export class LoggingComponent implements OnInit {

  logEvents: LogEvent[] = null;

  /**
   * Custom constructor.
   */
  constructor(
    private http: HttpClient
  ) { }

  /**
   * Method is called by the framework after initialization.
   */
  ngOnInit() {
    this.loadLogEvents();
  }

  /**
   * Load log events.
   */
  loadLogEvents() {
    this.http.get<LogEvent[]>("/logging")
      .subscribe((data: LogEvent[]) => this.logEvents = data);
  }
}
