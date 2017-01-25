package com.annosearch.search;

import com.annosearch.model.AnnotatedDocument;
import com.annosearch.model.Annotation;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static com.annosearch.parse.AnnotatedDocumentFields.*;

/**
 * @author Tsvetan Dimitrov <tsvetan.dimitrov23@gmail.com>
 * @since 22-Jan-2017
 */
public class LuceneIndexer {

    private static final Logger LOG = LoggerFactory.getLogger(LuceneIndexer.class);

    private String indexPath;

    private IndexWriter indexWriter;

    public LuceneIndexer(String indexPath) {
        this.indexPath = indexPath;
    }

    public void createIndex(List<AnnotatedDocument> annotatedDocuments) {
        openIndex();
        addDocuments(annotatedDocuments);
        finish();
    }

    private boolean openIndex() {
        try (Directory dir = FSDirectory.open(Paths.get(indexPath))) {
            Analyzer analyzer = new StandardAnalyzer();
            analyzer.setVersion(Version.LATEST);
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

            //Always overwrite the directory
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            indexWriter = new IndexWriter(dir, iwc);
            return true;
        } catch (IOException e) {
            LOG.error("Error opening index directory {}", indexPath);
        }
        return false;
    }

    private void addDocuments(List<AnnotatedDocument> annotatedDocuments) {
        for (AnnotatedDocument annDoc : annotatedDocuments) {
            Document doc = new Document();
            for (Annotation ann : annDoc.getAnnotations()) {
                doc.add(new StringField(STRING_FIELD, ann.getString(), Field.Store.YES));
                doc.add(new StringField(CLASS_FIELD, ann.getClazz(), Field.Store.YES));
                doc.add(new StringField(TYPE_FIELD, ann.getType(), Field.Store.YES));
                doc.add(new StringField(STRING_FIELD, ann.getString(), Field.Store.YES));
                ann.getSubClasses().forEach(s -> doc.add(new StringField(SUBCLASSES_FIELD, s, Field.Store.YES)));
                ann.getPrefferedLabel().forEach(l -> doc.add(new StringField(PREFERRED_LABEL_FIELD, l, Field.Store.YES)));
                doc.add(new LegacyLongField(START_OFFSET_FIELD, ann.getStartOffSet(),  Field.Store.YES));
                doc.add(new LegacyLongField(END_OFFSET_FIELD, ann.getEndOffset(),  Field.Store.YES));
            }
            doc.add(new LegacyDoubleField(SENTIMENT_SCORE_FIELD, annDoc.getSentimentScore(), Field.Store.YES));
            doc.add(new StringField(TEXT_FIELD, annDoc.getText(), Field.Store.YES));
            try {
                indexWriter.addDocument(doc);
            } catch (IOException e) {
                LOG.error("Error adding documents to the index. " + e.getMessage());
            }
        }
    }

    private void finish() {
        try {
            indexWriter.commit();
            indexWriter.close();
        } catch (IOException ex) {
            LOG.error("Error closing index {}", indexPath);
        }
    }
}
