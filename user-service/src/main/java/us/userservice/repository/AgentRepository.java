package us.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.userservice.model.entity.Agent;
import us.userservice.model.entity.Client;

import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<Agent,Long> {
    Optional<Agent> findAgentByEmail(String email);

}
