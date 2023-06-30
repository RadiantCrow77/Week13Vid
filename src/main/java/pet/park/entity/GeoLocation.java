package pet.park.entity;

import java.math.BigDecimal;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable

@Data
// lombok annotation, creates getters/setters ^
@NoArgsConstructor
public class GeoLocation {

// instance vars
	private BigDecimal latitude;
	private BigDecimal longitude;
	
	// copy constructor
	public GeoLocation(GeoLocation geoLocation) { // used when creating programming operations
		this.latitude = geoLocation.latitude;
		this.longitude = geoLocation.longitude;
	}
}
