package com.annosearch.service;

import com.annosearch.config.IndexAndStorageConfiguration;
import com.annosearch.search.Searcher;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.annosearch.parse.AnnotatedDocumentFields.STRING_FIELD;
import static com.annosearch.parse.AnnotatedDocumentFields.TYPE_FIELD;

/**
 * @author Tsvetan Dimitrov <tsvetan.dimitrov23@gmail.com>
 * @since 26-Jan-2017
 */
@Service
@EnableConfigurationProperties(IndexAndStorageConfiguration.class)
public class SearchService {

    private final IndexAndStorageConfiguration dataIndexConfiguration;

    private final String indexPath;

    @Autowired
    public SearchService(IndexAndStorageConfiguration dataIndexConfiguration) {
        this.dataIndexConfiguration = dataIndexConfiguration;
        this.indexPath = dataIndexConfiguration.getStorageRootPath() + "/index";
    }

    public Map<String, String> searchAnnotations(int topNCount, String annQuery) {
        List<Document> results = new Searcher<String>()
                .withIndexPath(indexPath)
                .withUserQueryInput(annQuery)
                .withQueryGenerationFunc(
                        q -> new TermQuery(new Term(STRING_FIELD, q))
                )
                .withTopNResults(topNCount)
                .search();

        return results.stream()
                .collect(Collectors.toMap(d -> d.get(STRING_FIELD), d -> d.get(TYPE_FIELD)));
    }

    public void searchDocuments(int topNCount, Map<String, String> fieldQueries) {
        List<Document> results = new Searcher<Map<String, String>>()
                .withIndexPath(indexPath)
                .withUserQueryInput(fieldQueries)
                .withQueryGenerationFunc(
                        (fQueries) -> {
                            BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();
                            fQueries.forEach((field, value) ->
                                    booleanQueryBuilder.add(
                                            new TermQuery(new Term(field, value)), BooleanClause.Occur.MUST));
                            return booleanQueryBuilder.build();
                        }
                )
                .withTopNResults(topNCount)
                .search();
    }
}
