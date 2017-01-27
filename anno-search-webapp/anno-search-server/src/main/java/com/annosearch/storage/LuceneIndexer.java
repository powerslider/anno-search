package com.annosearch.storage;

import com.annosearch.model.AnnotatedDocument;
import com.annosearch.model.Annotation;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.facet.FacetField;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.facet.taxonomy.TaxonomyWriter;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.annosearch.parse.AnnotatedDocumentFields.*;

/**
 * @author Tsvetan Dimitrov <tsvetan.dimitrov23@gmail.com>
 * @since 22-Jan-2017
 */
public class LuceneIndexer {

    private static final Logger LOG = LoggerFactory.getLogger(LuceneIndexer.class);

    private String indexPath;

    private String taxoPath;

    private IndexWriter indexWriter;

    private TaxonomyWriter taxonomyWriter;


    public static LuceneIndexer newInstance(String storageRootPath) {
        return new LuceneIndexer(storageRootPath);
    }

    public LuceneIndexer(String storageRootPath) {
        this.indexPath = storageRootPath + "/index";
        this.taxoPath = storageRootPath + "/taxonomy";
    }


    public void createIndex(List<AnnotatedDocument> annotatedDocuments) {
        openIndex();
        addDocuments(annotatedDocuments);
        finish();
    }

    private boolean openIndex() {
        try {
            Directory indexDir = FSDirectory.open(Paths.get(indexPath));
            Directory taxoDir = FSDirectory.open(Paths.get(taxoPath));

            Analyzer analyzer = new StandardAnalyzer();
            analyzer.setVersion(Version.LATEST);
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

            //Always overwrite the directory
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            indexWriter = new IndexWriter(indexDir, iwc);
            taxonomyWriter = new DirectoryTaxonomyWriter(taxoDir, IndexWriterConfig.OpenMode.CREATE);
            return true;
        } catch (IOException e) {
            LOG.error("Error opening index directory {}", indexPath);
        }
        return false;
    }

    private void addDocuments(List<AnnotatedDocument> annotatedDocuments) {
        FacetsConfig facetsConfig = new FacetsConfig();

        for (AnnotatedDocument annDoc : annotatedDocuments) {
            Document doc = new Document();

            for (Annotation ann : annDoc.getAnnotations()) {
                doc.add(new Field(STRING_FIELD, ann.getString(), termVectorFieldType()));
                doc.add(new StringField(CLASS_FIELD, ann.getClazz(), Field.Store.YES));
                doc.add(new StringField(TYPE_FIELD, ann.getType(), Field.Store.YES));

                List<String> subClasses = Optional.ofNullable(ann.getSubClasses())
                        .orElse(new ArrayList<>());
                for (String s : subClasses)
                    doc.add(new StringField(SUBCLASSES_FIELD, s, Field.Store.YES));


                List<String> preferredLabels = Optional.ofNullable(ann.getPreferredLabel())
                        .orElse(new ArrayList<>());
                for (String l : preferredLabels)
                    doc.add(new StringField(PREFERRED_LABEL_FIELD, l, Field.Store.YES));

                doc.add(new LongPoint(START_OFFSET_FIELD, ann.getStartOffSet()));
                doc.add(new StoredField(START_OFFSET_FIELD, ann.getStartOffSet()));
                doc.add(new LongPoint(END_OFFSET_FIELD, ann.getEndOffset()));
                doc.add(new StoredField(END_OFFSET_FIELD, ann.getEndOffset()));

                facetsConfig.setMultiValued(ann.getType(), true);
                doc.add(new FacetField(ann.getType(), ann.getString()));
            }

            doc.add(new DoublePoint(SENTIMENT_SCORE_FIELD, annDoc.getSentimentScore()));
            doc.add(new StoredField(SENTIMENT_SCORE_FIELD, annDoc.getSentimentScore()));

            doc.add(new StringField(TEXT_ID_FIELD, String.valueOf(annDoc.getId()), Field.Store.YES));

            doc.add(new StringField(TITLE_FIELD, annDoc.getTitle(), Field.Store.YES));

            // index raw text but do not store it because it will be in MapDB
            doc.add(new TextField(TEXT_FIELD, annDoc.getText(), Field.Store.YES));

            try {
                doc = facetsConfig.build(taxonomyWriter, doc);
                LOG.info("Indexed doc No. " + annDoc.getId());
                indexWriter.addDocument(doc);
            } catch (IOException e) {
                LOG.error("Error adding documents to the index. " + e.getMessage());
            }
        }
    }

    private FieldType termVectorFieldType() {
        FieldType type = new FieldType();
        type.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        type.setStored(true);
        type.setStoreTermVectors(true);
        type.setTokenized(true);
        type.setStoreTermVectorOffsets(true);

        return type;
    }

    private void finish() {
        try {
            indexWriter.commit();
            taxonomyWriter.commit();

            indexWriter.close();
            taxonomyWriter.close();
        } catch (IOException ex) {
            LOG.error("Error closing index {}", indexPath);
        }
    }
}
