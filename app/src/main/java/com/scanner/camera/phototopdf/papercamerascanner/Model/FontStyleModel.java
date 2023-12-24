package com.scanner.camera.phototopdf.papercamerascanner.Model;

public class FontStyleModel {
    private String fontName;
    private boolean isSelected;
    private int typeFace;

    public FontStyleModel(int i, String str, boolean z) {
        this.typeFace = i;
        this.fontName = str;
        this.isSelected = z;
    }

    public int getTypeFace() {
        return this.typeFace;
    }

    public void setTypeFace(int i) {
        this.typeFace = i;
    }

    public String getFontName() {
        return this.fontName;
    }

    public void setFontName(String str) {
        this.fontName = str;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }
}
