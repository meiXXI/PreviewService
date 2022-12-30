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
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD;

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
		byte[] artwork = XJDFServiceImplTest.class.getResourceAsStream("/com/meixxi/service/preview/service/file-1.pdf").readAllBytes();

		Mockito.doReturn(preview).when(previewServiceMock).generatePreview(artwork, 72, 72);

		// act
		byte[] result = xjdfService.processXJdfPackage(pgk);

		// assert
		Mockito.verify(previewServiceMock, Mockito.only()).generatePreview(artwork, 72, 72);

		System.out.println(result.length);
		assertTrue(result.length > 1000, "Result is 300,000 Bytes");

		Map<String, byte[]> files = extractPackage(result);
		assertEquals(3, files.size(), "Number of files is wrong.");

		assertTrue(files.containsKey("preview.png"), "Preview File is missing");
		assertTrue(files.containsKey("root.xjmf"), "XJMF File is missing");
		assertTrue(files.containsKey("response.xjmf"), "XJDF Document is missing");
	}

	@Test
	public void buildXJmfReturnQueueEntry() throws Exception {

		// arrange
		final String expected = "389a8b35c9dbc3d1205a34b3259a303fe44179cc0889d0a5ab0d0e43d0914c7a";

		// act
		byte[] result = ReflectionTestUtils.invokeMethod(xjdfService, "buildXJmfReturnQueueEntry", "myUrl");

		// assert
		System.out.println(new String(result));
//		String actual = new String(result).replaceAll("20([0-9]{2}.){6}", "xxxx");

		// assertEquals(expected, result,"XJMF is wrong.");
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