package com.ensah.core.dao;

import com.ensah.core.bo.Journal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface JournalDao extends CrudRepository<Journal,Long> {
}
