package com.radonlab.tungsten.dto;

import com.radonlab.tungsten.constant.AppConstant;
import com.radonlab.tungsten.dao.ScriptDO;

public class ScriptDTO {

    private final Long timestamp;
    private Integer id;
    private String name;
    private String content;

    public ScriptDTO(String name, String content) {
        this.id = null;
        this.name = name;
        this.content = content;
        this.timestamp = null;
    }

    private ScriptDTO(ScriptDO scriptDO) {
        this.id = scriptDO.getId();
        this.name = scriptDO.getName();
        this.content = scriptDO.getContent();
        this.timestamp = scriptDO.getModifiedTime();
    }

    public static ScriptDTO fromDO(ScriptDO scriptDO) {
        return new ScriptDTO(scriptDO);
    }

    public ScriptDO toDO() {
        if (this.id == AppConstant.UNDEFINED_SCRIPT_ID) {
            this.id = null;
        }
        if (!this.name.endsWith(AppConstant.SCRIPT_EXT)) {
            this.name = this.name + AppConstant.SCRIPT_EXT;
        }
        ScriptDO scriptDO = new ScriptDO();
        scriptDO.setId(this.id);
        scriptDO.setName(this.name);
        scriptDO.setContent(this.content);
        scriptDO.setModifiedTime(System.currentTimeMillis());
        return scriptDO;
    }

    public Integer getId() {
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