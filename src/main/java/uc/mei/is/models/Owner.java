package uc.mei.is.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Owner {
        private int id;

        private long phoneNumber;

        private String name;

        private List<Pet> pets;
    }
