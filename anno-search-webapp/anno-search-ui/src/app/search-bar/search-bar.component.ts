import { Component, OnInit, Input } from '@angular/core';
import { SearchResultsService } from '../search-results.service';
import { Result } from '../model/result';

@Component({
  selector: 'app-search-bar',
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.css']
})
export class SearchBarComponent implements OnInit {

  searchText: string = '';
  results: Result[];
  constructor(private resultService: SearchResultsService) { }

  ngOnInit() {

  }

  onSearchClicked() {
    console.log("Yeah! You clicked!");
    this.resultService.getResults();
  }
}
