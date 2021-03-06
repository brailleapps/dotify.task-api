package org.daisy.streamline.api.identify;

import org.daisy.streamline.api.identity.IdentificationFailedException;
import org.daisy.streamline.api.identity.Identifier;
import org.daisy.streamline.api.identity.IdentifierFactory;
import org.daisy.streamline.api.media.AnnotatedFile;
import org.daisy.streamline.api.media.AnnotatedInputStream;
import org.daisy.streamline.api.media.DefaultAnnotatedFile;
import org.daisy.streamline.api.media.FileDetails;
import org.daisy.streamline.api.media.InputStreamSupplier;

@SuppressWarnings("javadoc")
public class TestFactory implements IdentifierFactory {

	@Override
	public Identifier newIdentifier() {
		return new Identifier() {@Override
		public AnnotatedFile identify(AnnotatedFile f) throws IdentificationFailedException {
			return DefaultAnnotatedFile.with(f).mediaType("application/test").build();
		}

		@Override
		public AnnotatedInputStream identify(InputStreamSupplier f) throws IdentificationFailedException {
			return null;
		}};
	}

	@Override
	public boolean accepts(FileDetails type) {
		return type.getExtension().equals("unk");
	}

	@Override
	public void setCreatedWithSPI() {
	}

}
