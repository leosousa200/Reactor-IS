package uc.mei.is;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.List;

import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;
import uc.mei.is.models.Owner;
import uc.mei.is.models.Pet;


public class Client {

    //função para calcular média
    public static double mean(List<Double> weights) {
        double weightSum = 0.0;
        for (double weight : weights)
            weightSum += weight;
        return weightSum / weights.size();
    }

    //função para calcular desvio padrão
    public static double standardLeviation(List<Double> weights) {
        double stdLeviation = 0.0;
        double mean = mean(weights);

        for (double num : weights)
            stdLeviation += Math.pow(num - mean, 2);


        return Math.sqrt(stdLeviation / weights.size());
    }

    public final static int MAXTRY = 3;

    public static void main(String[] args) throws InterruptedException {

        //thread para realizar os pedidos do WebClient e tratar dos fluxes
        new Thread(() -> {
            //WebClient para pedir e receber lista de owners
            Flux<Owner> ownerFlux = WebClient
                    .create("http://localhost:8080/owners")
                    .get()
                    .uri("")
                    .retrieve()
                    .bodyToFlux(Owner.class);

            //WebClient para pedir e receber lista de pets
            Flux<Pet> petsFlux = WebClient
                    .create("http://localhost:8080/pets")
                    .get()
                    .uri("")
                    .retrieve()
                    .bodyToFlux(Pet.class);


            //WebClient para testar reconexão em caso de falha
            Flux<Pet> fluxError = WebClient
                    .create("http://localhost:8080/noexists")
                    .get()
                    .uri("")
                    .retrieve()
                    .bodyToFlux(Pet.class)
                    .retryWhen(Retry.fixedDelay(MAXTRY, Duration.ofSeconds(2))); // tenta reconectar 3 vezes, esperando 2 segundos


            //limpar ficheiro
            try {
                FileWriter fw = new FileWriter("resp1.txt", false);
                fw.write("");
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Names	and	telephones of all Owners.
            ownerFlux.subscribe(owner -> {
                try {
                    FileWriter fw = new FileWriter("resp1.txt", true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter pw = new PrintWriter(bw);
                    pw.println(owner.getName() + " " + owner.getPhoneNumber());
                    pw.flush();
                    pw.close();
                    bw.close();
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            //Total	number of pets.
            petsFlux
                    .map(pet -> 1)
                    .reduce(Integer::sum)
                    .subscribe(i -> {
                        try {
                            FileWriter fw = new FileWriter("resp2.txt", false);
                            fw.write(String.valueOf(i));
                            fw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });


            //Total	number of dogs. (endpoint novo)
            petsFlux
                    .filter(pet -> pet.getSpecies().equalsIgnoreCase("dog"))
                    .map(i -> 1)
                    .reduce(Integer::sum)
                    .subscribe(i -> {
                        try {
                            FileWriter fw = new FileWriter("resp3.txt", false);
                            fw.write(String.valueOf(i));
                            fw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

            //Sort	the	list	of	animals	weighting	more	than	10	Kg	in	ascending order	of	animal
            //weight

            //limpar ficheiro
            try {
                FileWriter fw = new FileWriter("resp4.txt", false);
                fw.write("");
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            petsFlux
                    .filter(pet -> pet.getWeight() > 10.0)
                    .sort((pet1, pet2) -> (pet1.getWeight() > pet2.getWeight() ? 0 : -1))
                    .subscribe(
                            pet -> {
                                try {
                                    FileWriter fw = new FileWriter("resp4.txt", true);
                                    fw.write(pet.getName() + " " + pet.getWeight() + '\n');
                                    fw.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                    );


            // Average and	standard deviations	of animal weights.
            petsFlux.map(Pet::getWeight)
                    .collectList()
                    .subscribe(list -> {
                        try {
                            FileWriter fw = new FileWriter("resp5.txt", false);
                            fw.write(String.valueOf(mean(list)) + '\n');
                            fw.write(String.valueOf(standardLeviation(list)));
                            fw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });


            // The name of the eldest Pet.
            petsFlux
                    .sort((pet1, pet2) -> (pet2.getBirthDate().compareTo(pet1.getBirthDate())))
                    .last()
                    .subscribe(eldest -> {
                        try {
                            FileWriter fw = new FileWriter("resp6.txt", false);
                            fw.write(eldest.getName());
                            fw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });


            //Average	number	of	Pets per	Owner,	considering	only	owners	with	more	than	one
            //animal.
            ownerFlux
                    .filter(owner -> owner.getPets().size() > 1)
                    .map(owner -> owner.getPets().size())
                    .collectList()
                    .subscribe(list -> {
                        int nrPets = 0;
                        for (int pets : list)
                            nrPets += pets;
                        double petsMean = (double) nrPets / list.size();
                        try {
                            FileWriter fw = new FileWriter("resp7.txt", false);
                            fw.write(String.format("%.2f", petsMean));
                            fw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });


            try { //to clear the file
                FileWriter fw = new FileWriter("resp8.txt", false);
                fw.write("");
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Name	 of	 Owner	 and	 number	 of	 respective	 Pets,	 sorted	 by	 this	 number	 in
            // descending	 order. In	 this	 and	 other	 exercises,	 students	 should	 minimize	 the
            // blocking	points	for	their	applications,	e.g.,	by	means	of	a	block() call
            ownerFlux
                    .sort((o1, o2) ->
                            (Integer.compare(o2.getPets().size(), o1.getPets().size()))
                    )
                    .subscribe(owner -> {
                        try (FileWriter fw = new FileWriter("resp8.txt", true)) {
                            BufferedWriter bw = new BufferedWriter(fw);
                            PrintWriter pw = new PrintWriter(bw);
                            pw.println(owner.getName() + " " + owner.getPets().size());
                            pw.flush();
                            pw.close();
                            bw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    });


            //limpar ficheiro
            try {
                FileWriter fw = new FileWriter("resp9.txt", false);
                fw.write("");
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // The	 same	 as before but	 now	 with	 the	 names	 of	 all	 pets	instead	 of	 simply	 the
            // number. Note	 that	most	 of	 the	work	 should	 occur	 on	 the	 client,	as	mentioned
            // before,	on	the	limitations	imposed	on	the	server.
            ownerFlux
                    .sort((o1, o2) ->
                            (Integer.compare(o2.getPets().size(), o1.getPets().size()))
                    )
                    .subscribe(owner -> {
                        try (FileWriter fw = new FileWriter("resp9.txt", true)) {
                            BufferedWriter bw = new BufferedWriter(fw);
                            PrintWriter pw = new PrintWriter(bw);
                            pw.print(owner.getName() + ":");
                            for (Pet pet : owner.getPets()) {
                                pw.print(" " + pet.getName());
                            }
                            pw.println();
                            pw.flush();
                            pw.close();
                            bw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    });

            //espera todos os processamentos dos Fluxs
            petsFlux.blockLast();
            ownerFlux.blockLast();

            System.out.println("Fim de querys!");
            try {
                fluxError.blockLast();
            } catch (Exception e) {
                System.err.println("Sem sucesso de conexão depois de " + MAXTRY + " tentativas!");
            }
        }).start();
        System.out.println("Fim de processamento do main!");
    }
}