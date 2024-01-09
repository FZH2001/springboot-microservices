package us.userservice.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.userservice.model.entity.Agent;
import us.userservice.model.entity.Beneficiaire;
import us.userservice.model.entity.Client;
import us.userservice.repository.AgentRepository;
import us.userservice.repository.BeneficiaireRepository;
import us.userservice.repository.ClientRepository;
import us.userservice.user.User;
import us.userservice.user.UserRestAPI;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Data
@AllArgsConstructor
public class ClientService {

    // teste du code
    private final ClientRepository clientRepository;
    private final BeneficiaireRepository beneficiaireRepository;
    private final AgentRepository agentRepository;
    private final UserRestAPI userRestAPI;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    public Client saveBeneficiaireClient(Client c,Long benId){
        Beneficiaire b = beneficiaireRepository.findById(benId).orElse(null);
        c.setBeneficiaire(b);
        assert b != null;
        b.setWalletClient(c);
        return clientRepository.saveAndFlush(c);
    }


    public Client getClientData(Long id){
        return clientRepository.findById(id).orElse(null);
    }


    public Client updateClient(Client c){
        User user=new User(c.getPrenom(),c.getTitle(),c.getEmail(),c.getPassword());
        userRestAPI.createUser(user);
        return clientRepository.saveAndFlush(c);
    }


    public List<Beneficiaire> getBeneficiairesByClient(Long clientId){
        return beneficiaireRepository.findAllByClient_Id(clientId);
    }

    public Beneficiaire createBeneficiaire(Beneficiaire b,Long clientId){
        b.setClient(clientRepository.findById(clientId).orElse(null));
        beneficiaireRepository.saveAndFlush(b);
        return b;
    }

    public List<Beneficiaire> searchBeneficiaireByTerm(String term){
        return beneficiaireRepository.searchBeneficiaireByTerm(term);
    }
    public Boolean isBeneficiaireBlacklisted(Long id) {
        return beneficiaireRepository.isBeneficiaireBlockListed(id);
    }
    public void updateClientSolde(Long clientId, Double newSolde) {
        Client client = clientRepository.findById(clientId).orElse(null);
        if (client != null) {
            client.setSolde(newSolde);
            clientRepository.save(client);
        } else {
            throw new EntityNotFoundException("Client not found with ID: " + clientId);
        }
    }
    public void saveOrUpdateClient(Client client) {
        User user=new User(client.getPrenom(),client.getTitle(),client.getEmail(),client.getPassword());
        userRestAPI.createUser(user);
        clientRepository.save(client);
    }


    public String loadData(){
        try {
            Agent a1 = new Agent(null,"Atlas","Abdelghafour","atlas@gmail.com","12345678","0600225588",5000.00);
            Agent a2 = new Agent(null,"Bekkari","Aissam","bekkari@gmail.com","12345678","0633336666",5555.00);
            User user=new User(a1.getPrenom(),a1.getNom(),a1.getEmail(),a1.getPassword());
            userRestAPI.createUser(user);
            User user1=new User(a1.getPrenom(),a1.getNom(),a1.getEmail(),a1.getPassword());
            userRestAPI.createUser(user1);
            agentRepository.saveAllAndFlush(List.of(a1,a2));
            Client c1 = Client.builder()
                    .gsm("0634348550")
                    .dateNaissance(sdf.parse("24-10-2001"))
                    .email("hamzaelgarai10@gmail.com")
                    .adresseLegale("ADRAR 2 MHAMID MARRAKECH")
                    .paysAdresse("Marrakech, Maroc")
                    .typePieceIdentite("CIN")
                    .prenom("Hamza")
                    .title("ELGARAI")
                    .password("12345678")
                    .paysNationalite("Marocain")
                    .ville("Marrakech")
                    .expirationPieceIdentite(sdf.parse("01-01-2026"))
                    .profession("Etudiant")
                    .paysEmissionPieceIdentite("Maroc")
                    .numeroPieceIdentite("EE930320")
                    .build();
            Client c2 = Client.builder()
                    .title("EL OMRANI")
                    .prenom("Hanae")
                    .gsm("0620359862")
                    .email("hanaeelomrani@gmail.com")
                    .password("12345678")
                    .dateNaissance(sdf.parse("29-03-2001"))
                    .adresseLegale("Marrakech")
                    .paysAdresse("Marrakech, Maroc")
                    .typePieceIdentite("CIN")
                    .paysNationalite("Marocain")
                    .ville("Marrakech")
                    .expirationPieceIdentite(sdf.parse("01-01-2030"))
                    .profession("Etudiante")
                    .paysEmissionPieceIdentite("Maroc")
                    .numeroPieceIdentite("OO35930")
                    .build();
            Client c3 = Client.builder()
                    .title("HAMDANI")
                    .prenom("Mohamed")
                    .gsm("0732016830")
                    .email("hamdanimee@gmail.com")
                    .password("12345678")
                    .dateNaissance(sdf.parse("15-08-2001"))
                    .adresseLegale("Anza, Agadir, Marrakech")
                    .paysAdresse("Marrakech, Maroc")
                    .typePieceIdentite("CIN")
                    .paysNationalite("Marocain")
                    .ville("Agadir")
                    .expirationPieceIdentite(sdf.parse("01-01-2030"))
                    .profession("Etudiant")
                    .paysEmissionPieceIdentite("Maroc")
                    .numeroPieceIdentite("GG35692")
                    .build();

            User user3=new User(c1.getPrenom(),c1.getTitle(),c1.getEmail(),c1.getPassword());
            userRestAPI.createUser(user3);
            User user4=new User(c2.getPrenom(),c2.getTitle(),c2.getEmail(),c2.getPassword());
            userRestAPI.createUser(user4);
            User user5=new User(c3.getPrenom(),c3.getTitle(),c3.getEmail(),c3.getPassword());
            userRestAPI.createUser(user5);

            clientRepository.saveAll(List.of(c1,c2,c3));
            clientRepository.flush();
            Client client = clientRepository.findByPrenom("Hamza").orElse(null);
            Beneficiaire b1 = new Beneficiaire(null,"ELGARAI","Karim","karimeg@yahoo.fr","0632579630",false,"hxxx23",client,null);
            Beneficiaire b2 = new Beneficiaire(null,"IRAOUI","Ahmed","ahmediraoui@gmail..com","0735903682",false,"xx34rr",client,null);
            beneficiaireRepository.saveAllAndFlush(List.of(b1,b2));


        }catch (Exception e){
            return e.getMessage();
        }
        return "SUCCESS";
    }

    public Client findClientByCIN(String cin){

        Optional<Client> client=clientRepository.findByNumeroPieceIdentite(cin);
        return client.orElse(null);

    }
    public Client getClientByEmail(String email){
        return clientRepository.findClientByEmail(email).orElse(null);
    }

}
