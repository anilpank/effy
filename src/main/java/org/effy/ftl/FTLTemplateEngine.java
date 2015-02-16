package org.effy.ftl;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.effy.string.StringUtils;

import freemarker.core.Environment;
import freemarker.core.InvalidReferenceException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Process String based FTL/Freemarker syntax. You can use this if you want to add freemarker support to your Java backend
 * @author averma
 *
 */
public enum FTLTemplateEngine  {
	
	INSTANCE;
	private final Configuration cfg;	

	private FTLTemplateEngine() {
		cfg = new Configuration();		
	}
	
	/**
	 * 
	 * @param template - Input string that would be processed. For example ${doc.book.title}
	 * @param input - Map that is required for intepreting the above String
	 * @return - Processed result.
	 * @throws IOException
	 * @throws TemplateException
	 */
	public String process(String template, Map<String, Object> input) throws IOException, TemplateException {
		String rc = null;
		final Writer out = new StringWriter();		
		try {
			if(StringUtils.isEmpty(template)){
				return "";
			}
			final Template temp =new Template("TemporaryTemplate", new StringReader(template), cfg);
			temp.process(input, out);
		}
		catch (InvalidReferenceException e) {			
			throw new InvalidReferenceException("FTL expression has evaluated to null or it refers to something that doesn't exist.  - " + template, Environment.getCurrentEnvironment());
		}
		catch (TemplateException e) {			
			throw new TemplateException("Unable to process FTL - " + template, e, Environment.getCurrentEnvironment());
		}
		catch (IOException e) {			
			throw new IOException("Unable to process FTL - " + template);
		}
		rc = out.toString();
		out.close();
		return rc.trim();
	}	

}
