package com.ensah.core.dao;

import com.ensah.core.bo.Element;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElementDao extends JpaRepository<Element,Long> {
    Element findByNom(String nom);
}
