package net.meixxi.service.preview.controller;

import net.meixxi.service.preview.service.AboutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for system related REST requests.
 */
@RestController
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

		public String getRevAbbrev() {
			return aboutService.getCommitIdAbbrev();
		}

		public String getRevFull() { return aboutService.getCommitId(); }

		public String getReleaseTime() {
			return aboutService.getReleaseTime();
		}

		public String getBranch() { return aboutService.getBranch(); }
	}
}
