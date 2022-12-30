package com.meixxi.service.preview.service;

import org.cip4.lib.xjdf.XJdfDocument;
import org.cip4.lib.xjdf.XJmfMessage;
import org.cip4.lib.xjdf.ZipPackage;
import org.cip4.lib.xjdf.schema.*;
import org.cip4.lib.xjdf.type.URI;
import org.cip4.lib.xjdf.type.XYPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class XJDFServiceImpl implements XJDFService {

    private static final Logger log = LoggerFactory.getLogger(XJDFServiceImpl.class);

    @Autowired
    private PreviewService previewService;

    @Override
    public byte[] processXJdfPackage(byte[] pkg) throws Exception {

        ZipPackage zipPackage = new ZipPackage(pkg);
        XJmfMessage xJmfMessage = zipPackage.getXJmfRoot();

        for(Message message: xJmfMessage.getMessages()) {
            if(message instanceof CommandSubmitQueueEntry) {
                CommandSubmitQueueEntry commandSubmitQueueEntry = (CommandSubmitQueueEntry) message;

                // load embedded XJDF Document
                URI xJdfUri = commandSubmitQueueEntry.getQueueSubmissionParams().getURL();
                XJdfDocument xJdfDocument = zipPackage.getXJdfDocument(xJdfUri);

                // extract parameters from the XJDF Document
                String jobId = xJdfDocument.getXJdf().getJobID();
                String[] types = xJdfDocument.getXJdf().getTypes().toArray(new String[]{});
                String queueEntryId = UUID.randomUUID().toString().substring(0, 8);

                RunList runList = xJdfDocument.getSpecificResourceByPart(RunList.class);
                URI uriArtwork = runList.getFileSpec().getURL();
                byte[] artwork = zipPackage.getFile(uriArtwork);

                PreviewGenerationParams previewGenerationParams = xJdfDocument.getSpecificResourceByPart(PreviewGenerationParams.class);
                int resX = Math.round(previewGenerationParams.getResolution().getX());
                int resY = Math.round(previewGenerationParams.getResolution().getY());

                // process preview generation
                log.info("Generate Preview of job '{}'...", jobId);

                byte[] preview = previewService.generatePreview(artwork, resX, resY);
                URI uriPreview = new URI("preview.png");

                // create the XJDF response document
                XJdfDocument xJdfResponse = new XJdfDocument(jobId, types);
                xJdfResponse.addResourceSet(
                        new Preview()
                            .withPreviewFileType(Preview.PreviewFileType.PNG)
                            .withFileSpec(new FileSpec().withURL(uriPreview)),
                        ResourceSet.Usage.OUTPUT
                );
                URI uriXJdfResponse = new URI("response.xjdf");
                System.out.println(new String(xJdfResponse.toXml()));

                // create ReturnQueueEntry XJMF Command.
                XJmfMessage xjmfReturnQueueEntry = new XJmfMessage();
                xjmfReturnQueueEntry.addMessage(
                        new CommandReturnQueueEntry().withReturnQueueEntryParams(
                                new ReturnQueueEntryParams()
                                        .withURL(uriXJdfResponse)
                                        .withQueueEntryID(queueEntryId)
                        )
                );

                log.info("Generation Preview of job '{}' completed.", jobId);

                // create and return zip package
                return new ZipPackage.Builder()
                        .withXJmfRoot(xjmfReturnQueueEntry)
                        .withXJdfDocument(uriXJdfResponse, xJdfResponse)
                        .withFile(uriPreview, preview)
                        .build()
                        .packageFiles();
            }
        }

        return null;
    }

    @Override
    public byte[] generateSubmitQueueEntryTestPackage() throws Exception {

        // load artwork
        byte[] artwork = XJDFServiceImpl.class
                .getResourceAsStream("/com/meixxi/service/preview/service/tudublin.pdf")
                .readAllBytes();
        URI artworkUri = new URI("artwork/tudublin.pdf");

        // create XJDF Document
        String jobId = UUID.randomUUID().toString().substring(0, 6);

        XJdfDocument xJdfDocument = new XJdfDocument(jobId, "PreviewGeneration");
        xJdfDocument.addResourceSet(
                new RunList().withFileSpec(new FileSpec().withURL(artworkUri)),
                ResourceSet.Usage.INPUT
        );
        xJdfDocument.addResourceSet(
                new PreviewGenerationParams().withResolution(new XYPair(72f, 72f)),
                ResourceSet.Usage.INPUT
        );
        URI xJdfDocumentUri = new URI(jobId + ".xjdf");

        // create CommandSubmitQueueEntry XJMF Message
        XJmfMessage xJmfMessage = new XJmfMessage();
        xJmfMessage.addMessage(
                new CommandSubmitQueueEntry().withQueueSubmissionParams(new QueueSubmissionParams().withURL(xJdfDocumentUri))
        );

        // create and return zip package
        return new ZipPackage.Builder()
                .withXJmfRoot(xJmfMessage)
                .withXJdfDocument(xJdfDocumentUri, xJdfDocument)
                .withFile(artworkUri, artwork)
                .build()
                .packageFiles();
    }
}
