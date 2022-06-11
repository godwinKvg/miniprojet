package com.ensah.core.services.impl;

import com.ensah.core.bo.Etudiant;
import com.ensah.core.bo.InscriptionAnnuelle;
import com.ensah.core.dao.InscriptionAnnuelleDao;
import com.ensah.core.services.InscriptionAnnuelleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InscriptionAnnuelleServiceImpl implements InscriptionAnnuelleService {
    @Autowired
    InscriptionAnnuelleDao inscriptionAnnuelleDao;

    public InscriptionAnnuelle findByEtudiant(Etudiant etudiant){
        return inscriptionAnnuelleDao.findByEtudiant(etudiant);
    }
}
