import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { Observer } from 'rxjs/Observer';
import { Result } from './model/result';
import { ResultAuto } from './model/result.auto';
@Injectable()
export class SearchResultsService {

  results: Result[];
  resultsAuto: ResultAuto[];
  serverUrl: string;
  showSuggestions = false;
  searchText: string;
  suggestion: ResultAuto;
  constructor(private http: Http) {
  }

  setSearchText(searchText: string) {
    this.searchText = searchText;
    this.getMockResults(this.searchText);
  }

  setSuggestion(suggestion: ResultAuto) {
    this.suggestion = suggestion;
    this.getMockResults2(suggestion);
  }

  getMockResults(filter: string) {
    let mock: ResultAuto[] = [];
    mock.push({ title: "Barack Obama", data: ["president", "person", "black"] });
    mock.push({ title: "Donald Trump", data: ["president", "person", "white"] });
    mock.push({ title: "Google", data: ["Organization"] });
    mock.push({ title: "blog", data: ["Work"] });
    if (filter != null && filter.length > 0) {
      mock = mock.filter(x => x.title.toLowerCase().indexOf(filter) != -1);
      this.showSuggestions = true;
    } else {
      mock = [];
      this.showSuggestions = false;
    }
    setTimeout(() => {
      this.resultsAuto = mock;
    }, 1234);
  }

  getMockResults2(filter: ResultAuto) {
    let mock: Result[] = [];
    mock.push({ title: "001", data: { "text": "www.mattcutts.com\n\nYou probably want my blog .\n\nHi, my name is Matt Cutts and I joined Google as a software engineer in January 2000. I'm currently the head of Google's Webspam team . I sometimes blog about things, but please bear in mind my disclaimer .\n\nBefore Google, I worked on my Ph.D. in computer graphics at the University of North Carolina at Chapel Hill. I have an M.S. from UNC-Chapel Hill, and B.S. degrees in both mathematics and computer science from the University of Kentucky.\n\nI wrote the first version of SafeSearch, which is Google's family filter, and I've worked on search quality and webspam at Google for the last several years.\n\nThis is what I look like:\n\n(Thanks to Andy Beal for the image.)\n\nYou can also find me on Twitter or on Google+ .\n", "entities": { "Organization": [{ "indices": [87, 93], "type": "Organization", "inst": "http://dbpedia.org/resource/Google", "class": "http://dbpedia.org/ontology/Organization", "string": "Google", "subclasses": ["http://dbpedia.org/ontology/Thing", "http://dbpedia.org/ontology/Organization"], "preferredLabel": ["Google"] }, { "indices": [160, 166], "type": "Organization", "inst": "http://dbpedia.org/resource/Google", "class": "http://dbpedia.org/ontology/Organization", "string": "Google", "subclasses": ["http://dbpedia.org/ontology/Thing", "http://dbpedia.org/ontology/Organization"], "preferredLabel": ["Google"] }, { "indices": [263, 269], "type": "Organization", "inst": "http://dbpedia.org/resource/Google", "class": "http://dbpedia.org/ontology/Organization", "string": "Google", "subclasses": ["http://dbpedia.org/ontology/Thing", "http://dbpedia.org/ontology/Organization"], "preferredLabel": ["Google"] }, { "indices": [316, 363], "type": "Organization", "inst": "http://dbpedia.org/resource/University_of_North_Carolina_at_Chapel_Hill", "class": "http://dbpedia.org/ontology/Organization", "string": "the University of North Carolina at Chapel Hill", "preferredLabel": ["University of North Carolina at Chapel Hill"], "subclasses": ["http://dbpedia.org/ontology/Thing", "http://dbpedia.org/ontology/Organization"] }, { "indices": [385, 400], "type": "Organization", "inst": "http://dbpedia.org/resource/University_of_North_Carolina_at_Chapel_Hill", "class": "http://dbpedia.org/ontology/Organization", "string": "UNC-Chapel Hill", "preferredLabel": ["University of North Carolina at Chapel Hill"], "subclasses": ["http://dbpedia.org/ontology/Thing", "http://dbpedia.org/ontology/Organization"] }, { "indices": [465, 491], "type": "Organization", "inst": "http://dbpedia.org/resource/University_of_Kentucky", "class": "http://dbpedia.org/ontology/Organization", "string": "the University of Kentucky", "preferredLabel": ["University of Kentucky"], "subclasses": ["http://dbpedia.org/ontology/Thing", "http://dbpedia.org/ontology/Organization"] }, { "indices": [523, 533], "type": "Organization", "ambiguityRank": 0, "ambiguityRankWithinClass": 0, "class": "http://dbpedia.org/ontology/Organization", "string": "SafeSearch", "inst": "http://data.ontotext.com/publishing/organization/SafeSearch" }, { "indices": [544, 550], "type": "Organization", "inst": "http://dbpedia.org/resource/Google", "class": "http://dbpedia.org/ontology/Organization", "string": "Google", "subclasses": ["http://dbpedia.org/ontology/Thing", "http://dbpedia.org/ontology/Organization"], "preferredLabel": ["Google"] }, { "indices": [617, 623], "type": "Organization", "inst": "http://dbpedia.org/resource/Google", "class": "http://dbpedia.org/ontology/Organization", "string": "Google", "subclasses": ["http://dbpedia.org/ontology/Thing", "http://dbpedia.org/ontology/Organization"], "preferredLabel": ["Google"] }, { "indices": [742, 749], "type": "Organization", "inst": "http://dbpedia.org/resource/Twitter", "class": "http://dbpedia.org/ontology/Organization", "string": "Twitter", "subclasses": ["http://dbpedia.org/ontology/Thing", "http://dbpedia.org/ontology/Organization"], "preferredLabel": ["Twitter"] }, { "indices": [756, 762], "type": "Organization", "inst": "http://dbpedia.org/resource/Google", "class": "http://dbpedia.org/ontology/Organization", "string": "Google", "subclasses": ["http://dbpedia.org/ontology/Thing", "http://dbpedia.org/ontology/Organization"], "modifiedBy": ["PropagateTrustedAnnotations"], "preferredLabel": ["Google"] }], "Work": [{ "indices": [40, 44], "type": "Work", "inst": "http://dbpedia.org/resource/Blog", "class": "http://dbpedia.org/ontology/Thing", "string": "blog", "preferredLabel": ["blog"], "subclasses": ["http://dbpedia.org/ontology/Thing", "http://dbpedia.org/ontology/Work"] }, { "indices": [196, 200], "type": "Work", "inst": "http://dbpedia.org/resource/Blog", "class": "http://dbpedia.org/ontology/Thing", "string": "blog", "preferredLabel": ["blog"], "subclasses": ["http://dbpedia.org/ontology/Thing", "http://dbpedia.org/ontology/Work"] }], "Keyphrase": [{ "indices": [4, 13], "type": "Keyphrase", "inst": "http://data.ontotext.com/publishing/topic/Mattcutt", "string": "Mattcutt", "class": "http://ontology.ontotext.com/taxonomy/Keyphrase", "label": "Pos" }, { "indices": [32, 36], "type": "Keyphrase", "inst": "http://dbpedia.org/resource/Want", "class": "http://dbpedia.org/ontology/Thing", "string": "want", "subclasses": ["http://dbpedia.org/ontology/Thing"], "preferredLabel": ["want"] }, { "indices": [55, 59], "type": "Keyphrase", "inst": "http://data.ontotext.com/publishing/topic/Name", "string": "Name", "class": "http://ontology.ontotext.com/taxonomy/Keyphrase", "label": "Pos" }, { "indices": [99, 116], "type": "Keyphrase", "inst": "http://dbpedia.org/resource/Software_engineer", "class": "http://dbpedia.org/ontology/Thing", "string": "software engineer", "subclasses": ["http://dbpedia.org/ontology/Thing"], "preferredLabel": ["software engineer"] }, { "indices": [120, 132], "type": "Keyphrase", "inst": "http://data.ontotext.com/publishing/topic/January_2000", "string": "January 2000", "class": "http://ontology.ontotext.com/taxonomy/Keyphrase", "label": "Pos" }, { "indices": [152, 156], "type": "Keyphrase", "inst": "http://data.ontotext.com/publishing/topic/Head", "string": "Head", "class": "http://ontology.ontotext.com/taxonomy/Keyphrase", "label": "Pos" }, { "indices": [160, 181], "type": "Keyphrase", "inst": "http://data.ontotext.com/publishing/topic/Google%27s_webspam_team", "string": "Google's webspam team", "class": "http://ontology.ontotext.com/taxonomy/Keyphrase", "label": "Pos" }, { "indices": [226, 238], "type": "Keyphrase", "inst": "http://dbpedia.org/resource/Bear_in_Mind", "class": "http://dbpedia.org/ontology/Thing", "string": "bear in mind", "subclasses": ["http://dbpedia.org/ontology/Thing", "http://dbpedia.org/ontology/Work"], "preferredLabel": ["Bear in Mind"] }, { "indices": [242, 252], "type": "Keyphrase", "inst": "http://data.ontotext.com/publishing/topic/Disclaimer", "string": "Disclaimer", "class": "http://ontology.ontotext.com/taxonomy/Keyphrase", "label": "Pos" }, { "indices": [286, 291], "type": "Keyphrase", "inst": "http://dbpedia.org/resource/Doctor_of_Philosophy", "class": "http://dbpedia.org/ontology/Thing", "string": "Ph.D.", "subclasses": ["http://dbpedia.org/ontology/Thing"], "preferredLabel": ["Doctor of Philosophy"] }, { "indices": [295, 312], "type": "Keyphrase", "inst": "http://dbpedia.org/resource/Computer_graphics", "class": "http://dbpedia.org/ontology/Thing", "string": "computer graphics", "subclasses": ["http://dbpedia.org/ontology/Thing"], "preferredLabel": ["computer graphics"] }, { "indices": [375, 379], "type": "Keyphrase", "inst": "http://dbpedia.org/resource/Master_of_Science", "class": "http://dbpedia.org/ontology/Thing", "string": "M.S.", "preferredLabel": ["Master of Science"], "subclasses": ["http://dbpedia.org/ontology/Thing"] }], "Person": [{ "indices": [63, 73], "type": "Person", "inst": "http://dbpedia.org/resource/Matt_Cutts", "class": "http://dbpedia.org/ontology/Person", "string": "Matt Cutts", "preferredLabel": ["Matt Cutts"], "subclasses": ["http://dbpedia.org/ontology/Thing", "http://dbpedia.org/ontology/Person"] }, { "indices": [691, 700], "type": "Person", "ambiguityRank": 0, "ambiguityRankWithinClass": 0, "class": "http://dbpedia.org/ontology/Person", "string": "Andy Beal", "inst": "http://data.ontotext.com/publishing/person/Andy_Beal" }] } } });
    if (filter != null) {
      mock = mock.filter(x => {
        for (let entity of filter.data) {
          if (!x.data.entities.hasOwnProperty(entity)) {
            return false;
          }
        }
        return true;
      });
    } else {
      mock = [];
    }
    setTimeout(() => {
      this.results = mock;
    }, 1234);
  }

  fetchAutoResults(filter: string) {
    this.getMockResults(filter);
  }

  fetchResults(filter: ResultAuto) {
    this.getMockResults2(filter);
  }
}
