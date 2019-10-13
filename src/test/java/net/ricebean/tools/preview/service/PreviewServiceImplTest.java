package net.ricebean.tools.preview.service;

import com.google.common.io.ByteStreams;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class PreviewServiceImplTest {

	private static final String RES_ROOT = "/net/ricebean/tools/preview/service/";

	@InjectMocks
	private PreviewServiceImpl previewService;


//	@Test
//	public void processXJdf() throws Exception {
//
//		// arrange
//		byte[] xjdf = ByteStreams.toByteArray(
//			PreviewServiceImplTest.class.getResourceAsStream(RES_ROOT + "preview-request.xjdf")
//		);
//
//		Path pdf = Paths.get(PreviewServiceImplTest.class.getResource(RES_ROOT + "file-1.pdf").toURI());
//		xjdf = new String(xjdf).replace("file-1.pdf", pdf.toAbsolutePath().toString()).getBytes();
//
//
//		// act
//		byte[] result = previewService.processXJdf(xjdf);
//
//		// assert
//		System.out.println(new String(result));
//	}

	@Test
	public void generatePreview() throws Exception {

		// arrange
		byte[] pdf = ByteStreams.toByteArray(
			PreviewServiceImplTest.class.getResourceAsStream(RES_ROOT + "file-1.pdf")
		);

		// act
		byte[] png = ReflectionTestUtils.invokeMethod(previewService, "generatePreview", pdf, 300, 300);

		// assert
		assertEquals("Size is wrong.", 830697, png.length);
	}
}