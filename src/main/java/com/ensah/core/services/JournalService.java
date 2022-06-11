package com.ensah.core.services;

import com.ensah.core.bo.Journal;
import org.springframework.beans.factory.annotation.Autowired;

public interface JournalService {

    public  void  sauvegarderJournalModification(Journal journal);
}
