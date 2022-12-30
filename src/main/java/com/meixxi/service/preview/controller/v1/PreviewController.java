package com.meixxi.service.preview.controller.v1;

import com.meixxi.service.preview.service.PreviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

/**
 * Controller providing REST endpoints for the business logic.
 */
@RestController
@RequestMapping("/v1/preview")
public class PreviewController {

	private static final Logger log = LoggerFactory.getLogger(PreviewController.class);

	@Autowired
	private PreviewService previewService;

	@PostMapping(value = "", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public @ResponseBody byte[] process(
		@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException, InterruptedException {

		// generate preview
		log.info("File '" + file.getOriginalFilename() + "' has been received for preview generation. Size: " + file.getSize());
		byte[] preview = previewService.generatePreview(file.getBytes(), 72);
		log.info("Prepare response. Size: " + preview.length);

		// return preview
		return preview;
	}
}
