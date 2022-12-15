package net.meixxi.service.preview.service

import org.cip4.lib.xjdf.schema.*
import org.cip4.lib.xjdf.type.DateTime
import org.cip4.lib.xjdf.type.IntegerList
import org.cip4.lib.xjdf.type.URI
import org.cip4.lib.xjdf.xml.XJdfParser
import org.cip4.lib.xjdf.xml.XJmfParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.xml.bind.JAXBElement
import javax.xml.bind.JAXBException
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

@Component
class XJDFServiceImpl implements XJDFService {

    private static final String ROOT_XJMF = "root.xjmf";

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
        def xjmf = new XmlSlurper().parse(new ByteArrayInputStream(files.get(ROOT_XJMF)))
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

        // prepare response
        byte[] xjdfBytes = buildResponseXJDF(files.get(xjdfUrl), previewUrl)
        files.put(xjdfUrl, xjdfBytes)

        files.put(ROOT_XJMF, buildReturnQE(xjdfUrl))

        // build and return package
        return packageFiles(files)
    }

    /**
     * Package list of files in ZIP Archive.
     * @param files The list (map) of files.
     * @return Zip archive as byte array.
     */
    private byte[] packageFiles(Map<String, byte[]> files) {
        byte[] buffer = new byte[1024]

        ByteArrayOutputStream bos = new ByteArrayOutputStream()
        ZipOutputStream zos = new ZipOutputStream(bos)

        for (String key : files.keySet()) {
            ZipEntry entry = new ZipEntry(key)
            zos.putNextEntry(entry)

            ByteArrayInputStream isFile = new ByteArrayInputStream(files.get(key))
            int len

            while ((len = isFile.read(buffer)) > 0) {
                zos.write(buffer, 0, len)
            }

            isFile.close()
        }

        zos.close()
        bos.close()

        return bos.toByteArray()
    }

    /**
     * Build the ReturnQueueEntry command.
     * @param xjdfUrl The reference to the xjdf.
     * @return the ReturnQueueEntry command as byte array.
     */
    private byte[] buildReturnQE(String xjdfUrl) {
        JAXBElement<CommandReturnQueueEntry> c = new ObjectFactory().createCommandReturnQueueEntry(new CommandReturnQueueEntry()
                .withHeader(new Header()
                        .withDeviceID(aboutService.getAppName())
                        .withAgentName(aboutService.getAppName())
                        .withAgentVersion(aboutService.getVersion())
                        .withTime(new DateTime()))
                .withReturnQueueEntryParams(new ReturnQueueEntryParams()
                        .withURL(new URI(new java.net.URI(xjdfUrl)))
                        .withQueueEntryID("QE-42")
                ))

        XJMF xjmf = new XJMF()
                .withHeader(new Header()
                        .withDeviceID(aboutService.getAppName())
                        .withAgentName(aboutService.getAppName())
                        .withAgentVersion(aboutService.getVersion())
                        .withTime(new DateTime()))

        xjmf.getMessages().add(c)

        return new XJmfParser().parseXJmf(xjmf)
    }

    /**
     * Create the response XJDF including the preview.
     * @param bytesXJdf The origin XJDF Document.
     * @param previewUrl The preview url.
     * @return The updated XJDF Document.
     */
    private byte[] buildResponseXJDF(byte[] bytesXJdf, String previewUrl) {

        // parsee XJDF
        XJDF xjdf = new XJdfParser().parseStream(new ByteArrayInputStream(bytesXJdf))

        // create audit pool
        ResourceSet previewSet = new ResourceSet()
                .withName("Preview")
                .withUsage(ResourceSet.Usage.OUTPUT)
                .withResource(new Resource()
                        .withPart(new Part().withDocIndex(new IntegerList(0, 0)))
                        .withSpecificResource(new ObjectFactory().createPreview(new Preview()
                                .withPreviewFileType(Preview.PreviewFileType.PNG)
                                .withFileSpec(new FileSpec()
                                        .withURL(new URI(new java.net.URI(previewUrl)))
                                ))
                        )
                )

        AuditCreated auditCreated = new AuditCreated()
                .withHeader(new Header()
                        .withDeviceID(aboutService.getAppName())
                        .withAgentName(aboutService.appName)
                        .withAgentVersion(aboutService.getVersion())
                        .withTime(new DateTime()))

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
        auditPool.getAudits().clear()
        auditPool.getAudits().add(auditCreated)
        auditPool.getAudits().add(auditResource)

        // return response XJDF as byte array
        return new XJdfParser().parseXJdf(xjdf)
    }

    /**
     * Extract XJDF Package.
     * @param pkg The package to be extracted as byte array.
     * @return Map of files.
     */
    private Map<String, byte[]> extractPackage(byte[] pkg) throws IOException {
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
