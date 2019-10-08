package net.ricebean.tools.preview.service;

import net.ricebean.tools.preview.util.XjdfUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.cip4.lib.xjdf.schema.*;
import org.cip4.lib.xjdf.type.DateTime;
import org.cip4.lib.xjdf.type.URI;
import org.cip4.lib.xjdf.xml.XJdfParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBElement;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class PreviewServiceImpl implements PreviewService {

	private static final Logger log = LoggerFactory.getLogger(PreviewServiceImpl.class);

	@Override
	public byte[] processXJdf(byte[] bytes) throws Exception {
		XJDF xjdf = new XJdfParser().parseStream(new ByteArrayInputStream(bytes));

		// get run list
		ResourceSet runListSet = XjdfUtil.getResourceSet(xjdf, "RunList");
		RunList runList = (RunList) runListSet.getResource().get(0).getSpecificResource().getValue();
		Path artworkPath = Paths.get(runList.getFileSpec().getURL().getSourceUri().getRawPath());
		byte[] pdfArtwork = FileUtils.readFileToByteArray(artworkPath.toFile());

		// get preview generation params
		ResourceSet previewParams = XjdfUtil.getResourceSet(xjdf, "PreviewGenerationParams");
		PreviewGenerationParams params = (PreviewGenerationParams) previewParams.getResource().get(0).getSpecificResource().getValue();
		int resX = (int) params.getResolution().getX();
		int resY = (int) params.getResolution().getY();

		// render PDF
		byte[] bytesPreview = renderPdf(pdfArtwork, resX, resY);

		// create tmp file
		Path pathPreview = Files.createTempFile("preview", ".pdf");
		Files.write(pathPreview, bytesPreview);

		// extend audit pool
		ResourceSet previewSet = new ResourceSet()
			.withResource(new Resource()
				.withSpecificResource(new ObjectFactory().createRunList(new RunList()
					.withFileSpec(new FileSpec()
						.withURL(new URI(pathPreview.toUri()))
				))
			)
		);


		AuditResource auditResource = new AuditResource()
			.withHeader(new Header()
				.withDeviceID("MyDevice")
				.withTime(new DateTime()))
			.withResourceInfo(
				new ResourceInfo()
				.withResourceSet( previewSet)
			);

		AuditPool auditPool = xjdf.getAuditPool();
		auditPool.getAudits().add(auditResource);

		return new XJdfParser().parseXJdf(xjdf, true);
	}

	/**
	 * Render an PDF using the ImageMagick command line interface.
	 *
	 * @param bytesPdf The PDF to be rendered as byte array.
	 * @return
	 */
	private byte[] renderPdf(byte[] bytesPdf, int resX, int resY) throws IOException, InterruptedException {
		Path dir = Files.createTempDirectory("preview-generation");

		Path pathPdf = dir.resolve("artwork.pdf");
		Path pathPng = dir.resolve("preview.png");

		Files.write(pathPdf, bytesPdf);

		ProcessBuilder
			pb =
			new ProcessBuilder("convert", "-density", resX + "x" + resY, "-alpha", "off", "-quality", "50", pathPdf.toString() + "[0]", pathPng.toString());

		Process process = pb.start();
		process.waitFor();
		int response = process.waitFor();
		log.info("Response code imagemagick thumbnail geneation: " + response);

		byte[] result = IOUtils.toByteArray(new FileInputStream(pathPng.toFile()));

		// clean up
		FileUtils.deleteDirectory(dir.toFile());

		// return thumb
		return result;
	}
}
