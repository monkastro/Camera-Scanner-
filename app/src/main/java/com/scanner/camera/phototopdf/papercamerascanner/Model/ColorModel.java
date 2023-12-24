package com.scanner.camera.phototopdf.papercamerascanner.Model;

public class ColorModel {
    String colorCode;
    private boolean isSelected;

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }

    public ColorModel(String str, boolean z) {
        this.colorCode = str;
        this.isSelected = z;
    }

    public String getColorCode() {
        return this.colorCode;
    }

    public void setColorCode(String str) {
        this.colorCode = str;
    }
}
