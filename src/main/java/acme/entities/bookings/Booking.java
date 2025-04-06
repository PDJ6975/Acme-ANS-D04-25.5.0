
package acme.entities.bookings;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.entities.flights.Flight;
import acme.realms.Customer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Booking extends AbstractEntity {

	// Serialisation version

	private static final long	serialVersionUID	= 1L;

	// Attributes

	@Mandatory
	@Column(unique = true)
	@ValidString(pattern = "^[A-Z0-9]{6,8}$")
	private String				locatorCode;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				purchaseMoment;

	@Mandatory
	@Automapped
	@Valid
	private TravelClass			travelClass;

	@Mandatory
	@Automapped
	@ValidMoney(min = 0.00, max = 10000.00)
	private Money				price;

	@Optional
	@Automapped
	@ValidString(pattern = "^[0-9]{4}$", min = 4, max = 4)
	private String				creditCardNibble;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	@ManyToOne(optional = false)
	@Automapped
	@Valid
	private Customer			customer;

	@ManyToOne(optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@Automapped
	@Valid
	private Flight				flight;

}
