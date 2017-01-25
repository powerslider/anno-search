package com.annosearch.search;

import com.annosearch.model.AnnotatedDocument;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
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

//        for (AnnotatedDocument annDoc : annotatedDocuments) {
//            Document doc = new Document();
//            doc.add(new StringField(field, (String) object.get(field), Field.Store.NO));
//            doc.add(new LegacyLongField(field, (long) object.get(field), Field.Store.YES));
//            doc.add(new LegacyDoubleField(field, (double) object.get(field), Field.Store.YES));
//            doc.add(new StringField(field, object.get(field).toString(), Field.Store.YES));
//            try {
//                indexWriter.addDocument(doc);
//            } catch (IOException ex) {
//                System.err.println("Error adding documents to the index. " + ex.getMessage());
//            }
//        }
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
