package us.userservice.service;

import us.userservice.model.entity.Beneficiaire;
import us.userservice.repository.BeneficiaireRepository;

import java.util.List;

public class TransactionService {
    private final BeneficiaireRepository beneficiaireRepository;

    public TransactionService(BeneficiaireRepository beneficiaireRepository) {
        this.beneficiaireRepository = beneficiaireRepository;
    }

}
