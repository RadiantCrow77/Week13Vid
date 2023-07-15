package pet.park.controller.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import lombok.Data;
import lombok.NoArgsConstructor;
import pet.park.entity.Amenity;
import pet.park.entity.Contributor;
import pet.park.entity.GeoLocation;
import pet.park.entity.PetPark;

// vvvv creates getters/setters for us and a constructor, but make our own constructor so noarg
@Data
@NoArgsConstructor
public class PetParkData {
// fields grabbed from PetPark.java
	private Long petParkId;
	private String parkName;
	private String directions;
	private String stateOrProvince;
	private String country;
	
	@Embedded
	private GeoLocation geoLocation;
	private PetParkContributor contributor; // this var was added
	private Set<String> amenities = new HashSet<>(); // changed obj to string

// end fields grabbed from PetPark
	
	// constructor takes in data from pet park
	public PetParkData(PetPark petPark) {
		petParkId = petPark.getPetParkId();
		parkName = petPark.getParkName();
		directions = petPark.getDirections();
		stateOrProvince = petPark.getStateOrProvince();
		country = petPark.getCountry();
		geoLocation = petPark.getGeoLocation();
		contributor = new PetParkContributor(petPark.getContributor());
		
		for(Amenity amenity : petPark.getAmenities()) {
			amenities.add(amenity.getAmenity());
		}
	}
	
	@Data
	@NoArgsConstructor
	public static class PetParkContributor {
		private Long contributorId; 
		private String contributorName;
		private String contributorEmail;
		
		// constructor that takes in a contributor
		public PetParkContributor(Contributor contributor) {
			contributorId = contributor.getContributorId();
			contributorName = contributor.getContributorName();
			contributorEmail = contributor.getContributorEmail();
		}
	}
	
}
