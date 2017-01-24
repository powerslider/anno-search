/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { SearchResultsService } from './search-results.service';

describe('SearchResultsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SearchResultsService]
    });
  });

  it('should ...', inject([SearchResultsService], (service: SearchResultsService) => {
    expect(service).toBeTruthy();
  }));
});
