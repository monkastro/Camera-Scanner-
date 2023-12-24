package com.scanner.camera.phototopdf.papercamerascanner.Model;

public class DataModel {
    String DirName;
    String FileName;

    public DataModel(String str) {
        this.DirName = str;
    }

    public DataModel(String str, String str2) {
        this.DirName = str;
        this.FileName = str2;
    }

    public String getDirName() {
        return this.DirName;
    }

    public void setDirName(String str) {
        this.DirName = str;
    }

    public String getFileName() {
        return this.FileName;
    }

    public void setFileName(String str) {
        this.FileName = str;
    }
}
