package org.jbake.forge.addon.types;

public enum TemplateType {

	FreeMarker("FreeMarker"), Groovy("Groovy"), Thymeleaf("Thymeleaf");

	private String text;

	TemplateType() {
	}

	TemplateType(String theText) {
		text = theText;
	}

	public String text() {
		return text;
	}

	public String toString() {
		return text;
	}

}
