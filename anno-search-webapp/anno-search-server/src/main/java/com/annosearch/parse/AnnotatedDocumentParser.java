package com.annosearch.parse;

import com.annosearch.model.AnnotatedDocument;
import com.annosearch.model.Annotation;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.annosearch.parse.AnnotatedDocumentFields.*;

/**
 * @author Tsvetan Dimitrov <tsvetan.dimitrov23@gmail.com>
 * @since 24-Jan-2017
 */
public class AnnotatedDocumentParser {

    private static final Logger LOG = LoggerFactory.getLogger(AnnotatedDocumentParser.class);

    private static final Pattern DOC_ID_PATTERN = Pattern.compile(".*\\/(\\d+)\\.json");

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
                LOG.info(++c + "->" + f);
                annotatedDocuments.add(parseDocument(f));
            }
        }
    }

    private AnnotatedDocument parseDocument(File jsonFile) {
        JSONObject mainObj = parseJSON(jsonFile);

        JSONObject entities = (JSONObject) mainObj.get("entities");
        String text = (String) mainObj.get("text");

        String title = StringUtils.substringBefore(text, "\n");

        Double sentimentScore = (Double) Optional
                .ofNullable(mainObj.get("sentimentScore"))
                .orElse(0.0d);

        int id = generateDocId(jsonFile, text);

        List<Annotation> annotations = extractAnnotations(entities);

        AnnotatedDocument document = new AnnotatedDocument();
        document.setId(id);
        document.setAnnotations(annotations);
        document.setTitle(title);
        document.setText(text);
        document.setSentimentScore(sentimentScore);

        return document;
    }

    private int generateDocId(File jsonFile, String text) {
        Matcher matcher = DOC_ID_PATTERN.matcher(jsonFile.toString());
        if (matcher.matches()) {
            int i = Integer.parseInt(matcher.group(1));
            return i;
        }
        return text.hashCode();
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
