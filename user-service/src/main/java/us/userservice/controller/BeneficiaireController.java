package us.userservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import us.userservice.model.entity.Beneficiaire;
import us.userservice.service.BeneficiaireService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/beneficiary")

@CrossOrigin(origins = {"http://localhost:4200","https://main--remarkable-starlight-5f7dc0.netlify.app","https://main--unique-moxie-e9385c.netlify.app/","https://659d28735cac669fa793e38d--chic-mousse-aa1b3d.netlify.app/"})

@AllArgsConstructor
public class BeneficiaireController{
    private final BeneficiaireService beneficiaireService;

    @GetMapping("/get-beneficiary-data/{id}")
    public Beneficiaire getBeneficiaryInfo(@PathVariable("id") Long beneficiaryId) {
        return beneficiaireService.getBeneficiaryById(beneficiaryId);
    }

    @PostMapping("/get-beneficiary-data")
    public List<Beneficiaire> getBeneficiaryInfoByName(@RequestBody Map<String, Object> requestParams) {
        String nom = (String) requestParams.get("nom");
        String prenom = (String) requestParams.get("prenom");
        return beneficiaireService.getBeneficiaryInfoByName(nom, prenom);
    }
    @PostMapping("/get-beneficiary-data-by-code")
    public Beneficiaire getBeneficiaryInfoByCode(@RequestBody Map<String, Object> requestParams) {
        String walletCode = (String) requestParams.get("walletCode");
        return beneficiaireService.getBeneficiaryInfoByCode(walletCode);
    }

    @PostMapping("/update")
    public String updateBeneficiaire(@RequestBody Beneficiaire b) {
        beneficiaireService.updateBeneficiaire(b);
        return "Beneficiaire updated successfully";
    }

}