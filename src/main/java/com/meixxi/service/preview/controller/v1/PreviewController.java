package com.meixxi.service.preview.controller.v1;

import com.meixxi.service.preview.service.PreviewService;
import com.meixxi.service.preview.util.DimensionUtil;
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

	@PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public @ResponseBody byte[] process(
		@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException, InterruptedException {
		return process(72, file.getBytes(), file.getOriginalFilename());
	}

	@PostMapping(value = "", consumes = MediaType.APPLICATION_PDF_VALUE, produces = MediaType.IMAGE_PNG_VALUE)
	public @ResponseBody byte[] process(@RequestBody byte[] pdf) throws IOException, InterruptedException {
		return process(72, pdf, null);
	}

	@PostMapping(value = "resolution/{resolution}", consumes = MediaType.APPLICATION_PDF_VALUE, produces = MediaType.IMAGE_PNG_VALUE)
	public @ResponseBody byte[] process(@PathVariable float resolution, @RequestBody byte[] pdf, String filename) throws IOException, InterruptedException {
		long startTime = System.currentTimeMillis();

		log.info("'{}' received ({}). Target: {} dpi.",
				filename == null ? "PDF" : filename,
				DimensionUtil.bytes2readable(pdf.length),
				resolution
		);

		byte[] preview = previewService.generatePreview(pdf, resolution);

		log.info("'{}' preview completed ({}). {} dpi, {} ms.",
				filename == null ? "PDF" : filename,
				DimensionUtil.bytes2readable(preview.length),
				resolution,
				System.currentTimeMillis() - startTime
		);

		return preview;
	}
}
