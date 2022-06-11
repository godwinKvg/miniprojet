package com.ensah.core.dao;

import com.ensah.core.bo.InscriptionAnnuelle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InscriptionDao extends JpaRepository<InscriptionAnnuelle,Long> {
}
