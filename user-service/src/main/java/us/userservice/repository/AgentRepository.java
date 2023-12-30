package us.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import us.userservice.model.entity.Agent;

@Repository
public interface AgentRepository extends JpaRepository<Agent,Long> {
}
