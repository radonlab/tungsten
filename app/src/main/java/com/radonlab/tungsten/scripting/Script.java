package com.radonlab.tungsten.scripting;

import com.radonlab.tungsten.dto.ScriptDTO;

public class Script {

    private final String filename;

    private final String content;

    public Script(String filename, String content) {
        this.filename = filename;
        this.content = content;
    }

    public static Script from(ScriptDTO scriptDTO) {
        return new Script(scriptDTO.getName(), scriptDTO.getContent());
    }

    public String getFilename() {
        return filename;
    }

    public String getContent() {
        return content;
    }
}