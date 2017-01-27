package com.annosearch.service;

import com.annosearch.config.IndexAndStorageConfiguration;
import com.annosearch.model.AnnotatedDocument;
import com.annosearch.model.AnnotatedDocumentSearchResult;
import com.annosearch.model.Annotation;
import com.annosearch.search.Searcher;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.annosearch.parse.AnnotatedDocumentFields.*;

/**
 * @author Tsvetan Dimitrov <tsvetan.dimitrov23@gmail.com>
 * @since 26-Jan-2017
 */
@Service
@EnableConfigurationProperties(IndexAndStorageConfiguration.class)
public class SearchService {

    private static final Logger LOG = LoggerFactory.getLogger(SearchService.class);

    private final IndexAndStorageConfiguration dataIndexConfiguration;

    private final String indexPath;

    @Autowired
    public SearchService(IndexAndStorageConfiguration dataIndexConfiguration) {
        this.dataIndexConfiguration = dataIndexConfiguration;
        String storageRootPath = dataIndexConfiguration.getStorageRootPath();

        this.indexPath = storageRootPath + "/index";
    }

    public Set<Map.Entry<String, String>> searchAnnotations(int topNCount, String annQuery) {
        Map<String, Document> results = new Searcher<String>()
                .withIndexPath(indexPath)
                .withUserQueryInput(annQuery)
                .withQueryGenerationFunc(
                        q -> {
                            try {
                                return new QueryParser(STRING_FIELD, new StandardAnalyzer()).parse(q);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                )
                .withTopNResults(topNCount)
                .search();

        Set<Map.Entry<String, String>> entries = new HashSet<>();
        for (Document d : results.values()) {
            String[] strVals = d.getValues(STRING_FIELD);
            String[] typeVals = d.getValues(TYPE_FIELD);
            for (int i = 0; i < strVals.length; i++) {
                if (strVals[i].contains(annQuery)) {
                    entries.add(new AbstractMap.SimpleEntry<>(strVals[i], typeVals[i]));
                }
            }
        }

        return entries;
    }

    public List<AnnotatedDocumentSearchResult> searchDocuments(int topNCount, String strVal, String typeVal) {
        Map<String, Document> results = new Searcher<Map.Entry<String, String>>()
                .withIndexPath(indexPath)
                .withUserQueryInput(new AbstractMap.SimpleEntry<>(strVal, typeVal))
                .withQueryGenerationFunc(
                        q -> {
                            try {
                                String query = "type:" + q.getKey() + " string:" + q.getValue();
                                return new MultiFieldQueryParser(new String[] { STRING_FIELD, TYPE_FIELD }, new StandardAnalyzer())
                                        .parse(query);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
//                        q -> new TermQuery(new Term(STRING_FIELD, q.get("string")))
//                        (fQueries) -> {
//                            BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();
//                            fQueries.forEach((field, value) ->
//                                    booleanQueryBuilder.add(
//                                            new TermQuery(new Term(field, value)), BooleanClause.Occur.FILTER));
//                            return booleanQueryBuilder.build();
//                        }
                )
                .withTopNResults(topNCount)
                .search();

        List<AnnotatedDocumentSearchResult> annDocs = new ArrayList<>();
        results.forEach((matchedText, doc) -> {

            AnnotatedDocumentSearchResult annDoc = new AnnotatedDocumentSearchResult();
            annDoc.setTitle(doc.get(TITLE_FIELD));

            int textId = Integer.parseInt(doc.get(TEXT_ID_FIELD));
            annDoc.setId(textId);

            annDoc.setMatchedText(matchedText);

            double sentimentScore = Double.parseDouble(doc.get(SENTIMENT_SCORE_FIELD));
            annDoc.setSentimentScore(sentimentScore);

            annDocs.add(annDoc);
        });

        return annDocs;
    }

    public AnnotatedDocument getAnnotatedDocumentPerId(String id) {
        Map<String, Document> results = new Searcher<String>()
                .withIndexPath(indexPath)
                .withUserQueryInput(id)
                .withQueryGenerationFunc(
                        q -> {
                            try {
                                return new QueryParser(TEXT_ID_FIELD, new StandardAnalyzer()).parse(q);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                )
                .withTopNResults(1)
                .search();

        return extractAnnotatedDocument(results.values().iterator().next());
    }

    @NotNull
    private AnnotatedDocument extractAnnotatedDocument(Document luceneDoc) {
        AnnotatedDocument annDoc = new AnnotatedDocument();
        annDoc.setTitle(luceneDoc.get(TITLE_FIELD));

        int textId = Integer.parseInt(luceneDoc.get(TEXT_ID_FIELD));
        annDoc.setId(textId);

        String rawText = luceneDoc.get(TEXT_FIELD);
        annDoc.setText(rawText);

        double sentimentScore = Double.parseDouble(luceneDoc.get(SENTIMENT_SCORE_FIELD));
        annDoc.setSentimentScore(sentimentScore);

        String[] stringFields = luceneDoc.getValues(STRING_FIELD);
        String[] typeFields = luceneDoc.getValues(TYPE_FIELD);
        String[] classFields = luceneDoc.getValues(CLASS_FIELD);
        String[] startOffsetFields = luceneDoc.getValues(START_OFFSET_FIELD);
        String[] endOffsetFields = luceneDoc.getValues(END_OFFSET_FIELD);
//            String[] subClassesFields = luceneDoc.getValues(SUBCLASSES_FIELD);
//            String[] preferredLabelFields = luceneDoc.getValues(PREFERRED_LABEL_FIELD);

        List<Annotation> annotations = new ArrayList<>();
        for (int i = 0; i < stringFields.length; i++) {
            Annotation annotation = new Annotation();
            annotation.setString(stringFields[i]);
            annotation.setType(typeFields[i]);
            annotation.setClazz(classFields[i]);
            annotation.setStartOffSet(Long.parseLong(startOffsetFields[i]));
            annotation.setEndOffset(Long.parseLong(endOffsetFields[i]));
//                annotation.setSubClasses(subClassesFields[i].stringValue());
//                annotation.setPreferredLabel(preferredLabelFields[i].stringValue());
            annotations.add(annotation);
        }
        annDoc.setAnnotations(annotations);

        return annDoc;
    }
}
