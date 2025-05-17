
package acme.validation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acme.spam.config.SpamConfig;
import com.acme.spam.config.SpamTermRepository;
import com.acme.spam.detection.SpamDetector;

@Configuration
public class SpamDetectionConfig {

	@Bean
	public SpamConfig spamConfig() {
		return SpamConfig.load();
	}

	@Bean
	public SpamTermRepository spamTermRepository(final SpamConfig cfg) {
		return new SpamTermRepository(cfg);
	}

	@Bean
	public SpamDetector spamDetector(final SpamTermRepository repo, final SpamConfig cfg) {
		return new SpamDetector(repo, cfg.getThreshold());
	}
}
