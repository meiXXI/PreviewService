package net.ricebean.tools.preview.service;

import net.ricebean.tools.preview.util.XjdfUtil;
import org.apache.commons.io.FileUtils;
import org.cip4.lib.xjdf.schema.*;
import org.cip4.lib.xjdf.type.DateTime;
import org.cip4.lib.xjdf.type.URI;
import org.cip4.lib.xjdf.xml.XJdfParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class XJDFServiceImpl implements XJDFService {

	private static final Logger log = LoggerFactory.getLogger(XJDFServiceImpl.class);

	@Autowired
	private PreviewService previewService;


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
		byte[] bytesPreview = previewService.generatePreview(pdfArtwork, resX, resY);

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
}
