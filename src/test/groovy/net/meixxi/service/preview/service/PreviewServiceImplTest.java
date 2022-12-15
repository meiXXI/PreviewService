package net.meixxi.service.preview.service;

import com.google.common.io.ByteStreams;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.springframework.test.util.AssertionErrors.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class PreviewServiceImplTest {

	private static final String RES_ROOT = "/net/ricebean/tools/preview/service/";

	@InjectMocks
	private PreviewServiceImpl previewService;

	@Test
	public void generatePreview() throws Exception {

		// arrange
		byte[] pdf = ByteStreams.toByteArray(
			PreviewServiceImplTest.class.getResourceAsStream(RES_ROOT + "file-1.pdf")
		);

		// act
		byte[] png = ReflectionTestUtils.invokeMethod(previewService, "generatePreview", pdf, 300, 300);

		// assert
		assertTrue("Size is wrong.", 750000 < png.length);
	}
}