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
@RequestMapping("/api/search")
public class AnnoSearchController {

    private final DataStorageService dataIndexingService;

    @Autowired
    public AnnoSearchController(DataStorageService dataIndexingService) {
        this.dataIndexingService = dataIndexingService;
    }

    @RequestMapping(
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> search() {
        dataIndexingService.processData();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
