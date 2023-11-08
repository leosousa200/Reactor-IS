package uc.mei.is.controllers;

import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uc.mei.is.OwnerRepository;
import uc.mei.is.models.Owner;
import uc.mei.is.models.Pet;

@RestController
@RequestMapping("owners")
public class OwnerController {

    private OwnerRepository ownerRepository;
    private Logger logger;

    public OwnerController(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
        logger = LoggerFactory.getLogger(OwnerController.class);
    }

    @GetMapping
    //devolve todos os donos disponíveis na base de dados
    private Flux<Owner> getAllOwners(){
        logger.info("Get all owners invocation.");
        return Flux.fromIterable(ownerRepository.findAll());
    }

    @GetMapping("/{id}")
    private Mono<Owner> getOwnerByID(@PathVariable String id){
        Optional<Owner> ownerChosen= ownerRepository.findById(Integer.valueOf(id));
        if(ownerChosen.isPresent()) {
            Owner owner = ownerChosen.get();
            logger.info("Get owner [" + owner.getID() +"] invocation.");
            return Mono.just(owner);
        }
        logger.error("Get owner error, ID " +  id + " dont exist!");
        return null;
    }

    @DeleteMapping("/{id}")
    private boolean delOwnerByID(@PathVariable String id){
        Optional<Owner> ownerChosen= ownerRepository.findById(Integer.valueOf(id));
        if(!ownerChosen.isPresent()){
            logger.error("Delete owner error, ID " +  id + " dont exist!");
            return false;
        }
        Owner owner = ownerChosen.get();
        if(owner.getPets().size() == 0) {
            logger.info("Delete owner [" + owner.getID() +"] invocation.");
            ownerRepository.deleteById(Integer.valueOf(id));
            return true;
        }
        logger.error("Delete owner error, ID " +  id + " have pets associated!");
        return false;
    }

    @PutMapping()
    private boolean createOwner(@RequestBody Owner owner){
        if(owner.getName().isEmpty() || owner.getPhoneNumber() == 0)
            return false;
        owner.setPets(new ArrayList<>());
        ownerRepository.save(owner);

        return true;
    }

    @PatchMapping("/{id}")
    private boolean editOwnerByID(@PathVariable String id, @RequestBody Owner owner){
        /*Owner ownerChoose = owners.get(String.valueOf(id));
        if(ownerChoose == null) return false;
        if(owner.getName() != null)
            ownerChoose.setName(owner.getName());
        if(owner.getPhoneNumber() != 0)
            ownerChoose.setPhoneNumber(owner.getPhoneNumber());
        if(owner.getPets() != null)
            ownerChoose.setPets(((owner.getPets())));

        owners.put(String.valueOf(id), ownerChoose);
*/
        return true;
    }
    
}
