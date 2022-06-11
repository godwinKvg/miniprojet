package com.ensah.core.services;

import com.ensah.core.bo.Enseignant;

public interface IEnseignantService {
    public Enseignant findByNom(String nom);
    public Enseignant findBySpecialite(String nom);
}
