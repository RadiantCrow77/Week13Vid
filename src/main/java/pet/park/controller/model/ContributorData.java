package pet.park.controller.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Value;
import pet.park.entity.Amenity;
import pet.park.entity.Contributor;
import pet.park.entity.GeoLocation;
import pet.park.entity.PetPark;

@NoArgsConstructor
@Data
public class ContributorData {

	private Long contributorId; // when created in java, is in snake case : contributor_id

	private String contributorName;

	@Column(unique = true)
	private String contributorEmail;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(mappedBy = "contributor", cascade = CascadeType.ALL)
	private Set<PetParkResponse> petParks = new HashSet<>();

	public ContributorData(Contributor contributor) {
		contributorId = contributor.getContributorId();
		contributorName = contributor.getContributorName();
		contributorEmail = contributor.getContributorEmail();

		for (PetPark petPark : contributor.getPetParks()) {
			petParks.add(new PetParkResponse(petPark));
		}
	}

	@Data
	@NoArgsConstructor
	static class PetParkResponse {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long petParkId;
		private String parkName;
		private String directions;
		private String stateOrProvince;
		private String country;

		private GeoLocation geoLocation;

		// list of emenities as a set
		@EqualsAndHashCode.Exclude
		@ToString.Exclude
		// many to many
		@ManyToMany(cascade = CascadeType.PERSIST) // PERSIST inst of ALL -- if delete a park, d el the join tbl rows
													// but not amenity rows
		@JoinTable(name = "pet_park_amenity", joinColumns = @JoinColumn(name = "pet_park_id"), inverseJoinColumns = @JoinColumn(name = "amenity_id"))

		private Set<String> amenities = new HashSet<>();

		PetParkResponse(PetPark petPark) {
			petParkId = petPark.getPetParkId();
			parkName = petPark.getParkName();
			directions = petPark.getDirections();
			stateOrProvince = petPark.getStateOrProvince();
			country = petPark.getCountry();
			geoLocation = new GeoLocation(petPark.getGeoLocation());
			
			for(Amenity amenity : petPark.getAmenities()) {
				amenities.add(amenity.getAmenity());
			}
		}

	}
}
