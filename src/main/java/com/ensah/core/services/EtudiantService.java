package com.ensah.core.services;

import com.ensah.core.bo.Etudiant;
import com.ensah.core.bo.Journal;

public interface EtudiantService  {

    public boolean findIfEtudiantExists(Long id);
    public  Etudiant getEtudiant(Long id);
    public  boolean updateEtudiantNomPrenomCne(Etudiant etudiant, Journal journal);
    public  void saveEtudiant(Etudiant etudiant);


}
