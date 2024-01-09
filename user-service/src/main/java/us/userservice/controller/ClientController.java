package us.userservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.userservice.model.entity.Beneficiaire;
import us.userservice.model.entity.Client;
import us.userservice.service.ClientService;

import java.util.List;

@RestController
@RequestMapping("/api/client")
@CrossOrigin(origins = "http://localhost:4200")

@AllArgsConstructor
public class ClientController {
    private final ClientService clientService;


    @GetMapping("/loadData")
    public String loadData(){
        System.out.println("i am loadData of client controller");
        return clientService.loadData();
    }
    @GetMapping("/get-client-data/{id}")
    public Client getClientData(@PathVariable("id") Long id){
        return clientService.getClientData(id);
    }

    @PutMapping("/update")
    public Client updateClient(@RequestBody Client c){ return clientService.updateClient(c); }

    @GetMapping("/beneficiaires/{id}")
    public List<Beneficiaire> getBeneficiaires(@PathVariable("id") Long clientId){ return clientService.getBeneficiairesByClient(clientId); }
    @PostMapping("/beneficiaire/{clientId}")
    public Beneficiaire createBeneficiaire(@RequestBody Beneficiaire b,@PathVariable Long clientId) { return clientService.createBeneficiaire(b,clientId);}


    @GetMapping("/beneficiaire/search")
    public List<Beneficiaire> searchBeneficiaire(@RequestParam String term){ return clientService.searchBeneficiaireByTerm(term); }

    @GetMapping("/beneficiaire/blocklisted/{id}")
    public Boolean isBeneficiaireBlocklisted(@PathVariable Long id){
        return clientService.isBeneficiaireBlacklisted(id);
    }
    @PutMapping("/update-client-solde/{id}")
    public ResponseEntity<String> updateClientSolde(@PathVariable Long id, @RequestParam Double newSolde) {
        try {
            clientService.updateClientSolde(id, newSolde);
            return new ResponseEntity<>("Client solde updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/add-client")
    public ResponseEntity<String> saveOrUpdateClient(@RequestBody Client client) {
        clientService.saveOrUpdateClient(client);
        return new ResponseEntity<>("Client saved or updated successfully", HttpStatus.CREATED);
    }
    @GetMapping("/get-client/{cin}")
    public Client getClientByCIN(@PathVariable("cin") String cin){
        return clientService.findClientByCIN(cin);
    }

    @GetMapping("/getClientByEmail/{email}")
    public Client getClientByEmail(@PathVariable("email") String email){
        return clientService.getClientByEmail(email);
    }



}
