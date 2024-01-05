package us.userservice.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import us.userservice.model.entity.Beneficiaire;
import us.userservice.repository.BeneficiaireRepository;

import java.util.List;
@Service
@Data
@AllArgsConstructor
public class BeneficiaireService {
    private final BeneficiaireRepository beneficiaireRepository;

    public Beneficiaire getBeneficiaryById(Long beneficiaryId) {
        return beneficiaireRepository.findById(beneficiaryId).orElse(null);
    }
    public List<Beneficiaire> getBeneficiaryInfoByName(String nom, String prenom) {
        String term = nom + " " + prenom;
        return beneficiaireRepository.searchBeneficiaireByTerm(term);
    }
    public Beneficiaire getBeneficiaryInfoByCode(String walletCode) {
        // Assuming wallet code is unique
        List<Beneficiaire> beneficiaries = beneficiaireRepository.searchBeneficiaireByTerm(walletCode);
        return beneficiaries.isEmpty() ? null : beneficiaries.get(0);
    }
}
