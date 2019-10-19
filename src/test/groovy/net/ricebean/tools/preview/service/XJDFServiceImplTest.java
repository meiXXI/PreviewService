package net.ricebean.tools.preview.service;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class XJDFServiceImplTest {

	@Mock
	private PreviewService previewServiceMock;

	@InjectMocks
	private XJDFServiceImpl xjdfService;

	@Test
	public void processXJdfPackage() throws Exception {

		// arrange
		byte[] bytes = IOUtils.toByteArray(XJDFServiceImplTest.class.getResourceAsStream("/net/ricebean/tools/preview/service/preview-request.xjmf.zip"));

		// act
		byte[] result = xjdfService.processXJdfPackage(bytes);

		// assert
	}
}