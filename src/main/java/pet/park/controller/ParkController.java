package pet.park.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pet.park.controller.model.ContributorData;
import pet.park.service.ParkService;

@RestController // says will return 200 response by default
@RequestMapping("/pet_park") // every method coming into the class must start with pet_park
@Slf4j
public class ParkController {
@Autowired
	private ParkService parkService;
	// CREATE contributor
	@PostMapping("/contributor") // post request to pet_park/contributor
	// controller method
	public ContributorData insertContributor(
			@RequestBody ContributorData contributorData) {
		log.info("Creating contributor {}", contributorData);
		
		return parkService.saveContributor(contributorData);
	}
	
	
	// UPDATE contributor
	@PutMapping("/contributor/{contributorId}") // put for updates, arg is resource Id
	public ContributorData updateContributor(@PathVariable Long contributorId, 
			@RequestBody ContributorData contributorData) { // payload
		
		// set contributor Id each time
		contributorData.setContributorId(contributorId);
		
		log.info("Updating contributor {}", contributorData);
		
		// this method vvv applies to both C and U operations
		return parkService.saveContributor(contributorData);
	}
	
	// READ contributor
	@GetMapping("/contributer")
	public List<ContributorData> retrieveAllContributers(){
		log.info("Retrieve all contributers called");
		return parkService.retrieveAllContributors();
	}
	
	// method retrieves single contributor by id
	@GetMapping("/contributor/{contributorId}") // have to pass in resource id, contributor is a resource, pet.park is application
			public ContributorData retrieveContributorById(@PathVariable Long contributorId) { // PathVariable tells Spring that we expect that var in the URL, and that goes into 
		// contributorId param
				log.info("Retrieving contributor with ID ={}", contributorId);
				return parkService.retrieveContributorById(contributorId);
			}
	
	// DELETE all contributors
	@DeleteMapping("/contributor") // no need to pass an ID as this method deletes ALL contributors
	public void deleteAllContributors() {
			log.info("Attempting to delete all contributors ...");
			throw new UnsupportedOperationException("Deleting all contributors is not allowed.");
			 
	}
	
	// DELETE one contributor , very similar to READ/GET with an ID
	@DeleteMapping("/contributor/{contributorId}")
	public Map<String, String> deleteContributorById(
			@PathVariable Long contributorId) // Jackson converts this msg to JSON
	{
		log.info("Deleting contributor with id = {}", contributorId);
		
		parkService.deleteContributorById(contributorId);
		
		return Map.of("message", "Deletion of contributor with ID = "+contributorId+" was successful.");
		
	}
}
