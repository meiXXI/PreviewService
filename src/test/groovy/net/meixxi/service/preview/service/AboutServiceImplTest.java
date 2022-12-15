package net.meixxi.service.preview.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * JUnit test of the AboutServiceImpl.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = "data.root=/")
public class AboutServiceImplTest {

	@Autowired
	private AboutService aboutService;

	@Test
	public void getIMDetails() {

		// arrange

		// act
		String actual = aboutService.getIMDetails();

		// assert
		assertTrue("ImageMagick version is wrong.", actual.startsWith("ImageMagick "));
	}

	@Test
	public void getVersion() {

		// arrange

		// act
		String actual = aboutService.getVersion();

		// assert
		assertEquals("Version is wrong.", "0.0.1-SNAPSHOT", actual);
	}

	@Test
	public void getAppName() {
	}

	@Test
	public void getBuildTime() {
	}

	@Test
	public void getCommitId() {
	}

	@Test
	public void getCommitIdAbbrev() {
	}
}