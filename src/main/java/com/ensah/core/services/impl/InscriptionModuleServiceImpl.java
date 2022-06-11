package com.ensah.core.services.impl;

import com.ensah.core.bo.InscriptionAnnuelle;
import com.ensah.core.bo.InscriptionModule;
import com.ensah.core.dao.InscriptionModuleDao;
import com.ensah.core.services.InscriptionModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InscriptionModuleServiceImpl implements InscriptionModuleService {

    @Autowired
    InscriptionModuleDao inscriptionModuleDao;

    @Override
    public InscriptionModule save(InscriptionModule entity) {
        return inscriptionModuleDao.save(entity);
    }

    public InscriptionModule findByInscriptionAnnuelle(InscriptionAnnuelle inscriptionAnnuelle){
        return inscriptionModuleDao.findByInscriptionAnnuelle(inscriptionAnnuelle);
    }
}
