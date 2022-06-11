package com.ensah.core.dao;

import com.ensah.core.bo.Element;
import com.ensah.core.bo.InscriptionAnnuelle;
import com.ensah.core.bo.InscriptionMatiere;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InscriptionMatiereDao extends JpaRepository<InscriptionMatiere,Long> {
    @Override
    <S extends InscriptionMatiere> S save(S entity);

    InscriptionMatiere findByInscriptionAnnuelleAndAndMatiere(InscriptionAnnuelle inscriptionAnnuelle, Element matiere);
}
