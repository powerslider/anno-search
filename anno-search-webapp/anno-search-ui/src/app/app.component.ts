import { Component } from '@angular/core';
import { SearchResultsService } from './search-results.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [SearchResultsService]
})
export class AppComponent {
  title = 'app works!';

  constructor(private resultService: SearchResultsService) {
  }
}
