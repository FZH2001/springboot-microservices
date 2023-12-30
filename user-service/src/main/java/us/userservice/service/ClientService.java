package us.userservice.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import us.userservice.repository.ClientRepository;

@Service
@Data
@AllArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

}
