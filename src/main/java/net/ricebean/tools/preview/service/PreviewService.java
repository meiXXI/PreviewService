package net.ricebean.tools.preview.service;

import javax.xml.bind.JAXBException;

public interface PreviewService {

	/**
	 * Process an XJDF Document.
	 * @param xjdf The XJDF Document to be processed.
	 * @return The modified XJDF Document.
	 */
	byte[] processXJdf(byte[] xjdf) throws JAXBException, Exception;

}
