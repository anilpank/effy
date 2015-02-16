package org.effy.ftl;

import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;

/**
 * Internal class
 * @author averma
 *
 */
public class TemplateLoaderFactory {

	public static StringTemplateLoader createTemplateLoader() {
		StringTemplateLoader loader = new StringTemplateLoader();
		loader.putTemplate(TemplateConstants.TEMPLATE_NAME, "Default");
		loader.putTemplate(TemplateConstants.TEMPLATE_NAME, "Default2");
		
		return loader;
	}

}
