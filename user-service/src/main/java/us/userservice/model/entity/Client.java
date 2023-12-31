package us.userservice.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {
    @Id @GeneratedValue
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
    private String password;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private Agent agent;
    private Double solde;

    @JsonIgnore
    @OneToMany
    List<Beneficiaire> beneficiaires;


    @OneToOne(cascade = CascadeType.ALL)
    private Beneficiaire beneficiaire;


}
