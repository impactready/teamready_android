package org.impactready.teamready;

public class SpinnerElement {
    private String eText;
    private String eId;

    public SpinnerElement(String text, String id) {
        eText = text;
        eId = id;
    }

    public String getId() {
        return eId;
    }

    public String getText(String mText) {
       return eText;
    }

    @Override
    public String toString() {
        return eText;
    }
}