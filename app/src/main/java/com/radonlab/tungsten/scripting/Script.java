package com.radonlab.tungsten.scripting;

public class Script {

    private String filename;

    private String content;

    public Script(String filename, String content) {
        this.filename = filename;
        this.content = content;
    }

    public String getFilename() {
        return filename;
    }

    public String getContent() {
        return content;
    }
}