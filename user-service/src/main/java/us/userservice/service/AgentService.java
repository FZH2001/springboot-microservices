package us.userservice.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import us.userservice.repository.AgentRepository;

@Service
@Data
@AllArgsConstructor
public class AgentService {
    private final AgentRepository agentRepository;
}
