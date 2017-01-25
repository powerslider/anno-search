import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { Result } from './model/result';
import { ResultAuto } from './model/result.auto';
@Injectable()
export class SearchResultsService {

  results: Result[];
  resultsAuto: ResultAuto[];
  serverUrl: string;
  constructor(private http: Http) { }

  fetchResults(filter: string): Observable<ResultAuto[]> {
    return Observable.create(function (observer) {
      let mock: ResultAuto[] = [];
      mock.push({ title: "Barack Obama", data: ["president", "person", "black"] });
      mock.push({ title: "Donald Trump", data: ["president", "person", "white"] });

      setTimeout(x => {
        this.results = mock.filter(x => x.title.toLowerCase().indexOf(filter) != -1);

        observer.next(this.results);
        observer.complete();
      }, 1234);
    });
  }

  getAutoResults(prefix: string): Observable<ResultAuto[]> {
    return this.fetchResults(prefix);
    //  return this.http.get(this.serverUrl, { search: "prefix=" + prefix }).map(this.extractData)
    //.catch(this.handleError);
  }

  private extractData(res: Response) {

  }

  private handleError(error: Response | any) {
    // In a real world app, we might use a remote logging infrastructure
    let errMsg: string;
    if (error instanceof Response) {
      const body = error.json() || '';
      const err = body.error || JSON.stringify(body);
      errMsg = `${error.status} - ${error.statusText || ''} ${err}`;
    } else {
      errMsg = error.message ? error.message : error.toString();
    }
    console.error(errMsg);
    return Observable.throw(errMsg);
  }
}
