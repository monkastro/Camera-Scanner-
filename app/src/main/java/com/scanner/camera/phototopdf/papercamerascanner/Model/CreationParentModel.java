package com.scanner.camera.phototopdf.papercamerascanner.Model;

public class CreationParentModel {
    int count;
    String fileDateTime;
    String fileFolder;
    long fileSize;
    String folderPath;

    public CreationParentModel(String str, String str2, String str3, int i, long j) {
        this.fileFolder = str;
        this.folderPath = str2;
        this.fileDateTime = str3;
        this.count = i;
        this.fileSize = j;
    }

    public CreationParentModel() {
    }

    public String getFileFolder() {
        return this.fileFolder;
    }

    public void setFileFolder(String str) {
        this.fileFolder = str;
    }

    public String getFolderPath() {
        return this.folderPath;
    }

    public void setFolderPath(String str) {
        this.folderPath = str;
    }

    public String getFileDateTime() {
        return this.fileDateTime;
    }

    public void setFileDateTime(String str) {
        this.fileDateTime = str;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int i) {
        this.count = i;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(long j) {
        this.fileSize = j;
    }
}
