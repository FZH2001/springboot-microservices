package us.userservice.controller;

import lombok.AllArgsConstructor;
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
}
