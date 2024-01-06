package us.userservice.service;

import org.springframework.stereotype.Service;
import us.userservice.model.entity.Beneficiaire;
import us.userservice.repository.BeneficiaireRepository;

import java.util.List;
@Service
public class TransactionService {
    private final BeneficiaireRepository beneficiaireRepository;

    public TransactionService(BeneficiaireRepository beneficiaireRepository) {
        this.beneficiaireRepository = beneficiaireRepository;
    }

}
