package org.jbake.forge.addon.types;

public enum JBakeTemplateType {

	FREEMARKER("FreeMarker"), GROOVY("Groovy"), THYMELEAF("Thymeleaf");

	private String text;

	JBakeTemplateType() {
	}

	JBakeTemplateType(String theText) {
		text = theText;
	}

	public String text() {
		return text;
	}

	public String toString() {
		return text;
	}

}
