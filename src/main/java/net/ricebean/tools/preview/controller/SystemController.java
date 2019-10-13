package net.ricebean.tools.preview.controller;

import net.ricebean.tools.preview.service.AboutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for system related REST requests.
 */
@RestController
@RequestMapping("/system")
public class SystemController {

	private static final Logger log = LoggerFactory.getLogger(SystemController.class);

	@Autowired
	private AboutService aboutService;

	/**
	 * Returns the current version details.
	 * @return The current version details
	 */
	@RequestMapping("/version")
	public Version getVersion() {
		return new Version();
	}

	/**
	 * Model class for providing relevant release details.
	 */
	private class Version {

		/**
		 * Default constructor.
		 */
		private Version() {
		}

		public String getVersion() {
			return aboutService.getVersion();
		}

		public String getGit() {
			return aboutService.getCommitIdAbbrev();
		}

		public String getBuildTime() {
			return aboutService.getBuildTime();
		}
	}
}
