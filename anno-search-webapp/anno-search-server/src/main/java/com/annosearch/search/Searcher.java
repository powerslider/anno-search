package com.annosearch.search;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author Tsvetan Dimitrov <tsvetan.dimitrov23@gmail.com>
 * @since 26-Jan-2017
 */
public class Searcher<I> {

    private static final Logger LOG = LoggerFactory.getLogger(Searcher.class);

    public Searcher<I> withIndexPath(String indexPath) {
        this.indexPath = indexPath;
        return this;
    }

    public Searcher<I> withTopNResults(int topNResults) {
        this.topNResults = topNResults;
        return this;
    }

    public Searcher<I> withUserQueryInput(I userQueryInput) {
        this.userQueryInput = userQueryInput;
        return this;
    }

    public Searcher<I> withQueryGenerationFunc(Function<I, Query> queryGenerationFunc) {
        this.queryGenerationFunc = queryGenerationFunc;
        return this;
    }

    private String indexPath;
    private int topNResults;
    private I userQueryInput;
    private Function<I, Query> queryGenerationFunc;

    public List<Document> search() {
        notNullOrThrow(indexPath, "indexPath");
        notNullOrThrow(userQueryInput, "userQueryInput");
        notNullOrThrow(queryGenerationFunc, "queryGenerationFunc");

        try (Directory indexDirectory = FSDirectory.open(Paths.get(indexPath))) {
            IndexReader indexReader = DirectoryReader.open(indexDirectory);
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);

            Query query = queryGenerationFunc.apply(userQueryInput);

            TopDocs topDocs = indexSearcher.search(query, topNResults > 0 ? topNResults : 10);
            List<Document> luceneDocs = new ArrayList<>();
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                int docId = scoreDoc.doc;
                Document indexDoc = indexSearcher.doc(docId);
                luceneDocs.add(indexDoc);
            }
            return luceneDocs;
        } catch (IOException e) {
            LOG.error("Error querying index {}. ", indexPath, e);
        }
        return new ArrayList<>();
    }

    private void notNullOrThrow(Object obj, String msg) {
        Optional.ofNullable(obj)
                .orElseThrow(() -> new IllegalArgumentException(msg + " cannot be null"));
    }

}
