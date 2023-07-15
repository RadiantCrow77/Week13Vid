package pet.park.dao;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import pet.park.entity.PetPark;

public interface PetParkDao extends JpaRepositoryImplementation<PetPark, Long> {

}
