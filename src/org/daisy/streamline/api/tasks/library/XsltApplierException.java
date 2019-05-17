package org.daisy.streamline.api.tasks.library;

/**
 * Provides an exception for errors that could occur when using {@link XsltApplier}.
 * @author Joel Håkansson
 *
 */
public class XsltApplierException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2296717558929858305L;


	/**
	 * Constructs a new exception with null as its detail message.
	 */
	public XsltApplierException() {
		super();
	}

	/**
	 * Constructs a new exception with the specified detail message.
	 * @param message the detail message
	 */
	public XsltApplierException(String message) {
		super(message);
	}

	/**
	 * Constructs a new exception with the specified cause
	 * @param cause the cause
	 */

	public XsltApplierException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new exception with the specified detail message and cause.
	 * @param message the detail message
	 * @param cause the cause
	 */
	public XsltApplierException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new exception with the specified detail message,
	 * cause, suppression enabled or disabled, and writable stack
	 * trace enabled or disabled.
	 *
	 * @param  message the detail message
	 * @param cause the cause
	 * @param enableSuppression whether or not suppression is enabled or disabled
	 * @param writableStackTrace whether or not the stack trace should be writable
	 */
	public XsltApplierException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
