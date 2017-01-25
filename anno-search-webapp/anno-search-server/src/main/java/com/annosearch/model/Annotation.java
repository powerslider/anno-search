package com.annosearch.model;

import java.util.List;

/**
 * @author Tsvetan Dimitrov <tsvetan.dimitrov23@gmail.com>
 * @since 24-Jan-2017
 */
public class Annotation {

    private String type;
    private String string;
    private String clazz;
    private List<String> subClasses;
    private List<String> prefferedLabel;
    private long startOffSet;
    private long endOffset;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public List<String> getSubClasses() {
        return subClasses;
    }

    public void setSubClasses(List<String> subClasses) {
        this.subClasses = subClasses;
    }

    public List<String> getPrefferedLabel() {
        return prefferedLabel;
    }

    public void setPreferredLabel(List<String> prefferedLabel) {
        this.prefferedLabel = prefferedLabel;
    }

    public long getStartOffSet() {
        return startOffSet;
    }

    public void setStartOffSet(long startOffSet) {
        this.startOffSet = startOffSet;
    }

    public long getEndOffset() {
        return endOffset;
    }

    public void setEndOffset(long endOffset) {
        this.endOffset = endOffset;
    }

    @Override
    public String toString() {
        return
            "\n===================================\n" +
            "type: " + type + "\n" +
            "string: " + string + "\n" +
            "class: " + clazz + "\n" +
            "subclasses: " + subClasses + "\n" +
            "preferredLabels: " + prefferedLabel + "\n" +
            "startOffset: " + startOffSet + "\n" +
            "endOffset: " + endOffset +
            "\n===================================\n";
    }
}
