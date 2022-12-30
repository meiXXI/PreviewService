package com.meixxi.service.preview.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.springframework.test.util.AssertionErrors.assertTrue;

@ExtendWith(MockitoExtension.class)
public class PreviewServiceImplTest {

	private static final String RES_ROOT = "/com/meixxi/service/preview/service/";

	@InjectMocks
	private PreviewServiceImpl previewService;

	@Test
	public void generatePreview() throws Exception {

		// arrange
		byte[] pdf = PreviewServiceImplTest.class.getResourceAsStream(RES_ROOT + "tudublin.pdf").readAllBytes();

		// act
		byte[] png = ReflectionTestUtils.invokeMethod(previewService, "generatePreview", pdf, 300f, 300f);

		// assert
		assertTrue("Size is wrong.", 750000 < png.length);

		Path path = Files.createTempFile("preview-", ".png");
		System.out.println(path);
		Files.write(path, png);
	}
}