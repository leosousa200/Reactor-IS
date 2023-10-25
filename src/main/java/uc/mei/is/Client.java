package uc.mei.is;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import uc.mei.is.models.Owner;
import uc.mei.is.models.Pet;



public class Client {

    public static double mean(List<Double> weights){
        double weightSum = 0.0;
        for(double weight : weights)
            weightSum += weight;
        return weightSum/weights.size();
    }

    public static double standardLeviation(List<Double> weights){
        double stdLeviation = 0.0;
        double mean = mean(weights);

        for(double num : weights)
            stdLeviation += Math.pow(num-mean, 2);
        
        
        return Math.sqrt(stdLeviation/weights.size());
    }


    public static void main(String[] args) throws InterruptedException {

        Scheduler s = Schedulers.newParallel("parallel-scheduler-mine", 4);

        Flux<Owner> ownerFlux = WebClient
         .create("http://localhost:8080/owners")
         .get()
         .uri("")
         .retrieve()
         .bodyToFlux(Owner.class)
         .subscribeOn(s);


        Flux<Pet> petsFlux = WebClient
         .create("http://localhost:8080/pets")
         .get()
         .uri("")
         .retrieve()
         .bodyToFlux(Pet.class)
         .subscribeOn(s);

        //Names	and	telephones of all Owners.
        ownerFlux.subscribe(i -> {
            try {
                FileWriter fw = new FileWriter("resp1.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);
                pw.println(i.getName() + " " + i.getPhoneNumber());
                pw.flush();
                pw.close();
                bw.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        //Total number	of	Pets. (endpoint)
        petsFlux.collectList().subscribe(
            i -> {
                try {
                FileWriter fw = new FileWriter("resp2.txt", false);
                fw.write(String.valueOf(i.size()));
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            }
        );

        //Total	number of dogs. (endpoint novo)
        petsFlux.filter(i -> i.getSpecies().equalsIgnoreCase("dog")).collectList().subscribe(
            i -> {
                try {
                FileWriter fw = new FileWriter("resp3.txt", false);
                fw.write(String.valueOf(i.size()));
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            }
        );

        //Total	number of animals weighting more than 10kg. Sort this list by ascending	
        //order	of animal weight
        petsFlux.filter(i -> i.getWeight() > 10.0).sort((a,b) -> {return (a.getWeight() > b.getWeight() ? 0 : -1);}).collectList().subscribe(
            i -> {
                try {
                FileWriter fw = new FileWriter("resp4.txt", false);
                fw.write(String.valueOf(i.size()) + "\n\n");
                for(Pet pet : i)
                    fw.write(String.valueOf(pet.getWeight()) + '\n');
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            }
        );


        // Average and	standard deviations	of animal weights.
        petsFlux.map(i -> i.getWeight()).collectList().subscribe(i -> {
            try {
                FileWriter fw = new FileWriter("resp5.txt", false);
                fw.write(String.valueOf(mean(i)) + '\n');
                fw.write(String.valueOf(standardLeviation(i)));
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        // The name of the	eldest Pet.
        petsFlux
            .sort((a,b) -> {return (a.getBirthDate().compareTo(b.getBirthDate()));})
            .last().subscribe(i -> {
             try {
                FileWriter fw = new FileWriter("resp6.txt", false);
                fw.write(i.getName());
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}



/*
 * 
 *          WebClient
         .create("http://localhost:8080/owners")
         .get()
         .uri("")
         .retrieve()
         .bodyToFlux(Owner.class)
         .subscribeOn(s)
         .subscribe(i -> {
            System.out.println("Name: " + i.getName());
            System.out.println("Id: " + i.getId());
            System.out.println("Phone Number: " + i.getPhoneNumber());
            for(Pet pet : i.getPets())
                System.out.println("Pet:\nId: " + pet.getId() + "\nName: " + pet.getName() + "\nSpecies: " + pet.getSpecies() + "\nWeight: " + pet.getWeight());
            System.out.println("-----------------------------");

        });

         WebClient
         .create("http://localhost:8080/pets")
         .get()
         .uri("")
         .retrieve()
         .bodyToFlux(Pet.class)
         .subscribeOn(s)
         .subscribe(i -> System.out.println(i.getName()));

        System.out.println("end client!");

 * 
 * 
 * 
 */
