package com.ensah.core.services.impl;

import com.ensah.core.bo.Element;
import com.ensah.core.dao.ElementDao;
import com.ensah.core.services.ElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ElementServiceImpl implements ElementService {
    @Autowired
    ElementDao elementDao;

    public Optional<Element> findByIdMatiere(Long id){
        return elementDao.findById(id);
    }


    public Element findByNom(String alias) {
        return elementDao.findByNom(alias);
    }
}
