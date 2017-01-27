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

  results: Result[];
  resultsAuto: Entity[];
  serverUrl: string = "http://localhost:8089/";
  public showSuggestions = false;
  public showResults = false;
  private searchText: string;
  public document: AnnoDocument = new AnnoDocument();
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

  public fetchSuggestions(filter: string): void {
    this.http.get(this.serverUrl + "api/search/annotations?string=" + filter)
      .map(this.extractSuggestions).subscribe(x => {
        this.resultsAuto = x;
        this.showSuggestions = true;
        this.showResults = false;
      });
  }

  public fetchResults(filter: Entity) {
    this.http.get(this.serverUrl + "api/search/docs?string=" + filter.title + "&type=" + filter.type)
      .map(this.extractResults).subscribe(x => {
        this.results = x;
        this.showSuggestions = false;
        this.showResults = true;
      });
  }

  public fetchDocument(id: any): Observable<AnnoDocument> {
    return this.http.get(this.serverUrl + "api/search/" + id)
      .map(this.extractDocument);
  }

  private extractSuggestions(res: Response): Array<Entity> {
    let arr: Array<any> = res.json();
    return arr.slice(0, 10).map((xx): Entity => {
      return { title: Object.keys(xx)[0], type: xx[Object.keys(xx)[0]] };
    });
  }

  private extractResults(res: Response): Array<Result> {
    let arr: Array<any> = res.json();
    console.log(arr);
    return arr.map((xx): Result => {
      return { title: xx["title"], matchedText: xx["matchedText"], id: xx["id"], sentimentScore: xx["sentimentScore"] };
    });
  }

  private extractDocument(res: Response): AnnoDocument {
    let obj: any = res.json();
    let annotations: Array<any> = obj["annotations"];
    let annos: AnnotationInfo[] = annotations.map((x): AnnotationInfo => {
      return { start: x["startOffSet"], end: x["endOffset"], type: x["type"] };
    })
    return {
      title: obj["title"],
      text: obj["text"],
      sentimentScore: obj["sentimentScore"],
      id: obj["id"],
      annotations: annos
    };
  }
}
