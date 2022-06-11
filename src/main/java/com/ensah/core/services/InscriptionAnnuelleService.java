package com.ensah.core.services;

import com.ensah.core.bo.Etudiant;
import com.ensah.core.bo.InscriptionAnnuelle;

public interface InscriptionAnnuelleService {
    InscriptionAnnuelle findByEtudiant(Etudiant etudiant);
}
