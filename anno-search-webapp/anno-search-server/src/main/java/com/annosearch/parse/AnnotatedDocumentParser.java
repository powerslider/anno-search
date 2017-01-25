package com.annosearch.parse;

import com.annosearch.model.AnnotatedDocument;
import com.annosearch.model.Annotation;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.annosearch.parse.AnnotatedDocumentFields.*;

/**
 * @author Tsvetan Dimitrov <tsvetan.dimitrov23@gmail.com>
 * @since 24-Jan-2017
 */
public class AnnotatedDocumentParser {

    private String dataSourcePath;

    private List<AnnotatedDocument> annotatedDocuments = new ArrayList<>();

    public static AnnotatedDocumentParser newInstance(String dataSourcePath) {
        return new AnnotatedDocumentParser(dataSourcePath);
    }


    private AnnotatedDocumentParser(String dataSourcePath) {
        this.dataSourcePath = dataSourcePath;
    }


    public List<AnnotatedDocument> getAnnotatedDocuments() {
        return annotatedDocuments;
    }

    public AnnotatedDocumentParser parse() {
        Path path = Paths.get(dataSourcePath);
        if (Files.exists(path)) {
            listJsonFilesAndParse(path.toFile());
        }

        return this;
    }

    private void listJsonFilesAndParse(File file) {
        int c = 0;
        for (File f : file.listFiles()) {
            if (f.isDirectory()) {
                listJsonFilesAndParse(f);
            } else {
                System.out.println(++c + "->" + f);
                annotatedDocuments.add(parseDocument(f));
            }
        }
    }

    private AnnotatedDocument parseDocument(File jsonFile) {
        JSONObject mainObj = parseJSON(jsonFile);

        JSONObject entities = (JSONObject) mainObj.get("entities");
        String text = (String) mainObj.get("text");
        Double sentimentScore = (Double) Optional
                .ofNullable(mainObj.get("sentimentScore"))
                .orElse(0.0d);

        List<Annotation> annotations = extractAnnotations(entities);

        AnnotatedDocument document = new AnnotatedDocument();
        document.setAnnotations(annotations);
        document.setText(text);
        document.setSentimentScore(sentimentScore);

        return document;
    }

    private List<Annotation> extractAnnotations(JSONObject entities) {
        List<Annotation> annotations = new ArrayList<>();

        for (Object key : entities.keySet()) {
            JSONArray typedAnnotations = (JSONArray) entities.get(key);

            for (Object ann : typedAnnotations) {
                JSONObject ann1 = (JSONObject) ann;
                Annotation currentAnnotation = new Annotation();

                for (Object k : ann1.keySet()) {
                    Object v = ann1.get(k);
                    switch ((String) k) {
                        case STRING_FIELD:
                            currentAnnotation.setString((String) v);
                            break;
                        case CLASS_FIELD:
                            currentAnnotation.setClazz((String) v);
                            break;
                        case PREFERRED_LABEL_FIELD:
                            currentAnnotation.setPreferredLabel((List<String>) v);
                            break;
                        case TYPE_FIELD:
                            currentAnnotation.setType((String) v);
                            break;
                        case INDICES_FIELD:
                            List<Long> indices = (List<Long>) v;
                            currentAnnotation.setStartOffSet(indices.get(0));
                            currentAnnotation.setEndOffset(indices.get(1));
                            break;
                        case SUBCLASSES_FIELD:
                            currentAnnotation.setSubClasses((List<String>) v);
                            break;
                    }
                }
                annotations.add(currentAnnotation);
            }
        }
        return annotations;
    }

    private JSONObject parseJSON(File jsonFile) {
        //Get the JSON file, in this case is in ~/resources/test.json
        try (InputStream is = new FileInputStream(jsonFile);
             Reader readerJson = new InputStreamReader(is)) {
            Object fileObjects = JSONValue.parse(readerJson);
            return (JSONObject) fileObjects;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
