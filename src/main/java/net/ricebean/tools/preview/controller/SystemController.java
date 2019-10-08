package net.ricebean.tools.preview.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for system related REST requests.
 */
@RestController
@RequestMapping("/system")
public class SystemController {

	private final static Version VERSION = new Version();

	/**
	 * Returns the current version details.
	 * @return The current version details
	 */
	@RequestMapping("/version")
	public Version getVersion() {
		return VERSION;
	}

	/**
	 * Model class for providing relevant release details.
	 */
	private static class Version {

		private String version;

		/**
		 * Default constructor.
		 */
		private Version() {
			this.version = "MyVersion";
		}

		public String getVersion() {
			return version;
		}
	}
}
