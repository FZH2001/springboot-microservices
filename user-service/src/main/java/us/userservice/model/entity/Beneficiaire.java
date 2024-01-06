package us.userservice.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import us.userservice.serializer.HibernateProxySerializer;

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
    @OneToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = HibernateProxySerializer.class)
    private Client walletClient;


}
