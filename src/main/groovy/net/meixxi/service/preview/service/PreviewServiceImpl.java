package net.meixxi.service.preview.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class PreviewServiceImpl implements PreviewService {

	private static final Logger log = LoggerFactory.getLogger(PreviewServiceImpl.class);

	@Override
	public byte[] generatePreview(byte[] pdf, int resX, int resY) throws IOException, InterruptedException {
		Path dir = Files.createTempDirectory("preview-generation");

		Path pathPdf = dir.resolve("artwork.pdf");
		Path pathPng = dir.resolve("preview.png");

		Files.write(pathPdf, pdf);

		ProcessBuilder pb = new ProcessBuilder("convert", "-density", resX + "x" + resY, "-alpha", "off", "-quality", "50", pathPdf.toString() + "[0]", pathPng.toString());

		log.info("Start preview generation...");
		long startTime = System.currentTimeMillis();

		Process process = pb.start();
		int response = process.waitFor();
		log.info("Preview generation successful (duration: " + (System.currentTimeMillis() - startTime) + " ms). Response Code ImageMagick: " + response);

		byte[] result = IOUtils.toByteArray(new FileInputStream(pathPng.toFile()));

		// clean up
		FileUtils.deleteDirectory(dir.toFile());

		// return preview
		return result;
	}
}
