package us.userservice.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import us.userservice.model.entity.Agent;
import us.userservice.model.entity.Beneficiaire;
import us.userservice.model.entity.Client;
import us.userservice.repository.AgentRepository;
import us.userservice.repository.BeneficiaireRepository;
import us.userservice.repository.ClientRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Data
@AllArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final BeneficiaireRepository beneficiaireRepository;
    private final AgentRepository agentRepository;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");


    public Client getClientData(Long id){
        return clientRepository.findById(id).orElse(null);
    }


    public Client updateClient(Client c){
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



    public String loadData(){
        try {
            Agent a1 = new Agent(null,"Atlas","Abdelghafour","atlas@gmail.com","0600225588");
            Agent a2 = new Agent(null,"Bekkari","Aissam","bekkari@gmail.com","0633336666");
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
            clientRepository.saveAll(List.of(c1,c2,c3));
            clientRepository.flush();
            Client client = clientRepository.findByPrenom("Hamza").orElse(null);
            Beneficiaire b1 = new Beneficiaire(null,"ELGARAI","Karim","karimeg@yahoo.fr","0632579630",false,client);
            Beneficiaire b2 = new Beneficiaire(null,"IRAOUI","Ahmed","ahmediraoui@gmail..com","0735903682",false,client);
            beneficiaireRepository.saveAllAndFlush(List.of(b1,b2));


        }catch (Exception e){
            return e.getMessage();
        }
        return "SUCCESS";
    }


}
