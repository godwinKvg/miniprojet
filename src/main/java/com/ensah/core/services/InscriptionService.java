package com.ensah.core.services;

import com.ensah.core.bo.Etudiant;
import com.ensah.core.bo.InscriptionAnnuelle;
import com.ensah.core.services.exceptions.InscriptionFailureException;

public interface InscriptionService {


    public  String reinscrireEtudiant (Etudiant etudiant);
    public  void  inscrireEtudiant(Etudiant etudiant) ;



}
