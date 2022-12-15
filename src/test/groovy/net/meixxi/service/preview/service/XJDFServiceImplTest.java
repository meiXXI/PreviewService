package net.meixxi.service.preview.service;

import com.google.common.hash.Hashing;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;

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
		byte[] pgk = IOUtils.toByteArray(XJDFServiceImplTest.class.getResourceAsStream("/net/meixxi/service/preview/service/preview.xjmf.zip"));
		byte[] artwork = IOUtils.toByteArray(XJDFServiceImplTest.class.getResourceAsStream("/net/meixxi/service/preview/service/file-1.pdf"));

		Mockito.doReturn(preview).when(previewServiceMock).generatePreview(artwork, 72, 72);
		Mockito.doReturn("PreviewService").when(aboutServiceMock).getAppName();
		Mockito.doReturn("v1234").when(aboutServiceMock).getVersion();

		// act
		byte[] result = xjdfService.processXJdfPackage(pgk);

		// assert
		Mockito.verify(previewServiceMock, Mockito.only()).generatePreview(artwork, 72, 72);
		Mockito.verify(aboutServiceMock, Mockito.times(8)).getAppName();
		Mockito.verify(aboutServiceMock, Mockito.times(4)).getVersion();

		System.out.println(result.length);
		assertTrue("Result is 300,000 Bytes", result.length > 300000);

		Map<String, byte[]> files = ReflectionTestUtils.invokeMethod(xjdfService, "extractPackage", result);
		assertEquals("Number of files is wrong.", 4, files.size());

		assertTrue("Preview File is missing", files.containsKey("preview.png"));
		assertTrue("PDF File is missing", files.containsKey("file-1.pdf"));
		assertTrue("XJDF File is missing", files.containsKey("preview.xjdf"));
		assertTrue("XJMF File is missing", files.containsKey("root.xjmf"));
	}

	@Test
	public void buildReturnQE() throws Exception {

		// arrange
		final String expected = "389a8b35c9dbc3d1205a34b3259a303fe44179cc0889d0a5ab0d0e43d0914c7a";

		Mockito.doReturn("PreviewService").when(aboutServiceMock).getAppName();
		Mockito.doReturn("v1234").when(aboutServiceMock).getVersion();

		// act
		byte[] result = ReflectionTestUtils.invokeMethod(xjdfService, "buildReturnQE", "myUrl");

		// assert
		System.out.println(new String(result));
		String actual = new String(result).replaceAll("20([0-9]{2}.){6}", "xxxx");

		assertEquals("XJMF is wrong.", expected, Hashing.sha256().hashString(actual, StandardCharsets.UTF_8).toString());
	}
}