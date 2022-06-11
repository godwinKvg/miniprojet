package com.ensah.core.services;

import com.ensah.core.bo.Etudiant;
import com.ensah.core.services.exceptions.InscriptionFailureException;

public interface NiveauService {

    public boolean findIfNiveauExists(Long id);


    boolean validerNiveau(Etudiant etudiant, Long id) throws InscriptionFailureException;
    public  boolean checkLevelFaisability(int id_niveau);
}
