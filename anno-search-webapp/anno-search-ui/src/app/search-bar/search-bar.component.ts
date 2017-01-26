import { Component, OnInit, Input } from '@angular/core';
import { SearchResultsService } from '../search-results.service';
import { ResultAuto } from '../model/result.auto';

@Component({
  selector: 'app-search-bar',
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.css']
})
export class SearchBarComponent implements OnInit {

  searchText: string = '';
  results: ResultAuto[] = [];

  constructor(private resultService: SearchResultsService) { }

  ngOnInit() {
  }

  onKey() {
    this.showSuggestionsList();
  }

  onSearchClicked() {
    this.showSuggestionsList();
  }

  showSuggestionsList() {
    this.resultService.setSearchText(this.searchText);
  }

  onSuggestionClicked(filter: ResultAuto) {
    this.resultService.setSuggestion(filter);
    this.resultService.resultsAuto = [];
  }
}
