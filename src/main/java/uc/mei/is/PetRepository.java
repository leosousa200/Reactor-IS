package uc.mei.is;

import org.springframework.data.repository.CrudRepository;

import uc.mei.is.models.Pet;

public interface PetRepository extends CrudRepository<Pet, Integer> {}
