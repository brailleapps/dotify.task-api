package org.daisy.streamline.api.tasks.library;

import java.util.Map;

import javax.xml.transform.Result;
import javax.xml.transform.Source;

/**
 * Provides an interface for an XSLT transform implementation.
 * @author Joel Håkansson
 *
 */
@FunctionalInterface
public interface XsltApplier {

	/**
	 * Transforms the <code>source</code> using the <code>xslt</code> and the <code>params</code> and
	 * creates the <code>result</code>.
	 * @param source the source
	 * @param result the result
	 * @param xslt the xslt
	 * @param params the parameters
	 * @throws XsltApplierException if something goes wrong
	 */
	public void transform(Source source, Result result, Source xslt, Map<String, Object> params) throws XsltApplierException;
}
