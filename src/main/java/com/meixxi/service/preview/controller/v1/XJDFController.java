package com.meixxi.service.preview.controller.v1;

import com.meixxi.service.preview.service.XJDFService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller providing REST endpoints for the XJDF processing.
 */
@RestController
@RequestMapping("/v1/xjdf")
public class XJDFController {

	private static final Logger log = LoggerFactory.getLogger(XJDFController.class);

	@Autowired
	private XJDFService xjdfService;

	@RequestMapping(value = "/", method = RequestMethod.POST, produces = "application/vnd.cip4-xjmf+zip")
	public byte[] processXJdf(@RequestBody byte[] bytes) throws Exception {
		log.info("A new XJDF Package has been received. Size: " + bytes.length);

		return xjdfService.processXJdfPackage(bytes);
	}

}
