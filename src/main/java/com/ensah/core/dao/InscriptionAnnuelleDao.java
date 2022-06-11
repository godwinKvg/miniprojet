package com.ensah.core.dao;

import com.ensah.core.bo.Etudiant;
import com.ensah.core.bo.InscriptionAnnuelle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InscriptionAnnuelleDao extends JpaRepository<InscriptionAnnuelle,Long> {
    InscriptionAnnuelle findByEtudiant(Etudiant etudiant);
}
