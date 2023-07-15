package pet.park.dao;

import java.util.Set;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import pet.park.entity.Amenity;

public interface AmenityDao extends JpaRepositoryImplementation<Amenity, Long> {

	Set<Amenity> findAllByAmenityIn(Set<String> amenities);

}
