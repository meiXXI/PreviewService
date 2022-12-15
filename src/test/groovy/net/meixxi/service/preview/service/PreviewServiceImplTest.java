package net.meixxi.service.preview.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.springframework.test.util.AssertionErrors.assertTrue;

@ExtendWith(MockitoExtension.class)
public class PreviewServiceImplTest {

	private static final String RES_ROOT = "/net/ricebean/tools/preview/service/";

	@InjectMocks
	private PreviewServiceImpl previewService;

	@Test
	public void generatePreview() throws Exception {

		// arrange
		byte[] pdf = PreviewServiceImplTest.class.getResourceAsStream(RES_ROOT + "file-1.pdf").readAllBytes();

		// act
		byte[] png = ReflectionTestUtils.invokeMethod(previewService, "generatePreview", pdf, 300, 300);

		// assert
		assertTrue("Size is wrong.", 750000 < png.length);
	}
}