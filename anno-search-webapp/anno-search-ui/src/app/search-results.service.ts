import { Injectable } from '@angular/core';
import { Http, Response, Headers, RequestOptionsArgs } from '@angular/http';
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
  serverUrl: string = "http://localhost:8089/";
  showSuggestions = false;
  private searchText: string;
  suggestion: Entity;
  constructor(private http: Http) {
  }

  public setSearchText(searchText: string) {
    this.searchText = searchText;
    this.fetchSuggestions(searchText);
  }

  public setSuggestion(suggestion: Entity) {
    this.suggestion = suggestion;
    this.fetchResults(suggestion);
  }

  fetchSuggestions(filter: string): void {
    this.http.get(this.serverUrl + "api/search/annotations?string=" + filter)
      .map(this.extractSuggestions).subscribe(x => { this.resultsAuto = x; this.showSuggestions = true; });
  }

  fetchResults(filter: Entity) {
    this.http.get(this.serverUrl + "api/search/docs?string=" + filter.title + "&type=" + filter.type)
      .map(this.extractResults).subscribe(x => { this.results = x; this.showSuggestions = false; });
  }

  extractSuggestions(res: Response) {
    let arr: Array<any> = res.json();
    return arr.slice(0, 10).map((xx): Entity => {
      return { title: Object.keys(xx)[0], type: xx[Object.keys(xx)[0]] };
    });
  }

  extractResults(res: Response) {
    let arr = res.json();
    console.log(arr);
    return arr.map((xx): Result => {
      return { title: xx["title"], matchedText: xx["matchedText"].replace(/(?:\r\n|\r|\n)/g, '<br />'), id: xx["id"], sentimentScore: xx["sentimentScore"] };
    });
  }
}
