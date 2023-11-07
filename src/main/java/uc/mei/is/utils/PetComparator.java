package uc.mei.is.utils;

import java.util.Comparator;

import uc.mei.is.models.Pet;

public class PetComparator implements Comparator<Pet>{

    @Override
    public int compare(Pet pet1, Pet pet2) {
        if(pet1.getBirthDate().compareTo(pet2.getBirthDate()) > 0)
            return 1;
        return -1;
    }
    
}
