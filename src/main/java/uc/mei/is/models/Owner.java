package uc.mei.is.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
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

        //@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Pet> pets = new ArrayList<>();
    }
