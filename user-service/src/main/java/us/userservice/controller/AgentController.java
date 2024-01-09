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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/agents")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class AgentController {
    private final AgentService agentService;
    private final ClientService clientService;

    @GetMapping("/get-agent-data/{id}")
    public ResponseEntity<Agent> getAgentData(@PathVariable Long id){
        return new ResponseEntity<>(agentService.getAgentData(id),HttpStatus.OK);
    }
    @PostMapping("/createAgent")
    public ResponseEntity<Object> createAgent(@RequestBody Agent agent){
        try{
            agentService.createAgent(agent);
        return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Error : "+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
    public ResponseEntity<Map> saveOrUpdateClient(@RequestBody Client client) {
        Map<String,String> response = new HashMap<>();
        try{
            agentService.saveOrUpdateClient(client);
            response.put("message","Client saved or updated successfully");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch (Exception e){
            response.put("message","Error : "+e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/client/{id}")
    public ResponseEntity<Map<String, String>> updateClient(@RequestBody Client client, @PathVariable Long id) {
        try {
            agentService.updateClient(client, id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Client updated successfully");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Client not found");

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Internal server error");

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);        }
    }

    @DeleteMapping("/client/{id}")
    public ResponseEntity<Map<String, String>> deleteClient(@PathVariable Long id) {
        agentService.deleteClient(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Client deleted successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
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
