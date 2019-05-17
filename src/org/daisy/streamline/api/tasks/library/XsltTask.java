package org.daisy.streamline.api.tasks.library;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.daisy.streamline.api.media.AnnotatedFile;
import org.daisy.streamline.api.media.DefaultAnnotatedFile;
import org.daisy.streamline.api.option.UserOption;
import org.daisy.streamline.api.option.UserOptionValue;
import org.daisy.streamline.api.tasks.InternalTaskException;
import org.daisy.streamline.api.tasks.ReadWriteTask;

/**
 * <p>Task that runs an XSLT conversion.</p>
 * <p>Input file type requirement: XML</p>
 * 
 * @author  Joel Håkansson
 */
public class XsltTask extends ReadWriteTask {
	private static final Logger logger = Logger.getLogger(XsltTask.class.getCanonicalName());
	private final XsltApplier applier;
	final URL url;
	final Map<String, Object> options;
	List<UserOption> uiOptions;
	
	/**
	 * <p>Create a new XSLT task.</p>
	 * 
	 * <p>User options are collected from the xslt itself.</p>
	 * 
	 * <p>The following applies to an XSLT, given that the namespace
	 * <code>xtd</code> is bound to 
	 * <code>https://www.ologolo.org/ns/doc/xsl</code>:</p>
	 * 
	 * <ul><li>The value of <code>@xtd:desc</code> on any top level
	 * <code>xsl:param</code> will be used as description
	 * for the parameter.</li>
	 * 
	 * <li><p>The value of <code>@xtd:default</code> on any top level
	 * <code>xsl:param</code> will be used as default
	 * value for the parameter.</p>
	 * <p>Note: Determining the default value from @select is
	 * unfortunately very tricky to do from another XSLT as it requires dynamic 
	 * XPath evaluation and, in the general cases, access to the input document
	 * as well.</p></li>
	 * 
	 * <li>The value of <code>@xtd:values</code> on any top level
	 * <code>xsl:param</code> will be used to enumerate
	 * acceptable values for the parameter.</li>
	 * </ul>
	 * 
	 * @param name task name
	 * @param url relative path to XSLT
	 * @param options XSLT parameters
	 * @param applier an xslt applier
	 */
	public XsltTask(String name, URL url, Map<String, Object> options, XsltApplier applier) {
		this(name, url, options, null, applier);
	}
	/**
	 * Creates a new XSLT task. Use system property javax.xml.transform.TransformerFactory
	 * to set factory implementation if needed.
	 * @param name the task name
	 * @param url the relative path to the XSLT
	 * @param options the xslt parameters
	 * @param uiOptions the options presented to a user
	 * @param applier xml applier
	 */
	public XsltTask(String name, URL url, Map<String, Object> options, List<UserOption> uiOptions, XsltApplier applier) {
		super(name);
		this.url = url;
		this.options = options;
		this.uiOptions = uiOptions;
		this.applier = applier;
	}
	
	private List<UserOption> buildOptions() {
		List<UserOption> ret = new ArrayList<>();
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			applier.transform(
					toSource(url), 
					toResult(os), 
					toSource(this.getClass().getResource("resource-files/list-params.xsl")),
					new HashMap<String, Object>()
			);
			Properties px = new Properties();
			px.loadFromXML(new ByteArrayInputStream(os.toByteArray()));
			for (Entry<Object, Object> entry : px.entrySet()) {
				List<String> fields = splitFields(entry.getValue().toString());
				UserOption.Builder builder = new UserOption.Builder(entry.getKey().toString());
				if (fields.size()>0) {
					builder.defaultValue(fields.get(0));
					if (fields.size()>1) {
						if (!"".equals(fields.get(1))) {
							String[] values = fields.get(1).split("/");
							for (String value : values) {
								builder.addValue(new UserOptionValue.Builder(value).build());
							}
						}
						if (fields.size()>2) {
							builder.description(fields.get(2));
						}
					}
				}
				ret.add(builder.build());
			}
		} catch (XsltApplierException | IOException e) {
			logger.log(Level.FINE, "Failed to compile options for xslt: " + url, e);
		} 
		return ret;
	}
	
	static List<String> splitFields(String input) {
		List<String> ret = new ArrayList<>();
		int last = 0;
		// not using split here, because the last field is wanted, even if it is empty
		for (int i=0; i<input.length(); i++) {
			if (input.charAt(i)=='\t') {
				ret.add(input.substring(last, i));
				last = i+1;
			}
		}
		ret.add(input.substring(last, input.length()));
		return ret;
	}

	@Override
	public AnnotatedFile execute(AnnotatedFile input, File output) throws InternalTaskException {
		try {
			applier.transform(
					toSource(input.getPath().toFile()), 
					toResult(output), 
					toSource(url),
					options
			);
		} catch (XsltApplierException e) {
			throw new InternalTaskException("Error: ", e);
		}
		return new DefaultAnnotatedFile.Builder(output.toPath()).extension("xml").mediaType("application/xml").build();
	}

	@Override
	@Deprecated
	public void execute(File input, File output) throws InternalTaskException {
		execute(new DefaultAnnotatedFile.Builder(input).build(), output);
	}

	@Override
	public List<UserOption> getOptions() {
		if (uiOptions==null) {
			this.uiOptions = buildOptions();
		}
		return uiOptions;
	}
	
	private static Source toSource(File source) throws XsltApplierException {
		return new StreamSource(source);
	}
	
	private static Source toSource(URL source) throws XsltApplierException {
		try {
			// Compare to {@link StreamSource#StreamSource(File)}
			return new StreamSource(source.toURI().toASCIIString());
		} catch (URISyntaxException e) {
			throw new XsltApplierException(e);
		}
	}

	private static Result toResult(File result) throws XsltApplierException {
		return new StreamResult(result);
	}
	
	private static Result toResult(OutputStream result) throws XsltApplierException {
		return new StreamResult(result);
	}

}
