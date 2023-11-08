package uc.mei.is.controllers;

import java.time.LocalDate;
import java.util.*;

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
import uc.mei.is.PetRepository;
import uc.mei.is.models.Pet;
import uc.mei.is.utils.PetComparator;

@RestController
@RequestMapping("pets")
public class PetController {

    private final PetRepository petRepository;
    private static int PetIDs = 0;
    public PetController(PetRepository petRepository) {
        this.petRepository = petRepository;
      }
    
    
    @GetMapping
    //devolve todos os animais disponíveis na base de dados
    private Flux<Pet> getAllPets(){
        return Flux.fromIterable(petRepository.findAll());
    }

    @GetMapping("/{id}")
    //devolve o animal que contém o ID procurado
    private Mono<Pet> getPetById(@PathVariable String id){
        Optional<Pet> petChosen= petRepository.findById(Integer.valueOf(id));
        if(petChosen != null) return Mono.just( petChosen.get());
        return null;
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

    @DeleteMapping("/{id}")
    //elimina o animal que contém o ID indicado
    private boolean delPetById(@PathVariable String id){
        petRepository.deleteById(Integer.valueOf(id));
        return true;
    }

    @PutMapping
    //cria o animal com as informações inseridas
    private boolean createPet(@RequestBody Pet pet){
        if(pet.getName().isEmpty() || pet.getSpecies().isEmpty() || pet.getWeight() == 0)
            return false;

        petRepository.save(pet);

        return true;
    }

    @PatchMapping("/{id}")
    //edita o animal através do ID com as novas informações inseridas
    private boolean editPetById(@PathVariable String id, @RequestBody Pet pet){
        Pet petChoose = null;
        if(petChoose == null) return false;
        if(pet.getName() != null)
            petChoose.setName(pet.getName());
        if(pet.getWeight() != 0)
            petChoose.setWeight((pet.getWeight()));
        if(pet.getBirthDate() != null)
            petChoose.setBirthDate((pet.getBirthDate()));
        if(pet.getSpecies() != null)
            petChoose.setSpecies(pet.getSpecies());

        //pets.put(String.valueOf(id), petChoose);
        return true;
    }
}
