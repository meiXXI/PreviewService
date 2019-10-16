package net.ricebean.tools.preview.service

import net.ricebean.tools.preview.util.XjdfUtil
import org.apache.commons.io.FileUtils
import org.cip4.lib.xjdf.schema.*
import org.cip4.lib.xjdf.type.DateTime
import org.cip4.lib.xjdf.type.URI
import org.cip4.lib.xjdf.xml.XJdfParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.xml.bind.JAXBException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

@Component
class XJDFServiceImpl implements XJDFService {

	private static final Logger log = LoggerFactory.getLogger(XJDFServiceImpl.class)

	@Autowired
	private PreviewService previewService

	@Override
	byte[] processXJdfPackage(byte[] pkg) throws IOException, JAXBException {

		// extract files
		log.info("Extract XJDF Package...")
		Map<String, byte[]> files = extractPackage(pkg)

		// analyze XJMF
		log.info("Read root.xjmf...")
		def xjmf = new XmlSlurper().parse(new ByteArrayInputStream(files.get("root.xjmf")))
		String xjdfUrl = xjmf.CommandSubmitQueueEntry.QueueSubmissionParams.@URL.toString()

		// read XJDF
		log.info("Read " + xjdfUrl + "...")
		def xjdf = new XmlSlurper().parse(new ByteArrayInputStream(files.get(xjdfUrl)))
		String artworkUrl = xjdf.ResourceSet.Resource.RunList.FileSpec.@URL.toString()

		String strResolution = xjdf.ResourceSet.Resource.PreviewGenerationParams.@Resolution.toString()
		int resX = Integer.parseInt(strResolution.split(" ")[0])
		int resY = Integer.parseInt(strResolution.split(" ")[1])

		// process artwork
		byte[] artwork = files.get(artworkUrl)
		byte[] preview = previewService.generatePreview(artwork, resX, resY)


		return new byte[0];
	}

	/**
	 * Extract XJDF Package.
	 * @param pkg The package to be extracted as byte array.
	 * @return Map of files.
	 */
	private static Map<String, byte[]> extractPackage(byte[] pkg) throws IOException {
		byte[] buffer = new byte[1024]

		// unzip package
		ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(pkg))
		ZipEntry zipEntry = zis.getNextEntry()

		Map<String, byte[]> files = new HashMap<>()

		while (zipEntry != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream()

			int len;
			while ((len = zis.read(buffer)) > 0) {
				bos.write(buffer, 0, len)
			}
			bos.close()
			zis.closeEntry()

			files.put(zipEntry.getName(), bos.toByteArray())
			zipEntry = zis.getNextEntry()
		}

		zis.closeEntry()
		zis.close()

		return files
	}


	byte[] processXJdfOld(byte[] bytes) throws Exception {
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
