import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

/**
 * Material imports
 */
import { MaterialModule } from '@angular/material';
import { FlexLayoutModule} from '@angular/flex-layout'; 

/**
 * Application imports
 */
import { AppComponent } from './app.component';
import { SearchBarComponent } from './search-bar/search-bar.component';
import { SearchResultsComponent } from './search-results/search-results.component';
import { ColorParagraphPipe } from './color-paragraph.pipe';

import { AnnoDocument } from './model/annodocument';
import { AnnotationInfo } from './model/annotation';
@NgModule({
  declarations: [
    AppComponent,
    SearchBarComponent,
    SearchResultsComponent,
    ColorParagraphPipe
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    MaterialModule.forRoot(),
    FlexLayoutModule.forRoot()
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
