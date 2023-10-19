package uc.mei.is.controllers;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.cglib.core.Local;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uc.mei.is.models.Pet;

@RestController
@RequestMapping("pets")
public class PetController {

    private Map<String, Pet> pets;
    
    @GetMapping
    private Flux<Pet> getAllPets(){
        if(pets == null){
            pets = new HashMap<>();
            pets.put("1", new Pet(1, 7.3, "rob", "some", LocalDate.now()));
            pets.put("2", new Pet(2, 4.6, "john", "other", LocalDate.now()));
        }
        return Flux.fromIterable(pets.values());
    }

    @GetMapping("/{id}")
    private Mono<Pet> getPetById(@PathVariable String id){
        return Mono.just(pets.get(id));
    }

    @DeleteMapping("/{id}")
    private boolean delPetById(@PathVariable String id){
        pets.remove(id);
        return true;
    }

    @PutMapping
    private boolean createPet(@RequestBody Pet pet){
        if(pets.get((String.valueOf(pet.getId()))) != null)
            return false;

        pets.put(String.valueOf(pet.getId()), pet);

        return true;
    }

    @PatchMapping("/{id}")
    private boolean getPetById(@PathVariable String id, @RequestBody Pet pet){
        Pet petChoose = pets.get(String.valueOf(id));
        if(petChoose == null) return false;
        if(pet.getName() != null)
            petChoose.setName(pet.getName());
        if(pet.getWeight() != 0)
            petChoose.setWeight((pet.getWeight()));
        if(pet.getBirthDate() != null)
            petChoose.setBirthDate((pet.getBirthDate()));
        if(pet.getSpecies() != null)
            petChoose.setSpecies(pet.getSpecies());

        pets.put(String.valueOf(id), petChoose);
        return true;
    }
}
