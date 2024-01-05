package us.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import us.userservice.model.entity.Beneficiaire;

import java.util.List;

@Repository
public interface BeneficiaireRepository extends JpaRepository<Beneficiaire,Long> {
    List<Beneficiaire> findAllByClient_Id(Long clientId);

    @Query("SELECT b FROM Beneficiaire b WHERE (b.nom LIKE %:term%) OR (b.prenom LIKE %:term%) OR (b.walletCode LIKE %:term%)")
    List<Beneficiaire> searchBeneficiaireByTerm(@Param("term") String term);



    @Query("SELECT b.isBlockListed FROM Beneficiaire b WHERE b.id = :id")
    Boolean isBeneficiaireBlockListed(@Param("id") Long id);

}
