package com.annosearch.model;

import java.util.List;

/**
 * @author Tsvetan Dimitrov <tsvetan.dimitrov23@gmail.com>
 * @since 24-Jan-2017
 */
public class AnnotatedDocument {

    private String text;
    private double sentimentScore;
    private List<Annotation> annotations;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getSentimentScore() {
        return sentimentScore;
    }

    public void setSentimentScore(double sentimentScore) {
        this.sentimentScore = sentimentScore;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }
}
