import { Component, OnInit } from '@angular/core';
import { SearchResultsService } from '../search-results.service';

@Component({
  selector: 'app-document-preview',
  templateUrl: './document-preview.component.html',
  styleUrls: ['./document-preview.component.css']
})
export class DocumentPreviewComponent implements OnInit {

  constructor(private resultService: SearchResultsService) { }

  ngOnInit() {
  }

}
