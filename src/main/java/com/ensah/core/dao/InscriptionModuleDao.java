package com.ensah.core.dao;

import com.ensah.core.bo.InscriptionAnnuelle;
import com.ensah.core.bo.InscriptionModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InscriptionModuleDao  extends JpaRepository<InscriptionModule,Long> {
    InscriptionModule  save(InscriptionModule entity);

    InscriptionModule findByInscriptionAnnuelle(InscriptionAnnuelle inscriptionAnnuelle);
}
