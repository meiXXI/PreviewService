package net.meixxi.service.preview.service;

/**
 * Interface abstracting the XJDF business logic.
 */
public interface XJDFService {

	/**
	 * Process an XJDF Package.
	 * @param xjdf The XJDF Document to be processed.
	 * @return The modified XJDF Document.
	 */
	byte[] processXJdfPackage(byte[] xjdf) throws Exception;

}
