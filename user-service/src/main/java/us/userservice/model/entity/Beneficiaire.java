package us.userservice.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Beneficiaire {
    @Id @GeneratedValue
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String phone;
    private Boolean isBlockListed;
    private String walletCode;
    //Parent client (the client who owns Beneficiaire records)
    @ManyToOne
    @JsonIgnore
    private Client client;

    // Reference to benefeciary wallet account
    @OneToOne()
    @JsonIgnore
    private Client walletClient;


}
