package com.example.fundtransferservice.model.rest.response;

import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class ClientResponse {
    private Long id;
    private String title;
    private String prenom;
    private String typePieceIdentite;
    private String paysEmissionPieceIdentite;
    private String numeroPieceIdentite;
    private Date expirationPieceIdentite;
    private Date dateNaissance;
    private String profession;
    private String paysNationalite;
    private String paysAdresse;
    private String adresseLegale;
    private String ville;
    private String gsm;
    private String email;
    private AgentResponse agentResponse;
    private Double solde;
    private List<BeneficiaryResponse> beneficiaires;
}
