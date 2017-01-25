import { Injectable } from '@angular/core';
import { Result } from './model/result';

@Injectable()
export class SearchResultsService {

  results: Result[];
  constructor() { }

  fetchResults() {
    let mock: Result[] = [];
    mock.push({ title: "Result 1", data: "This is the most important result in the world!" });
    mock.push({ title: "Result 1", data: "This \nis thdasdasdasde most important result in the world!" });
    this.results = mock;
  }

  getResults(): Result[] {
    if (this.results == null) {
      this.fetchResults();
    }
    return this.results;
  }
}
