package com.ensah.core.services.impl;

import com.ensah.core.bo.Element;
import com.ensah.core.bo.InscriptionAnnuelle;
import com.ensah.core.bo.InscriptionMatiere;
import com.ensah.core.dao.InscriptionMatiereDao;
import com.ensah.core.services.InscriptionMatiereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InscriptionMatiereServiceImpl implements InscriptionMatiereService {

    @Autowired
    InscriptionMatiereDao inscriptionMatiereDao;
    @Override
    public <S extends InscriptionMatiere> S save(S entity) {
        return inscriptionMatiereDao.save(entity);
    }

    public InscriptionMatiere findByElementAndInscriptionAnnuelle(Element elt, InscriptionAnnuelle inscriptionAnnuelle){
        return inscriptionMatiereDao.findByInscriptionAnnuelleAndAndMatiere(inscriptionAnnuelle,elt);
    }
}
