
package acme.entities.noticeBoards;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class NoticeBoard extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString
	@Automapped
	private String				title;

	@Mandatory
	@ValidString
	@Automapped
	private String				headline;

	@Mandatory
	@ValidUrl
	@Automapped
	private String				url;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				isPaid;

	@Optional
	@ValidMoney
	@Automapped
	private Money				price;

	@Optional
	@ValidString(max = 10)
	@Automapped
	private String				currency;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				instructorName;

	@Optional
	@ValidUrl
	@Automapped
	private String				instructorUrl;

	@Optional
	@ValidUrl
	@Automapped
	private String				instructorImage;

	@Optional
	@ValidUrl
	@Automapped
	private String				imageUrl;

	@Optional
	@ValidString(max = 50)
	@Automapped
	private String				language;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				postedDate;
}
