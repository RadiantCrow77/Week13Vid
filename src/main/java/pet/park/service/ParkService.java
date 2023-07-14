package pet.park.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.park.controller.model.ContributorData;
import pet.park.dao.ContributorDao;
import pet.park.entity.Contributor;

@Service
public class ParkService {

	@Autowired
	private ContributorDao contributorDao;

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

	public ContributorData retrieveContributorById(Long contributorId) {
	Contributor contributor = findContributorById(contributorId);
	return new ContributorData(contributor);
	}
	


}
