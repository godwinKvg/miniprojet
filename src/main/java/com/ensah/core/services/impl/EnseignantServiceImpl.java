package com.ensah.core.services.impl;

import com.ensah.core.bo.Enseignant;
import com.ensah.core.dao.IEnseignantDao;
import com.ensah.core.services.IEnseignantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnseignantServiceImpl implements IEnseignantService {

    @Autowired
    IEnseignantDao enseignantDao;

    @Override
    public Enseignant findByNom(String nom) {
        System.out.println("Recherche Enseignant par Nom ");
        return enseignantDao.findByNomIgnoreCase(nom);
    }

    @Override
    public Enseignant findBySpecialite(String specialite) {
        return enseignantDao.findBySpecialite(specialite);
    }


}
