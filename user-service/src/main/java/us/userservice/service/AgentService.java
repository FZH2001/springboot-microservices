package us.userservice.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import us.userservice.model.entity.Agent;
import us.userservice.model.entity.Client;
import us.userservice.repository.AgentRepository;
import us.userservice.repository.BeneficiaireRepository;
import us.userservice.repository.ClientRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Data
@AllArgsConstructor
public class AgentService {
    private final AgentRepository agentRepository;
    private final ClientRepository clientRepository;
    private final BeneficiaireRepository beneficiaireRepository;

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }


    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    public void deleteClients(List<Long> ids){
        clientRepository.deleteAllById(ids);
    }
    public void deleteBeneficiaires(List<Long> ids){
        beneficiaireRepository.deleteAllById(ids);
    }

    public void saveOrUpdateClient(Client client) {
        clientRepository.save(client);
    }
    public void updateClient(Client client, Long clientId) {

        if (clientRepository.existsById(clientId)) {
            client.setId(clientId);
            clientRepository.save(client);
        } else {
            throw new EntityNotFoundException("Client not found with ID: " + clientId);
        }
    }

    public Agent getAgentData(Long id){
        return agentRepository.findById(id).orElse(null);
    }
    public Agent updateData(Agent a){
        return agentRepository.saveAndFlush(a);
    }
    public void updateAgentSolde(Long agentId, Double newSolde) {

        Agent agent = agentRepository.findById(agentId).orElse(null);

        if (agent != null) {
            agent.setSolde(newSolde);
            agentRepository.save(agent);
        } else {
            throw new EntityNotFoundException("Agent not found with ID: " + agentId);
        }
    }
}
