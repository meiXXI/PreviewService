package net.ricebean.tools.preview.util;

import org.cip4.lib.xjdf.schema.ResourceSet;
import org.cip4.lib.xjdf.schema.XJDF;

public class XjdfUtil {

	/**
	 * Getting a specific ResourceSet object.
	 *
	 * @param name The name of the resource set.
	 * @return The specific ResourceSet object.
	 */
	public static ResourceSet getResourceSet(XJDF xjdf, String name) {
		ResourceSet resourceSet = null;

		for (int i = 0; i < xjdf.getResourceSet().size() && resourceSet == null; i++) {
			ResourceSet rs = xjdf.getResourceSet().get(i);

			if (name.equalsIgnoreCase(rs.getName())) {
				resourceSet = rs;
			}
		}

		return resourceSet;
	}

}
