package com.scanner.camera.phototopdf.papercamerascanner.Model;

public class CreationModel {
    String fileDateTime;
    String fileFolder;
    String fileName;
    String filePath;
    long fileSize;

    public CreationModel(String str, String str2, String str3, String str4, long j) {
        this.fileName = str;
        this.fileFolder = str2;
        this.filePath = str3;
        this.fileDateTime = str4;
        this.fileSize = j;
    }

    public CreationModel() {
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String str) {
        this.fileName = str;
    }

    public String getFileFolder() {
        return this.fileFolder;
    }

    public void setFileFolder(String str) {
        this.fileFolder = str;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String str) {
        this.filePath = str;
    }

    public String getFileDateTime() {
        return this.fileDateTime;
    }

    public void setFileDateTime(String str) {
        this.fileDateTime = str;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(long j) {
        this.fileSize = j;
    }
}
