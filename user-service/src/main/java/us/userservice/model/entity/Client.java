package us.userservice.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @ManyToOne
    private Agent agent ;


}
