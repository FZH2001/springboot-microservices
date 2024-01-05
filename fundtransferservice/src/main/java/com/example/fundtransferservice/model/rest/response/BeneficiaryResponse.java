package com.example.fundtransferservice.model.rest.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class BeneficiaryResponse {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String phone;
    private Boolean isBlockListed;
    //private ClientResponse clientResponse;
}
