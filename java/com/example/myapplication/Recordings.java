package com.example.myapplication;

public class Recordings {
    private String fileName;
    private String dateCreated;
    private String duration;
    private String path;
    private String fileSize;

    public String getFileSize() {
        return fileSize;
    }

    public Recordings(String fileName, String dateCreated, String duration, String path, String fileSize) {
        this.fileName = fileName;
        this.dateCreated = dateCreated;
        this.duration = duration;
        this.path = path;
        this.fileSize = fileSize;
    }

    public Recordings(String fileName, String dateCreated, String duration, String path) {
        this.fileName = fileName;
        this.dateCreated = dateCreated;
        this.duration = duration;
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getDuration() {
        return duration;
    }

    public String getPath() {
        return path;
    }
}
