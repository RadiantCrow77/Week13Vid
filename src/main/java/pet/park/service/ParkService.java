package pet.park.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.park.controller.model.ContributorData;
import pet.park.controller.model.PetParkData;
import pet.park.dao.AmenityDao;
import pet.park.dao.ContributorDao;
import pet.park.dao.PetParkDao;
import pet.park.entity.Amenity;
import pet.park.entity.Contributor;
import pet.park.entity.PetPark;

@Service
public class ParkService {

	@Autowired
	private ContributorDao contributorDao;

	@Autowired
	private AmenityDao amenityDao;
	
	@Autowired
	private PetParkDao petParkDao;
	
	@Transactional(readOnly = false)
	public ContributorData saveContributor(ContributorData contributorData) {
		Long contributorId = contributorData.getContributorId();
		Contributor contributor = findOrCreateContributor(contributorId, contributorData.getContributorEmail());
		
		setFieldsInContributor(contributor, contributorData);
		return new ContributorData(contributorDao.save(contributor));
		// convert contributor obj to contributor data by making constructor
	}

	private void setFieldsInContributor(Contributor contributor, ContributorData contributorData) { // destination, src
	contributor.setContributorEmail(contributorData.getContributorEmail());
	contributor.setContributorName(contributorData.getContributorName());
	}

	private Contributor findOrCreateContributor(Long contributorId, String contributorEmail) {
		Contributor contributor;
		
		if (Objects.isNull(contributorId)) {
			// see if contributor with a certain email exists
			// custom method to do that
			// method exists in Contributor Dao, returns a contributor if it finds one
Optional<Contributor> opContrib= contributorDao.findByContributorEmail(contributorEmail);

// check for duplicate contributor
if(opContrib.isPresent()) {
	throw new DuplicateKeyException("Contributor with emaiL: "+contributorEmail+" already exists.");
}
			contributor = new Contributor();
		} else {
			contributor = findContributorById(contributorId);
		}
		return contributor;
	}

	private Contributor findContributorById(Long contributorId) {

		return contributorDao.findById(contributorId).orElseThrow(
				() -> new NoSuchElementException("Contributor with Id = " + contributorId + " was not found. "));
	}
	//wk 14 code begins here: 
	@Transactional(readOnly = true)
	public List<ContributorData> retrieveAllContributors() {
		// 1st way to get contributor data
		List<Contributor> contributors = contributorDao.findAll();
		// turn contributor objects into data
		List<ContributorData> response = new LinkedList<>();
		
		// return list of contributor entities into data
		for(Contributor contributor: contributors) {
			response.add(new ContributorData(contributor));
		}
		return response;
		
		// 2nd way to get contributor data using streams
//		// @formatter:off
//		return contributorDao.findAll()
//			.stream() // .stream turns list to a strea
//			.map(ContributorData::new)
//			.toList(); 
//		// @formatter:on
		
		
	}

	@Transactional(readOnly = true)
	public ContributorData retrieveContributorById(Long contributorId) {
	Contributor contributor = findContributorById(contributorId);
	return new ContributorData(contributor);
	}

	@Transactional(readOnly = false)
	public void deleteContributorById(Long contributorId) {
	Contributor contributor = findContributorById(contributorId); // call findbyId to get contributor, invalid id throws an exception
	contributorDao.delete(contributor);
	}

	@Transactional(readOnly = false)
	public PetParkData savePetPark(Long contributorId, PetParkData petParkData) {
	// find contributor using the ID
		Contributor contributor = findContributorById(contributorId);
		
		Set<Amenity> amenities = amenityDao.findAllByAmenityIn(petParkData.getAmenities()); // sending instructions to Spring JPA
		
		PetPark petPark = findOrCreatePetPark(petParkData.getPetParkId());
		
		// set fields for the data obj that gets passed in
		setPetParkFields(petPark, petParkData); // (target, source)
		
		// establish relationships
		petPark.setContributor(contributor);
		contributor.getPetParks().add(petPark);
		
		for(Amenity amenity : amenities) {
			amenity.getPetParks().add(petPark);
			petPark.getAmenities().add(amenity);
	}
		
		PetPark dbPetPark = petParkDao.save(petPark);
		return new PetParkData(dbPetPark);
	}

	private void setPetParkFields(PetPark petPark, PetParkData petParkData) {
		// setting petpark fields, note: Contributor + Controller are differently  set
		petPark.setCountry(petParkData.getCountry());
		petPark.setDirections(petParkData.getDirections());
		petPark.setGeoLocation(petParkData.getGeoLocation());
		petPark.setParkName(petParkData.getParkName());
		petPark.setPetParkId(petParkData.getPetParkId());
		petPark.setStateOrProvince(petParkData.getStateOrProvince());
	}

	private PetPark findOrCreatePetPark(Long petParkId) {
		PetPark petPark;
		
		if(Objects.isNull(petParkId)) { // if no petparkid
			petPark = new PetPark();
		}else {
			petPark = findPetParkById(petParkId);
		}
		return petPark;
	}

	private PetPark findPetParkById(Long petParkId) {
		return petParkDao.findById(petParkId).orElseThrow(() -> new NoSuchElementException("Pet park with ID = "+petParkId+" does not exist."));
	}
	


}
