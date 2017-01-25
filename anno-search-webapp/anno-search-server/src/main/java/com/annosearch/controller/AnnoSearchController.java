package com.annosearch.controller;

import com.annosearch.service.DataStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Tsvetan Dimitrov <tsvetan.dimitrov23@gmail.com>
 * @since 22-January-2017
 */
@RestController
@RequestMapping("/api")
public class AnnoSearchController {

    private final DataStorageService dataStorageService;

    @Autowired
    public AnnoSearchController(DataStorageService dataStorageService) {
        this.dataStorageService = dataStorageService;
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
    public ResponseEntity<String> searchData() {
        dataStorageService.search();
        return new ResponseEntity<>("Data successfully indexed", HttpStatus.OK);
    }
}
