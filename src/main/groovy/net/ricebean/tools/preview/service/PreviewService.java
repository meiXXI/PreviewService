package net.ricebean.tools.preview.service;

import java.io.IOException;

/**
 * Interface abstracting the preview generation logic.
 */
public interface PreviewService {

	/**
	 * Generate a preview from a PDF.
	 * @param pdf The PDF file as byte array.
	 * @param res The resolution of the preview image.
	 * @return The preview image as byte array.
	 */
	default byte[] generatePreview(byte[] pdf, int res) throws IOException, InterruptedException {
		return generatePreview(pdf, res, res);
	}

	/**
	 * Generate a preview from a PDF.
	 * @param pdf The PDF file as byte array.
	 * @param resX The horizontal resolution of the preview image.
	 * @param resY The vertical resolution of the preview image.
	 * @return The preview image as byte array.
	 */
	byte[] generatePreview(byte[] pdf, int resX, int resY) throws IOException, InterruptedException;

}
