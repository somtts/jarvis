package org.jarvis.main.model.parser.category.impl;

import org.jarvis.main.model.parser.IAimlProperty;
import org.jarvis.main.model.parser.category.IAimlBot;
import org.jarvis.main.model.parser.impl.AimlElementContainer;

public class AimlBotImpl extends AimlElementContainer implements IAimlBot {

	public AimlBotImpl() {
		super("bot");
	}

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void add(IAimlProperty value) {
		if(value.getKey().compareTo("name")==0) name = accept(value);		
	}

	@Override
	public String toString() {
		return "\n\t\t\t\tAimlBot [elements=" + elements + "]";
	}
}
