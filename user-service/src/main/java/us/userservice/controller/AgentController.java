package us.userservice.controller;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import us.userservice.entity.Student;
import us.userservice.model.entity.Agent;
import us.userservice.model.entity.Client;
import us.userservice.service.AgentService;
import us.userservice.service.ClientService;

import java.util.List;

@RestController
@RequestMapping("/api/agents")
@AllArgsConstructor
public class AgentController {
    private final AgentService agentService;
    private final ClientService clientService;

    @GetMapping("/get-agent-data/{id}")
    public ResponseEntity<Agent> getAgentData(@PathVariable Long id){
        return new ResponseEntity<>(agentService.getAgentData(id),HttpStatus.OK);
    }
    //Warning : This is not tested yet
    @PutMapping("/update-data")
    public ResponseEntity<String> updateAgentData(@RequestBody Agent a){
        try{
            agentService.updateData(a);
            return new ResponseEntity<>("Agent updated successfully",HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/add-client/{benId}")
    public ResponseEntity<Object> createBeneficiaireClient(@PathVariable Long benId,@RequestBody Client c){
        try{
            Client out = clientService.saveBeneficiaireClient(c,benId);
            return new ResponseEntity<>(out,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Error : "+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/clients")
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = agentService.getAllClients();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        Client client = agentService.getClientById(id);
        return client != null
                ? new ResponseEntity<>(client, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/client")
    public ResponseEntity<String> saveOrUpdateClient(@RequestBody Client client) {
        agentService.saveOrUpdateClient(client);
        return new ResponseEntity<>("Client saved or updated successfully", HttpStatus.CREATED);
    }

    @PutMapping("/client/{id}")
    public ResponseEntity<String> updateClient(@RequestBody Client client, @PathVariable Long id) {
        try {
            agentService.updateClient(client, id);
            return new ResponseEntity<>("Client updated successfully", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while updating the client", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/client/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable Long id) {
        agentService.deleteClient(id);
        return new ResponseEntity<>("Client deleted successfully", HttpStatus.OK);
    }
    @PutMapping("/update-agent-solde/{id}")
    public ResponseEntity<String> updateAgentSolde(@PathVariable Long id, @RequestParam Double newSolde) {
        try {
            agentService.updateAgentSolde(id, newSolde);
            return new ResponseEntity<>("Agent solde updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}
