package uc.mei.is.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import uc.mei.is.models.Owner;
import uc.mei.is.models.Pet;

@RestController
@RequestMapping("owners")
public class OwnerController {

    private Map<String, Owner> owners;

    @GetMapping
    //devolve todos os donos disponíveis na base de dados
    private Flux<Owner> getAllOwners(){
        if(owners == null){
            owners = new HashMap<>();
            owners.put("1", new Owner(1, 914851551, "Ricardo", new ArrayList<>()));
            owners.put("2", new Owner(2, 910539510, "Maria", new ArrayList<>()));
        }
        return Flux.fromIterable(owners.values());
    }
    
}
