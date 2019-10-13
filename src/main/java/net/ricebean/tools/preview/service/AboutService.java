package net.ricebean.tools.preview.service;

/**
 * Service interface providing details about the current applicatoin.
 */
public interface AboutService {

	/**
	 * Returns details about ImageMagick.
	 * @return The ImageMagick details.
	 */
	String getIMDetails();

	/**
	 * Returns the version of the application.
	 * @return The applications version.
	 */
	String getVersion();

	/**
	 * Returns the name of the application.
	 * @return The applications name.
	 */
	String getAppName();

	/**
	 * Retruns the build time of the application.
	 * @return The applications build time.
	 */
	String getBuildTime();

	/**
	 * Returns the full commit id of the latest change.
	 * @return The latest full commit id.
	 */
	String getCommitId();

	/**
	 * Returns the shortend commit id of the latest change.
	 * @return The latest shortend commit id.
	 */
	String getCommitIdAbbrev();

}