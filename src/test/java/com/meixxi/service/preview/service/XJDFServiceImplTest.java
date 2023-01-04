package com.meixxi.service.preview.service;

import org.cip4.lib.xjdf.xml.XJdfConstants;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD;
import static org.mockito.ArgumentMatchers.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


@Execution(SAME_THREAD)
@ExtendWith(MockitoExtension.class)
public class XJDFServiceImplTest {

	@Mock
	private PreviewService previewServiceMock;

	@InjectMocks
	private XJDFServiceImpl xjdfService;

	@BeforeAll
	static void initTests() {
		XJdfConstants.AGENT_NAME = "PreviewService";
		XJdfConstants.AGENT_VERSION = "1.2.34";
		XJdfConstants.DEVICE_ID = "PREVIEW_SERVICE";
	}

	@Test
	public void processXJdfPackage() throws Exception {

		// arrange
		byte[] preview = "preview".getBytes();
		byte[] pgk = XJDFServiceImplTest.class.getResourceAsStream("/com/meixxi/service/preview/service/preview.xjmf.zip").readAllBytes();

		Mockito.doReturn(preview).when(previewServiceMock).generatePreview(any(byte[].class), eq(72f), eq(72f));

		// act
		byte[] result = xjdfService.processXJdfPackage(pgk);

		// assert
		Mockito.verify(previewServiceMock, Mockito.only()).generatePreview(any(byte[].class), eq(72f), eq(72f));

		System.out.println(result.length);
		assertTrue(result.length > 1000, "Result is 300,000 Bytes");

		Map<String, byte[]> files = extractPackage(result);
		assertEquals(3, files.size(), "Number of files is wrong.");

		assertTrue(files.containsKey("preview.png"), "Preview File is missing");
		assertTrue(files.containsKey("root.xjmf"), "XJMF File is missing");
		assertTrue(files.containsKey("response.xjdf"), "XJDF Document is missing");
	}

	@Test
	public void generateSubmitQueueEntryTestPackage() throws Exception {

		// arrange

		// act
		byte[] result = xjdfService.generateSubmitQueueEntryTestPackage();

		// assert
		Map<String, byte[]> files = extractPackage(result);
		assertEquals(3, files.size(), "Number of files is wrong.");

		assertTrue(files.containsKey("artwork/tudublin.pdf"), "Preview File is missing");
		assertTrue(files.containsKey("root.xjmf"), "XJMF File is missing");
	}

	/**
	 * Helper method to extract a zip package.
	 */
	private Map<String, byte[]> extractPackage(byte[] pkg) throws IOException {
        byte[] buffer = new byte[1024];

        // unzip package
        ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(pkg));
        ZipEntry zipEntry = zis.getNextEntry();

        Map<String, byte[]> files = new HashMap<>();

        while (zipEntry != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            int len;
            while ((len = zis.read(buffer)) > 0) {
                bos.write(buffer, 0, len);
            }
            bos.close();
            zis.closeEntry();

            files.put(zipEntry.getName(), bos.toByteArray());
            zipEntry = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();

        return files;
	}
}