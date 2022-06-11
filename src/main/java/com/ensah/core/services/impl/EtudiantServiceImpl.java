package com.ensah.core.services.impl;

import com.ensah.core.bo.Etudiant;
import com.ensah.core.bo.Journal;
import com.ensah.core.dao.EtudiantDao;
import com.ensah.core.services.EtudiantService;
import org.apache.logging.log4j.core.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@Service
public class EtudiantServiceImpl  implements EtudiantService {

    @Autowired
    EtudiantDao etudiantDao;
    @Autowired
    JournalServiceImpl journalServiceImpl;
    @Autowired
    HttpSession session;

    @Override
    public boolean findIfEtudiantExists(Long id) {

        return  etudiantDao.existsById(id);
    }
    public  Etudiant getEtudiant(Long id){
        return  etudiantDao.getById(id);
    }


    public  boolean updateEtudiantNomPrenomCne(Etudiant etudiant,Journal journal){

        etudiantDao.save(etudiant);
        journalServiceImpl.sauvegarderJournalModification(journal);
        return  true;




    }

    @Override
    public void saveEtudiant(Etudiant etudiant) {
          etudiantDao.save(etudiant);
    }

}
