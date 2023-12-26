package us.userservice.model.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "title")
    private String title;
    @Column(name = "prenom")
    private String prenom;
    @Column(name = "typePieceIdentite")
    private String typePieceIdentite;
    @Column(name = "paysEmissionPieceIdentite")
    private String paysEmissionPieceIdentite;
    @Column(name = "numeroPieceIdentite")
    private String numeroPieceIdentite;
    @Column(name = "expirationPieceIdentite")
    private Date expirationPieceIdentite;
    @Column(name = "dateNaissance")
    private Date dateNaissance;
    @Column(name = "profession")
    private String profession;
    @Column(name = "paysNationnalite")
    private String paysNationalite;
    @Column(name = "paysAdresse")
    private String paysAdresse;
    @Column(name = "adresseLegale")
    private String adresseLegale;
    @Column(name = "ville")
    private String ville;
    @Column(name = "gsm")
    private String gsm;
    @Column(name = "email")
    private String email;

    public client(){

    }
    public client(String title, String prenom, String typePieceIdentite, String paysEmissionPieceIdentite, String numeroPieceIdentite, Date expirationPieceIdentite, Date dateNaissance, String profession, String paysNationalite, String paysAdresse, String adresseLegale, String ville, String gsm, String email) {
        this.title = title;
        this.prenom = prenom;
        this.typePieceIdentite = typePieceIdentite;
        this.paysEmissionPieceIdentite = paysEmissionPieceIdentite;
        this.numeroPieceIdentite = numeroPieceIdentite;
        this.expirationPieceIdentite = expirationPieceIdentite;
        this.dateNaissance = dateNaissance;
        this.profession = profession;
        this.paysNationalite = paysNationalite;
        this.paysAdresse = paysAdresse;
        this.adresseLegale = adresseLegale;
        this.ville = ville;
        this.gsm = gsm;
        this.email = email;
    }
    public long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTypePieceIdentite() {
        return typePieceIdentite;
    }


    public void setTypePieceIdentite(String typePieceIdentite) {
        this.typePieceIdentite = typePieceIdentite;
    }

    public String getPaysEmissionPieceIdentite() {
        return paysEmissionPieceIdentite;
    }

    public void setPaysEmissionPieceIdentite(String paysEmissionPieceIdentite) {
        this.paysEmissionPieceIdentite = paysEmissionPieceIdentite;
    }

    public String getNumeroPieceIdentite() {
        return numeroPieceIdentite;
    }

    public void setNumeroPieceIdentite(String numeroPieceIdentite) {
        this.numeroPieceIdentite = numeroPieceIdentite;
    }

    public Date getExpirationPieceIdentite() {
        return expirationPieceIdentite;
    }

    public void setExpirationPieceIdentite(Date expirationPieceIdentite) {
        this.expirationPieceIdentite = expirationPieceIdentite;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getPaysNationalite() {
        return paysNationalite;
    }

    public void setPaysNationalite(String paysNationalite) {
        this.paysNationalite = paysNationalite;
    }

    public String getPaysAdresse() {
        return paysAdresse;
    }

    public void setPaysAdresse(String paysAdresse) {
        this.paysAdresse = paysAdresse;
    }

    public String getAdresseLegale() {
        return adresseLegale;
    }

    public void setAdresseLegale(String adresseLegale) {
        this.adresseLegale = adresseLegale;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getGsm() {
        return gsm;
    }

    public void setGsm(String gsm) {
        this.gsm = gsm;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }




}
