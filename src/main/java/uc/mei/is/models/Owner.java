package uc.mei.is.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "owners")
public class Owner {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private int ID;

        private long phoneNumber;

        private String name;

        @OneToMany(mappedBy="owner", fetch = FetchType.EAGER)
        @JsonManagedReference
        private List<Pet> pets;
}
