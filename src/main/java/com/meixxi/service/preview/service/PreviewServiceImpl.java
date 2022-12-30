package com.meixxi.service.preview.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PreviewServiceImpl implements PreviewService {

    private static final Logger log = LoggerFactory.getLogger(PreviewServiceImpl.class);

    @Override
    public byte[] generatePreview(byte[] pdf, float resX, float resY) throws IOException {
        byte[] result;

        try (PDDocument document = Loader.loadPDF(pdf)) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(0, resX);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", bos);
            bos.flush();

            result = bos.toByteArray();

            bos.close();
        }

        // return preview
        return result;
    }


    public byte[] generatePreviewImageMagick(byte[] pdf, int resX, int resY) throws IOException, InterruptedException {
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

        byte[] result = new FileInputStream(pathPng.toFile()).readAllBytes();

        // clean up
        Files.delete(dir);

        // return preview
        return result;
    }
}
