package com.annosearch.service;

import com.annosearch.model.AnnotatedDocument;
import com.annosearch.config.IndexAndStorageConfiguration;
import com.annosearch.parse.AnnotatedDocumentParser;
import com.annosearch.storage.LuceneIndexer;
import com.annosearch.storage.MapDBStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Tsvetan Dimitrov <tsvetan.dimitrov23@gmail.com>
 * @since 25-Jan-2017
 */
@Service
@EnableConfigurationProperties(IndexAndStorageConfiguration.class)
public class DataStorageService {

    private final LuceneIndexer luceneIndexer;

    private final AnnotatedDocumentParser annotatedDocParser;

    private final IndexAndStorageConfiguration dataIndexConfiguration;

    private final MapDBStorage mapDBStorage;

    @Autowired
    public DataStorageService(IndexAndStorageConfiguration dataIndexConfiguration) {
        this.dataIndexConfiguration = dataIndexConfiguration;
        this.luceneIndexer = LuceneIndexer.newInstance(dataIndexConfiguration.getIndexPath());
        this.annotatedDocParser = AnnotatedDocumentParser.newInstance(dataIndexConfiguration.getDataSourcePath());
        this.mapDBStorage = MapDBStorage.newInstance(dataIndexConfiguration.getStoragePath() + "/annDocs.db", "docTextMap");
    }

    public void processData() {
        // parse data to AnnotatedDocument-s from json
        List<AnnotatedDocument> annotatedDocuments = annotatedDocParser.parse().getAnnotatedDocuments();

        storeRawData(annotatedDocuments);

        // index parsed documents in Lucene
        luceneIndexer.createIndex(annotatedDocuments);
    }

    private void storeRawData(List<AnnotatedDocument> annotatedDocuments) {
        // generate raw text map for search results retrieval
        Map<Integer, String> id2Text = annotatedDocuments.stream()
                .collect(Collectors.toMap(a -> a.getText().hashCode(), AnnotatedDocument::getText));

        // save raw text in MapDB and refer by hash code
        mapDBStorage.save(id2Text);
    }
}
