package net.ricebean.tools.preview.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller providing REST endpoints for the business logic.
 */
@RestController
@RequestMapping("/preview")
public class PreviewController {

	private static final Logger log = LoggerFactory.getLogger(PreviewController.class);

	@GetMapping("")
	public String process() {
		return "haut...";
	}

	@PostMapping("")
	public String process(
		@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

		log.info("yeapp.,bast");

		return "basst";
	}
}
