package com.radonlab.tungsten.dto;

import com.radonlab.tungsten.dao.ScriptDO;

public class ScriptDTO {

    private final Integer id;

    private String name;

    private String content;

    private final Long timestamp;

    public ScriptDTO(String name, String content) {
        this.id = null;
        this.name = name;
        this.content = content;
        this.timestamp = null;
    }

    public ScriptDTO(ScriptDO scriptDO) {
        this.id = scriptDO.getId();
        this.name = scriptDO.getName();
        this.content = scriptDO.getContent();
        this.timestamp = scriptDO.getModifiedTime();
    }

    ScriptDO toDO() {
        ScriptDO scriptDO = new ScriptDO();
        scriptDO.setId(this.id);
        scriptDO.setName(this.name);
        scriptDO.setContent(this.content);
        scriptDO.setModifiedTime(null);
        return scriptDO;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}