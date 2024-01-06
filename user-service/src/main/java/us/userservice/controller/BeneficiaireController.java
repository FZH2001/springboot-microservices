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


}