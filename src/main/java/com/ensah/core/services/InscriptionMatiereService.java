package com.ensah.core.services;

import com.ensah.core.bo.Element;
import com.ensah.core.bo.InscriptionAnnuelle;
import com.ensah.core.bo.InscriptionMatiere;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InscriptionMatiereService {
    <S extends InscriptionMatiere> S save(S entity);

    public InscriptionMatiere findByElementAndInscriptionAnnuelle(Element elt, InscriptionAnnuelle inscriptionAnnuelle);
}
