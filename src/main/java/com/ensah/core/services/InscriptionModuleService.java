package com.ensah.core.services;

import com.ensah.core.bo.InscriptionAnnuelle;
import com.ensah.core.bo.InscriptionModule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InscriptionModuleService {
    InscriptionModule save(InscriptionModule entity);

    InscriptionModule findByInscriptionAnnuelle(InscriptionAnnuelle inscriptionAnnuelle);
}
