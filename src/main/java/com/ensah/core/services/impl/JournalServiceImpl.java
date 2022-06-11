package com.ensah.core.services.impl;

import com.ensah.core.bo.Journal;
import com.ensah.core.dao.JournalDao;
import com.ensah.core.services.JournalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class JournalServiceImpl implements JournalService {
    @Autowired
    JournalDao journalDao;
    @Override
    public void sauvegarderJournalModification(Journal journal) {
         journalDao.save(journal);
    }
}
