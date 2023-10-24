package uc.mei.is.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import uc.mei.is.models.Owner;
import uc.mei.is.models.Pet;

@RestController
@RequestMapping("owners")
public class OwnerController {

    private Map<String, Owner> owners;

    @GetMapping
    //devolve todos os donos disponíveis na base de dados
    private Flux<Owner> getAllOwners(){
        List<Pet> pets1 = new ArrayList<>(List.of(new Pet(1, 7.3, "rob", "some", LocalDate.now()), new Pet(2, 7.3, "john", "some", LocalDate.now())));
        List<Pet> pets2 = new ArrayList<>(List.of(new Pet(3, 7.3, "rob", "some", LocalDate.now()), new Pet(4, 7.3, "john", "some", LocalDate.now())));
        if(owners == null){
            owners = new HashMap<>();
            owners.put("1", new Owner(1, 914851551, "Ricardo", pets1));
            owners.put("2", new Owner(2, 910539510, "Maria", pets2));
        }
        return Flux.fromIterable(owners.values());
    }

    @GetMapping("/{id}")
    private Mono<Owner> getOwnerByID(@PathVariable String id){
        return Mono.just(owners.get(id));
    }

    @DeleteMapping("/{id}")
    private boolean delOwnerByID(@PathVariable String id){
        return (owners.remove(id) == null ? false : true);
    }

    @PutMapping()
    private boolean createOwner(@RequestBody Owner owner){
        if(owners.get((String.valueOf(owner.getId()))) != null)
        return false;

        owners.put(String.valueOf(owner.getId()), owner);
        return true;
    }

    @PatchMapping("/{id}")
    private boolean editOwnerByID(@PathVariable String id, @RequestBody Owner owner){
        Owner ownerChoose = owners.get(String.valueOf(id));
        if(ownerChoose == null) return false;
        if(owner.getName() != null)
            ownerChoose.setName(owner.getName());
        if(owner.getPhoneNumber() != 0)
            ownerChoose.setPhoneNumber(owner.getPhoneNumber());
        if(owner.getPets() != null)
            ownerChoose.setPets(((owner.getPets())));

        owners.put(String.valueOf(id), ownerChoose);

        return true;
    }
    
}
