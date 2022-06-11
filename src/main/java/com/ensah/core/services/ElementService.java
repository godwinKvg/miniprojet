package com.ensah.core.services;

import com.ensah.core.bo.Element;

import java.util.Optional;

public interface ElementService {
    Optional<Element> findByIdMatiere(Long id);
    Element findByNom(String alias);
}
