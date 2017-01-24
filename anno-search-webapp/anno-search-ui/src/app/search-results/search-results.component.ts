import { Component, OnInit, Input } from '@angular/core';
import { SearchResultsService } from '../search-results.service';
import { Result } from '../model/result';

@Component({
  selector: 'app-search-results',
  templateUrl: './search-results.component.html',
  styleUrls: ['./search-results.component.css']
})
export class SearchResultsComponent implements OnInit {

  results: Result[];
  constructor(private resultService: SearchResultsService) { }

  ngOnInit() {
    this.results = this.resultService.getResults();
  }
}
