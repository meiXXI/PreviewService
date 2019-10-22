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
	 * Returns the builds commit time of the application.
	 * @return The applications build time.
	 */
	String getReleaseTime();

	/**
	 * Returns the number of total commits.
	 * @return The number of total commits.
	 */
	int getCommitCount();

	/**
	 * Returns the branch name.
	 * @return The branches name.
	 */
	String getBranch();

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