
package acme.validation;

import com.acme.spam.config.SpamConfig;
import com.acme.spam.config.SpamTermRepository;
import com.acme.spam.detection.SpamDetector;

public class UserInputValidator {

	private final SpamDetector spamDetector;


	public UserInputValidator() {
		var cfg = SpamConfig.load();
		this.spamDetector = new SpamDetector(new SpamTermRepository(cfg), cfg.getThreshold());
	}

	public void validate(final String userInput) {
		if (this.spamDetector.isSpam(userInput))
			throw new IllegalArgumentException("Entrada rechazada: contenido identificado como spam.");
	}
}
