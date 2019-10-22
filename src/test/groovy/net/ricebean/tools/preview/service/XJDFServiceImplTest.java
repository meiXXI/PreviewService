package net.ricebean.tools.preview.service;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class XJDFServiceImplTest {

	@Mock
	private PreviewService previewServiceMock;

	@Mock
	private AboutService aboutServiceMock;

	@InjectMocks
	private XJDFServiceImpl xjdfService;

	@Test
	public void processXJdfPackage() throws Exception {

		// arrange
		byte[] preview = "preview".getBytes();
		byte[] pgk = IOUtils.toByteArray(XJDFServiceImplTest.class.getResourceAsStream("/net/ricebean/tools/preview/service/preview-request.xjmf.zip"));
		byte[] artwork = IOUtils.toByteArray(XJDFServiceImplTest.class.getResourceAsStream("/net/ricebean/tools/preview/service/file-1.pdf"));

		Mockito.doReturn(preview).when(previewServiceMock).generatePreview(artwork, 72, 72);
		Mockito.doReturn("PreviewService").when(aboutServiceMock).getAppName();
		Mockito.doReturn("v1234").when(aboutServiceMock).getVersion();

		// act
		byte[] result = xjdfService.processXJdfPackage(pgk);

		// assert
		Mockito.verify(previewServiceMock, Mockito.only()).generatePreview(artwork, 72, 72);
		Mockito.verify(aboutServiceMock, Mockito.times(2)).getAppName();
		Mockito.verify(aboutServiceMock, Mockito.times(1)).getVersion();

		assertEquals("Result is wrong.", preview, result);
	}
}