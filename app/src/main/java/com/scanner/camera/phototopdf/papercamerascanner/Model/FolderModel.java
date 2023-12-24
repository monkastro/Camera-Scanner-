package com.scanner.camera.phototopdf.papercamerascanner.Model;

public class FolderModel {
    String filePath;
    boolean isSelected;
    String name;

    public FolderModel(String str, String str2, boolean z) {
        this.name = str;
        this.filePath = str2;
        this.isSelected = z;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String str) {
        this.filePath = str;
    }
}
