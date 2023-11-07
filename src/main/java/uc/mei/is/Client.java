package uc.mei.is;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.List;

import org.springframework.web.reactive.function.client.WebClient;

import io.netty.handler.codec.http.HttpResponseStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;
import reactor.util.retry.RetrySpec;
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

    public static Flux<Owner> errorHandle(Throwable t){
        System.out.println("Entrei aqui!");
        int retryCount = 3;
        Flux<Owner> owners = null;
        for(int i=0 ; i < retryCount; i++){
            System.out.println("Tentativa: " + i);
            try{
            owners = WebClient
            .create("http://localhost:8080/ownersFailure")
            .get()
            .uri("")
            .retrieve()
            .bodyToFlux(Owner.class);
            return owners;
            }catch(Exception e){

            }
        }
        System.out.println("Fim tentativa");
        return null;
    }

    public final static int MAXTRY = 3;

    public static void main(String[] args) throws InterruptedException {

        Scheduler s = Schedulers.newParallel("parallel-scheduler-mine", 8);
        
        //fail tolerance to test
        Flux<Owner> ownerTry = WebClient
         .create("http://localhost:8080/ownersFailure")
         .get()
         .uri("")
         .retrieve()
         .bodyToFlux(Owner.class)
         .retryWhen(Retry.fixedDelay(MAXTRY, Duration.ofSeconds(5)))
         .onErrorResume(Client::errorHandle)
         .subscribeOn(s);

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


        Mono<String> petsNumber = WebClient
         .create("http://localhost:8080/pets/count")
         .get()
         .uri("")
         .retrieve().bodyToMono(String.class)
         .subscribeOn(s);

         Mono<String> dogsNumber = WebClient
         .create("http://localhost:8080/pets/count?specie=dog")
         .get()
         .uri("")
         .retrieve().bodyToMono(String.class)
         .subscribeOn(s);

         Mono<String> petsEldest = WebClient
         .create("http://localhost:8080/pets/eldest")
         .get()
         .uri("")
         .retrieve().bodyToMono(String.class)
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
        
        //WAY I (with full flux of pets)
        /*petsFlux.collectList().subscribe(
            i -> {
                try {
                FileWriter fw = new FileWriter("resp2.txt", false);
                fw.write(String.valueOf(i.size()));
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            }
        );*/

        //WAY II (with specific endpoint)
        petsNumber
            .subscribe(str -> {
                try {
                    FileWriter fw = new FileWriter("resp2.txt", false);
                    fw.write(str);
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        });












        
        //Total	number of dogs. (endpoint novo)
        //WAY I (with full flux of pets)
        /*petsFlux.filter(i -> i.getSpecies().equalsIgnoreCase("dog")).collectList().subscribe(
            i -> {
                try {
                FileWriter fw = new FileWriter("resp3.txt", false);
                fw.write(String.valueOf(i.size()));
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            }
        );*/


        //WAY II (with specific endpoint)
                dogsNumber
                    .subscribe(str -> {
                        try {
                            FileWriter fw = new FileWriter("resp3.txt", false);
                            fw.write(str);
                            fw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                });











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
        //WAY I (with full flux of pets)
        /*petsFlux
            .sort((a,b) -> {return (a.getBirthDate().compareTo(b.getBirthDate()));})
            .last().subscribe(i -> {
             try {
                FileWriter fw = new FileWriter("resp6.txt", false);
                fw.write(i.getName());
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });*/

        //WAY II (with specific endpoint)
                petsEldest
                    .subscribe(str -> {
                        try {
                            FileWriter fw = new FileWriter("resp6.txt", false);
                            fw.write(str);
                            fw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                });

















        //Average	number	of	Pets per	Owner,	considering	only	owners	with	more	than	one	
        //animal.
        ownerFlux
            .filter(owner -> owner.getPets().size() > 1)
            .subscribe();


        // Name	 of	 Owner	 and	 number	 of	 respective	 Pets,	 sorted	 by	 this	 number	 in	
        // descending	 order. In	 this	 and	 other	 exercises,	 students	 should	 minimize	 the	
        // blocking	points	for	their	applications,	e.g.,	by	means	of	a	block() call
        ownerFlux
            .sort((o1, o2) -> {
                return (Integer.compare(o1.getPets().size(),o2.getPets().size()));
            }).subscribe(owner -> {
                try (FileWriter fw = new FileWriter("resp8.txt", true)) {
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter pw = new PrintWriter(bw);
                    pw.println(owner.getName() + " " + owner.getPets().size());
                    pw.flush();
                    pw.close();
                    bw.close();
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });


        // The	 same	 as before but	 now	 with	 the	 names	 of	 all	 pets	instead	 of	 simply	 the	
        // number. Note	 that	most	 of	 the	work	 should	 occur	 on	 the	 client,	as	mentioned	
        // before,	on	the	limitations	imposed	on	the	server.
        ownerFlux
            .sort((o1, o2) -> {
                return (Integer.compare(o1.getPets().size(),o2.getPets().size()));
            }).subscribe(owner -> {
                try (FileWriter fw = new FileWriter("resp9.txt", true)) {
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter pw = new PrintWriter(bw);
                    pw.print(owner.getName() + ":");
                    for(Pet pet : owner.getPets()){
                        pw.print(" " +  pet.getName());
                    }
                    pw.println();
                    pw.flush();
                    pw.close();
                    bw.close();
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });

        //

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
