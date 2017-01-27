package com.annosearch.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.annosearch.parse.AnnotatedDocumentFields.TEXT_FIELD;

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

    public Map<String, Document> search() {
        notNullOrThrow(indexPath, "indexPath");
        notNullOrThrow(userQueryInput, "userQueryInput");
        notNullOrThrow(queryGenerationFunc, "queryGenerationFunc");

        try (Directory indexDirectory = FSDirectory.open(Paths.get(indexPath))) {
            IndexReader indexReader = DirectoryReader.open(indexDirectory);
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);

            Analyzer analyzer = new StandardAnalyzer();

            Query query = queryGenerationFunc.apply(userQueryInput);

            TopDocs topDocs = indexSearcher.search(query, topNResults > 0 ? topNResults : 10);

            SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter();
            Highlighter highlighter = new Highlighter(htmlFormatter, new QueryScorer(query));

            Map<String, Document> luceneDocs = new HashMap<>();
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                int docId = scoreDoc.doc;
                Document indexDoc = indexSearcher.doc(docId);
                String text = indexDoc.get(TEXT_FIELD);
                TokenStream tokenStream = TokenSources.getAnyTokenStream(indexReader, docId, TEXT_FIELD, analyzer);
                TextFragment[] bestTextFragments = highlighter
                        .getBestTextFragments(tokenStream, text, false, 4);
                StringBuilder builder = new StringBuilder();
                for (TextFragment frag : bestTextFragments) {
                    if (frag != null && (frag.getScore() > 0)) {
                        String fr = frag.toString().replaceAll("\\R", ";");
                        builder.append(fr);
                    }
                }

//                String[] strings = indexDoc.getValues(STRING_FIELD);
//                TokenStream stringTokenStream = TokenSources.getAnyTokenStream(indexReader, docId, STRING_FIELD, analyzer);
//                for (String s : strings) {
//                    if (s.length() > query.toString().length()) {
//                        TextFragment[] strBestTextFragments = highlighter
//                                .getBestTextFragments(stringTokenStream, s, false, 1);
//                        for (TextFragment frag : strBestTextFragments) {
//                            if (frag != null && (frag.getScore() > 0)) {
//                                System.out.println(frag.toString());
//                            }
//                        }
//                    }
//                }
                luceneDocs.put(builder.toString(), indexDoc);
            }
            return luceneDocs;
        } catch (IOException e) {
            LOG.error("Error querying index {}. ", indexPath, e);
        } catch (InvalidTokenOffsetsException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    private void notNullOrThrow(Object obj, String msg) {
        Optional.ofNullable(obj)
                .orElseThrow(() -> new IllegalArgumentException(msg + " cannot be null"));
    }
}
