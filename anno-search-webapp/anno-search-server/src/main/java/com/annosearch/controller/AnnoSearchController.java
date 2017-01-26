package com.annosearch.controller;

import com.annosearch.service.DataStorageService;
import com.annosearch.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
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

    @RequestMapping(
            value = "search",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, String>> searchData(@RequestParam(value = "query") String annQuery) {
        Map<String, String> annMap = searchService.searchAnnotations(100, annQuery);
        return new ResponseEntity<>(annMap, HttpStatus.OK);
    }
}
