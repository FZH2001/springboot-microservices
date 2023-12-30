package us.userservice.model.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  long id ;
    @Column(name = "ref")
    private String ref;

    public long getId() {
        return id;
    }

    public Transaction(){

    }

    public Transaction(String ref, Date datetime, Double montant) {
        this.ref = ref;
        this.datetime = datetime;
        this.montant = montant;
    }

    @Column(name = "datetime")
    private Date datetime;
    @Column(name = "montant")
    private Double montant;

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }




}
