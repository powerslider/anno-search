package com.annosearch.service;

import com.annosearch.model.AnnotatedDocument;
import com.annosearch.model.JsonDataIndexConfiguration;
import com.annosearch.parse.AnnotatedDocumentParser;
import com.annosearch.search.LuceneIndexer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Tsvetan Dimitrov <tsvetan.dimitrov23@gmail.com>
 * @since 25-Jan-2017
 */
@Service
public class DataIndexdingService {

    private final LuceneIndexer luceneIndexer;

    private final AnnotatedDocumentParser annotatedDocParser;

    @Autowired
    public DataIndexdingService(JsonDataIndexConfiguration dataConfig) {
        this.luceneIndexer = new LuceneIndexer(dataConfig.getJsonIndexPath());
        this.annotatedDocParser = new AnnotatedDocumentParser(dataConfig.getJsonDataSourcePath());
    }

    public void indexData() {
        List<AnnotatedDocument> annotatedDocuments = annotatedDocParser.parse().getAnnotatedDocuments();
        luceneIndexer.createIndex(annotatedDocuments);
    }
}
