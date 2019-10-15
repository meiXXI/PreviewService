import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { VersionComponent } from './version/version.component';
import { PreviewGeneratorComponent } from './preview-generator/preview-generator.component';

import { FileSizePipe } from './filesize.pipe';
import { LoggingComponent } from './logging/logging.component';
import { ApiInfoComponent } from './api-info/api-info.component'


@NgModule({
  declarations: [
    AppComponent,
    VersionComponent,
    PreviewGeneratorComponent,
    FileSizePipe,
    LoggingComponent,
    ApiInfoComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
