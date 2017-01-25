package com.annosearch.search;

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

/**
 * @author Tsvetan Dimitrov <tsvetan.dimitrov23@gmail.com>
 * @since 24-Jan-2017
 */
public class AnnotatedDocumentParser {

    private String jsonDataSource;

    private List<AnnotatedDocument> annotatedDocuments;


    public AnnotatedDocumentParser(String jsonDataSource) {
        this.jsonDataSource = jsonDataSource;
    }


    public List<AnnotatedDocument> getAnnotatedDocuments() {
        return annotatedDocuments;
    }

    public AnnotatedDocumentParser parse() {
        Path path = Paths.get(jsonDataSource);
        if (Files.exists(path)) {
            for (File file : path.toFile().listFiles()) {
                listJsonFiles(file);
            }
        }

        return this;
    }

    private void listJsonFiles(File file) {
        for (File f : file.listFiles()) {
            if (f.isDirectory()) {
                listJsonFiles(f);
            } else {
                annotatedDocuments.add(parseDocument(f));
            }
        }
    }

    private AnnotatedDocument parseDocument(File jsonFile) {
        JSONObject mainObj = parseJSON(jsonFile);

        JSONObject entities = (JSONObject) mainObj.get("entities");
        String text = (String) mainObj.get("text");
        double sentimentScore = (double) mainObj.get("sentimentScore");

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
                        case "string":
                            currentAnnotation.setString((String) v);
                            break;
                        case "class":
                            currentAnnotation.setClazz((String) v);
                            break;
                        case "preferredLabel":
                            currentAnnotation.setPreferredLabel((List<String>) v);
                            break;
                        case "type":
                            currentAnnotation.setType((String) v);
                            break;
                        case "indices":
                            List<Long> indices = (List<Long>) v;
                            currentAnnotation.setStartOffSet(indices.get(0));
                            currentAnnotation.setEndOffset(indices.get(1));
                            break;
                        case "subclasses":
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
