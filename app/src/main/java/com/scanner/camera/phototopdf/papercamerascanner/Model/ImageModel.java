package com.scanner.camera.phototopdf.papercamerascanner.Model;

public class ImageModel {
    String filePath;
    boolean isSelected;

    public ImageModel(String str, boolean z) {
        this.filePath = str;
        this.isSelected = z;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String str) {
        this.filePath = str;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }
}
