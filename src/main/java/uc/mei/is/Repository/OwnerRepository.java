package uc.mei.is.Repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uc.mei.is.models.Owner;

public interface OwnerRepository extends CrudRepository<Owner, Integer> {

    @Modifying
    @Query(value = "INSERT INTO owners (phone_number, name) VALUES (:phoneNumber, :name);", nativeQuery = true)
    @Transactional
    void createOwner(Long phoneNumber, String name);

}
