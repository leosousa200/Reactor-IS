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
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uc.mei.is.Repository.OwnerRepository;
import uc.mei.is.models.Owner;

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
        if(owner.getName().isEmpty() || owner.getPhoneNumber() == 0) {
            logger.error("Create owner error, it dont have all the parameters needed!");
            return false;
        }
        ownerRepository.createOwner(owner.getPhoneNumber(), owner.getName());
        logger.info("Created owner invocation.");
        return true;
    }

    @PatchMapping("/{id}")
    private boolean editOwnerByID(@PathVariable int id, @RequestBody Owner owner){
        Optional<Owner> ownerChosen = ownerRepository.findById(id);
        if(!ownerChosen.isPresent()){
            logger.error("Patch owner error, pet ID " +  id + " dont exist!");
            return false;
        }
        Owner ownerOld = ownerChosen.get();

        if(owner.getName() != null)
            ownerOld.setName(owner.getName());
        if(owner.getPhoneNumber() != 0)
            ownerOld.setPhoneNumber((owner.getPhoneNumber()));

        ownerRepository.save(ownerOld);
        logger.info("Patched owner [" + id +"] invocation.");

        return true;
    }


    //comentar para verificar tentativa de conexão do cliente
    @GetMapping("/erro")
    private boolean QueryError(){
        return true;
    }

    
}
