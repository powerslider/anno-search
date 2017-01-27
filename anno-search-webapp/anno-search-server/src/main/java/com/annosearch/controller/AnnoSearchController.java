package com.annosearch.controller;

import com.annosearch.model.AnnotatedDocument;
import com.annosearch.model.AnnotatedDocumentSearchResult;
import com.annosearch.service.DataStorageService;
import com.annosearch.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Controller class for exposing indexed data.
 *
 * @author Tsvetan Dimitrov <tsvetan.dimitrov23@gmail.com>
 * @since 22-January-2017
 */
@RestController
@RequestMapping("/api")
public class AnnoSearchController {

    private final DataStorageService dataStorageService;

    private final SearchService searchService;

    @Autowired
    public AnnoSearchController(DataStorageService dataStorageService, SearchService searchService) {
        this.dataStorageService = dataStorageService;
        this.searchService = searchService;
    }

    @RequestMapping(
            value = "index",
            method = RequestMethod.GET
    )
    public ResponseEntity<String> indexData() {
        dataStorageService.processData();
        return new ResponseEntity<>("Data successfully indexed", HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(
            value = "search/annotations",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Set<Map.Entry<String, String>>> searchAnnotations(@RequestParam(value = "string") String annQuery) {
        Set<Map.Entry<String, String>> annMap = searchService.searchAnnotations(100, annQuery);
        return new ResponseEntity<>(annMap, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(
            value = "search/docs",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<AnnotatedDocumentSearchResult>> searchDocs(
            @RequestParam(value = "string") String strVal,
            @RequestParam(value = "type") String typeVal) {

        List<AnnotatedDocumentSearchResult> annDocList = searchService.searchDocuments(100, strVal, typeVal);
        return new ResponseEntity<>(annDocList, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(
            value = "search/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<AnnotatedDocument> searchDocs(@PathVariable("id") String id) {
        AnnotatedDocument annDoc = searchService.getAnnotatedDocumentPerId(id);
        return new ResponseEntity<>(annDoc, HttpStatus.OK);
    }
}
