package com.example.fundtransferservice.model.rest.response;

import lombok.Data;

@Data
public class AgentResponse {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String phone;
}
