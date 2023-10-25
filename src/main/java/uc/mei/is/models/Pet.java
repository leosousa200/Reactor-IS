package uc.mei.is.models;

import java.time.LocalDate;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pet {
    private int id;

    private double weight;

    private String name;
    private String species;

    private LocalDate birthDate;
}
