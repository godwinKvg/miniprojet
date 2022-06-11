package com.ensah.core.web.controllers;

import com.ensah.core.services.*;
import com.ensah.core.services.impl.EnseignantServiceImpl;
import com.ensah.core.services.impl.ModuleServiceImpl;
import com.ensah.core.utils.ExcelImportNotesModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpSession;

@MultipartConfig
@Controller
public class NoteModuleController {
    @Autowired
    HttpSession session;

    @Autowired
    ModuleServiceImpl moduleServiceImpl;

    @Autowired
    EnseignantServiceImpl enseignantService;

    @Autowired
    InscriptionAnnuelleService inscriptionAnnuelleService;
    @Autowired
    InscriptionMatiereService inscriptionMatiereService;
    @Autowired
    InscriptionModuleService inscriptionModuleService;
    @Autowired
    EtudiantService etudiantService;
    @Autowired
    ElementService elementService;

    @GetMapping("/prof/module")
    public String getUpload(){

        return "prof/notesModule";
    }


    @PostMapping("/prof/module")
    public String importFile(@RequestParam("file") MultipartFile file,Model model) {

        ExcelImportNotesModule excelImportNotesModule = ExcelImportNotesModule.getInstance();


        excelImportNotesModule.importProcess(file,model,moduleServiceImpl,enseignantService,etudiantService,inscriptionAnnuelleService,inscriptionModuleService,inscriptionMatiereService,elementService);

        return "prof/notesModule";
    }
}
