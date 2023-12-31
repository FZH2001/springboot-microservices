package us.userservice.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import us.userservice.model.entity.Client;
import us.userservice.repository.AgentRepository;
import us.userservice.repository.ClientRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Data
@AllArgsConstructor
public class AgentService {
    private final AgentRepository agentRepository;
    private final ClientRepository clientRepository;

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    public void saveOrUpdateClient(Client client) {
        clientRepository.save(client);
    }
    public void updateClient(Client client, Long clientId) {
        // Vérifie si le client existe
        if (clientRepository.existsById(clientId)) {
            // Met à jour le client
            client.setId(clientId);
            clientRepository.save(client);
        } else {
            // Le client n'existe pas, vous pouvez gérer cela en levant une exception, par exemple
            throw new EntityNotFoundException("Client not found with ID: " + clientId);
        }
    }
}
