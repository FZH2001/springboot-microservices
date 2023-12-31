package us.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.userservice.model.entity.Client;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {
    Optional<Client> findByPrenom(String prenom);

    Optional<Client> findByNumeroPieceIdentite(String cin);

    Optional<Client> findClientByEmail(String email);
}
