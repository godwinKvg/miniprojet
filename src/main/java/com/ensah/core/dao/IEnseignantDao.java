package com.ensah.core.dao;

import com.ensah.core.bo.Enseignant;
import com.ensah.core.bo.Module;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEnseignantDao extends JpaRepository<Enseignant,Long> {
    public Enseignant findByNomIgnoreCase(String nom);

    public Enseignant findBySpecialite(String specialite);
}
