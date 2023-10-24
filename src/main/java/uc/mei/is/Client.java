package uc.mei.is;

import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import uc.mei.is.models.Owner;

public class Client {

    public static void main(String[] args) throws InterruptedException {

        Scheduler s = Schedulers.newParallel("parallel-scheduler-mine", 4);

         WebClient
         .create("http://localhost:8080/owners")
         .get()
         .uri("")
         .retrieve()
         .bodyToFlux(Owner.class)
         .subscribeOn(s)
         .subscribe(i -> System.out.println(i.getName()));

        System.out.println("end client!");
    }
}
