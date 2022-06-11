package com.ensah.core.dao;

import com.ensah.core.bo.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EtudiantDao  extends JpaRepository<Etudiant,Long> {

}
