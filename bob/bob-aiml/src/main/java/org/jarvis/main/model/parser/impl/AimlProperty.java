package org.jarvis.main.model.parser.impl;

import java.util.Map.Entry;

import org.jarvis.main.model.parser.IAimlProperty;

public class AimlProperty implements Entry<String,String>, IAimlProperty {
	private String key;
    private String value;

    public AimlProperty(String k, String v) {
    	key = k;
    	value = v.substring(1,v.length()-1);
    }

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String setValue(String value) {
        String old = this.value;
        this.value = value;
        return old;
	}
}
