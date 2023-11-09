package uc.mei.is.controllers;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uc.mei.is.Repository.OwnerRepository;
import uc.mei.is.Repository.PetRepository;
import uc.mei.is.models.Owner;
import uc.mei.is.models.Pet;
import uc.mei.is.utils.PetComparator;

@RestController
@RequestMapping("pets")
public class PetController {

    private final PetRepository petRepository;
    private final OwnerRepository ownerRepository;

    private Logger logger;
    public PetController(PetRepository petRepository, OwnerRepository ownerRepository) {
        this.petRepository = petRepository;
        this.ownerRepository = ownerRepository;
        logger = LoggerFactory.getLogger(PetController.class);
      }
    
    
    @GetMapping
    //devolve todos os animais disponíveis na base de dados
    private Flux<Pet> getAllPets(){
        logger.info("Get all pets invocation.");
        return Flux.fromIterable(petRepository.findAll());
    }

    @GetMapping("/{id}")
    //devolve o animal que contém o ID procurado
    private Mono<Pet> getPetById(@PathVariable String id){
        Optional<Pet> petChosen= petRepository.findById(Integer.valueOf(id));
        if(petChosen.isPresent()) {
            Pet pet = petChosen.get();
            logger.info("Get pet [" + pet.getID() +"] invocation.");
            return Mono.just(pet);
        }
        logger.error("Get pet error, ID " +  id + " dont exist!");
        return null;
    }

    @DeleteMapping("/{id}")
    //elimina o animal que contém o ID indicado
    private boolean delPetById(@PathVariable String id){

        Optional<Pet> petChosen= petRepository.findById(Integer.valueOf(id));
        if(!petChosen.isPresent()){
            logger.error("Delete pet error, ID " +  id + " dont exist!");
            return false;
        }
        Pet pet = petChosen.get();
        Owner petOwner = pet.getOwner();

        petOwner.getPets().remove(pet);
        ownerRepository.save(petOwner);
        petRepository.delete(pet);
        logger.info("Delete pet [" + pet.getID() +"] invocation.");

        return true;
    }

    @PutMapping
    //cria o animal com as informações inseridas
    private boolean createPet(@RequestParam(required = true) int ownerId, @RequestBody Pet pet){
        if(pet.getName().isEmpty() || pet.getSpecies().isEmpty() || pet.getWeight() == 0)
            return false;

        Optional<Owner> ownerChosen = ownerRepository.findById(ownerId);
        if(!ownerChosen.isPresent()){
            logger.error("Create pet error, owner ID " +  ownerId + " dont exist!");
            return false;
        }
        Owner owner = ownerChosen.get();

        pet.setOwner(owner);
        petRepository.save(pet);
        logger.info("Created pet invocation.");
        return true;
    }

    @PatchMapping("/{id}")
    //edita o animal através do ID com as novas informações inseridas
    private boolean editPetById(@PathVariable int id, @RequestBody Pet pet){
        Optional<Pet> petChosen = petRepository.findById(id);
        if(!petChosen.isPresent()){
            logger.error("Patch pet error, pet ID " +  id + " dont exist!");
            return false;
        }
        Pet petOld = petChosen.get();

        if(pet.getName() != null)
            petOld.setName(pet.getName());
        if(pet.getWeight() != 0)
            petOld.setWeight((pet.getWeight()));
        if(pet.getBirthDate() != null)
            petOld.setBirthDate((pet.getBirthDate()));
        if(pet.getSpecies() != null)
            petOld.setSpecies(pet.getSpecies());

        petRepository.save(petOld);
        logger.info("Patched pet [" + id +"] invocation.");

        return true;
    }



    @GetMapping("/count")
    //devolve o animal que contém o ID procurado
    private Mono<Long> getNrPets(@RequestParam(required = false) String specie){
        if(specie != null) return Flux.fromIterable(petRepository.findAll()).filter(pet -> pet.getSpecies().equalsIgnoreCase(specie)).count();
        return Mono.just(Long.valueOf(((Collection<?>) petRepository.findAll()).size()));
    }

    @GetMapping("/eldest")
    //devolve o animal que contém o ID procurado
    private Mono<String> getEldestPed(){
        ArrayList<Pet> petsList = new ArrayList<Pet>((Collection<Pet>) petRepository.findAll());
        Collections.sort(petsList, new PetComparator());
        return Mono.just(petsList.get(0).getName());

        /*return Mono           //operação bloqueante
                .just(Flux
                    .fromIterable(pets.values())
                        .sort((a,b) -> {return (a.getBirthDate().compareTo(b.getBirthDate()));})
                        .last().block().getName());*/
    }

}
