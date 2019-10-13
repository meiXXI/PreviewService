package net.ricebean.tools.preview;

import net.ricebean.tools.preview.controller.PreviewController;
import net.ricebean.tools.preview.service.AboutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	@Autowired
	private AboutService aboutService;

	/**
	 * Applications main entrance point.
	 * @param args The command line arguments.
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/**
	 * Event is called after applications start up.
	 */
	@EventListener(ApplicationReadyEvent.class)
	public void onStartUp() {
		log.warn(String.format("%s %s has started. (rev: %s)", aboutService.getAppName(), aboutService.getVersion(), aboutService.getCommitIdAbbrev()));
		log.info("Rendering Engine: " + aboutService.getIMDetails());
	}

	/**
	 * Read git properties.
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		PropertySourcesPlaceholderConfigurer propsConfig = new PropertySourcesPlaceholderConfigurer();
		propsConfig.setLocation(new ClassPathResource("git.properties"));
		propsConfig.setIgnoreResourceNotFound(true);
		propsConfig.setIgnoreUnresolvablePlaceholders(true);
		return propsConfig;
	}

}
