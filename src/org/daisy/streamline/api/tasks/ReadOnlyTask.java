package org.daisy.streamline.api.tasks;

import java.io.File;

import org.daisy.streamline.api.media.AnnotatedFile;

/**
 * Provides an abstract base for read only tasks. A read only task is 
 * a task that does not produce an altered output.
 * 
 * @author Joel Håkansson
 *
 */
public abstract class ReadOnlyTask extends InternalTask { //NOPMD

	/**
	 * Creates a new read only task with the specified name
	 * @param name the name of the task
	 */
	public ReadOnlyTask(String name) {
		super(name);
	}

	/**
	 * Apply the task to <code>input</code>
	 * @param input input file
	 * @throws InternalTaskException throws InternalTaskException if something goes wrong.
	 */
	public abstract void execute(File input) throws InternalTaskException;
	
	/**
	 * 
	 * @param input input file
	 * @throws InternalTaskException throws InternalTaskException if something goes wrong
	 */
	public abstract void execute(AnnotatedFile input) throws InternalTaskException;

}
