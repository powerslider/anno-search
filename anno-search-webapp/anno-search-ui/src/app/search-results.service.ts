import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { Observer } from 'rxjs/Observer';
import { Result } from './model/result';
import { Entity } from './model/entity';
import { AnnoDocument } from './model/annodocument';
import { AnnotationInfo } from './model/annotation';
@Injectable()
export class SearchResultsService {

  results: AnnoDocument[];
  resultsAuto: Entity[];
  serverUrl: string;
  showSuggestions = false;
  private searchText: string;
  suggestion: Entity;
  constructor(private http: Http) {
  }

  public setSearchText(searchText: string) {
    this.searchText = searchText;
    this.getMockResults(this.searchText);
  }

  public setSuggestion(suggestion: Entity) {
    this.suggestion = suggestion;
    this.getMockResults2(suggestion);
  }

  private getMockResults(filter: string) {
    let mock: Entity[] = [];
    mock.push({ title: "Barack Obama", data: ["president", "person", "black"] });
    mock.push({ title: "Donald Trump", data: ["president", "person", "white"] });
    mock.push({ title: "Google", data: ["Organization"] });
    mock.push({ title: "blog", data: ["Work"] });
    if (filter != null && filter.length > 0) {
      mock = mock.filter(x => x.title.toLowerCase().indexOf(filter) != -1);
      this.showSuggestions = true;
    } else {
      mock = [];
      this.showSuggestions = false;
    }
    setTimeout(() => {
      this.resultsAuto = mock;
      this.results = [];
    }, 1234);
  }

  private getMockResults2(filter: Entity) {
    let mock: AnnoDocument[] = [];
    mock.push({
      text: "This is a text.\nAnd this is another text.",
      title: "This is the only document",
      annotations: [{ type: "ivan", start: 5, end: 6 },
      { type: "peter", start: 8, end: 8 }]
    });

    setTimeout(() => {
      this.results = mock;
    }, 1234);
  }

  fetchAutoResults(filter: string) {
    this.getMockResults(filter);
  }

  fetchResults(filter: Entity) {
    this.getMockResults2(filter);
  }
}
