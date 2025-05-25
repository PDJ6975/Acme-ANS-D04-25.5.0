
package acme.components;

import java.util.Date;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExchangeRate {

	// Attributes -------------------------------------------------------------

	public String				base;
	public Date					date;
	public Map<String, Double>	rates;
	public boolean				success;
	public long					timestamp;

}
