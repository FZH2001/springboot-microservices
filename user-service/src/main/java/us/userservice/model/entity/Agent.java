package us.userservice.model.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agent{
    @Id @GeneratedValue
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String phone;
    private Double solde;

}
