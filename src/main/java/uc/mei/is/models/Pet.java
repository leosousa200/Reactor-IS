package uc.mei.is.models;

import java.time.LocalDate;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pets")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int ID;

    private double weight;

    private String name;
    private String species;

    private LocalDate birthDate;

    @ManyToOne()
    @JoinColumn(name = "ownerId")
    @JsonBackReference
    private Owner owner;

}
