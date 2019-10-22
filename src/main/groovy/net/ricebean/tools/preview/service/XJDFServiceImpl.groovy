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

	@Autowired
	private AboutService aboutService;

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
        log.info("Generate Preview...")
        byte[] artwork = files.get(artworkUrl)
        byte[] preview = previewService.generatePreview(artwork, resX, resY)

        String previewUrl = "preview.png"
        files.put(previewUrl, preview)

        // build response
        byte[] xjdfBytes = buildResponseXJDF(files.get(xjdfUrl), previewUrl)
        files.put(xjdfUrl, xjdfBytes)

        // build package
		byte[] respPgk = "".getBytes()

		// return package
        return respPgk
    }

    /**
     * Create the response XJDF including the preview.
     * @param bytesXJdf The origin XJDF Document.
     * @param previewUrl The preview url.
     * @return The updated XJDF Document.
     */
    byte[] buildResponseXJDF(byte[] bytesXJdf, String previewUrl) {

        // parsee XJDF
        XJDF xjdf = new XJdfParser().parseStream(new ByteArrayInputStream(bytesXJdf))

        // create audit pool
        ResourceSet previewSet = new ResourceSet()
				.withName("Preview")
				.withUsage(ResourceSet.Usage.OUTPUT)
                .withResource(new Resource()
                        .withSpecificResource(new ObjectFactory().createPreview(new Preview()
								.withPreviewFileType(Preview.PreviewFileType.PNG)
                                .withFileSpec(new FileSpec()
                                        .withURL(new URI(new java.net.URI(previewUrl)))
                                ))
                        )
                )

        AuditResource auditResource = new AuditResource()
                .withHeader(new Header()
                        .withDeviceID(aboutService.getAppName())
						.withAgentName(aboutService.appName)
						.withAgentVersion(aboutService.getVersion())
                        .withTime(new DateTime()))
                .withResourceInfo(
                        new ResourceInfo()
                                .withResourceSet(previewSet)
                );

        AuditPool auditPool = xjdf.getAuditPool()
        auditPool.getAudits().add(auditResource)

        // return response XJDF as byte array
        return new XJdfParser().parseXJdf(xjdf, true)
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
}
