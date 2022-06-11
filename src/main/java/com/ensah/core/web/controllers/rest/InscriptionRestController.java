package com.ensah.core.web.controllers.rest;

import com.ensah.core.bo.Etudiant;
import com.ensah.core.bo.Filiere;
import com.ensah.core.bo.Journal;
import com.ensah.core.bo.Utilisateur;
import com.ensah.core.dao.FiliereDao;
import com.ensah.core.services.JournalService;
import com.ensah.core.services.NiveauService;
import com.ensah.core.services.exceptions.InscriptionFailureException;
import com.ensah.core.services.impl.EtudiantServiceImpl;
import com.ensah.core.services.impl.InscriptionServiceImpl;
import com.ensah.core.services.impl.JournalServiceImpl;
import com.ensah.core.utils.ExcellFileRowObject;
import com.ensah.core.utils.ResponseTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.xml.crypto.Data;
import java.util.*;

@RestController
public class InscriptionRestController {

    @Autowired
    HttpSession session;
    @Autowired
    EtudiantServiceImpl etudiantServiceImpl;
    @Autowired
    FiliereDao filiereDao;
    ExcellFileRowObject targetRow;
    Etudiant targetEtudiant;
    @Autowired
    NiveauService niveauServiceImpl;

    @Autowired
    InscriptionServiceImpl inscriptionService;
    List<Integer>niveauxImpossibles=Arrays.asList(12,5,8);


    @RequestMapping(value = "/admin/rest/updateInfos/{email}",method = RequestMethod.GET)
    public  String  updateInfos(@PathVariable(name = "email",required = true) String email){

        List<Etudiant> etudiants= (List<Etudiant>) session.getAttribute("badInfos");
        List<ExcellFileRowObject>rows= (List<ExcellFileRowObject>) session.getAttribute("badInfoExcell");
        List<ExcellFileRowObject>rowsWithNoErros= (List<ExcellFileRowObject>) session.getAttribute("inscritspaserreur");
        List<Etudiant> etudiantDejaInscrits= (List<Etudiant>) session.getAttribute("dejaInscrits");


        for(int i=0;i<etudiants.size();i++){
            System.out.println(etudiants.get(i).getEmail());
            if(etudiants.get(i).getEmail().equals(email)){
                targetRow=rows.get(i);
                targetEtudiant=etudiants.get(i);
                Etudiant et=etudiants.get(i);
                ExcellFileRowObject ex=rows.get(i);
                Journal journal=new Journal();
                journal.setEvenement(et.getNom(),et.getPrenom(),et.getCne(),ex.getNom(),ex.getPrenom(), ex.getCne(),session);
                etudiants.get(i).setCne(rows.get(i).getCne());
                etudiants.get(i).setPrenom(rows.get(i).getPrenom());
                etudiants.get(i).setNom(rows.get(i).getNom());
                 etudiantServiceImpl.updateEtudiantNomPrenomCne(etudiants.get(i),journal);

                break;

            }






        }

        if(rowsWithNoErros==null) rowsWithNoErros=new ArrayList<>();
        if(etudiantDejaInscrits==null) etudiantDejaInscrits=new ArrayList<>();
        if(etudiants==null) etudiants=new ArrayList<>();
        if(rows==null) rows=new ArrayList<>();

        rowsWithNoErros.add(targetRow);
        etudiantDejaInscrits.add(targetEtudiant);
        rows.remove(targetRow);
        etudiants.remove(targetEtudiant);



        session.setAttribute("inscritspaserreur",rowsWithNoErros);
        session.setAttribute("dejaInscrits",etudiantDejaInscrits);

        //On verifie voir si toutes les lignes de donnees contradictoires ont ete corrigees
        if(rows.size()>0){
            session.setAttribute("badInfoExcell",rows);
            session.setAttribute("badInfos",etudiants);
        }
        else {
            session.removeAttribute("badInfos");
            session.removeAttribute("badInfoExcell");
        }

        return  "success";
    }


    @RequestMapping(value = "admin/validerInscriptions/{id}", method = RequestMethod.GET)
    public String validerInscriptionsPost(@PathVariable("id") int id) {



        List<Etudiant> etudiants= (List<Etudiant>) session.getAttribute("dejaInscrits");
        Etudiant etudiant=null;
        for(Etudiant et:etudiants){
            if(et.getIdUtilisateur()==id){
                etudiant=et;
                break;
            }
        }



          String message=  inscriptionService.reinscrireEtudiant(etudiant);
          return message;


    }

    @Transactional
    @RequestMapping(value = "admin/InscrireNouvel/{id}", method = RequestMethod.GET,produces =  MediaType.APPLICATION_JSON_VALUE)

    @ResponseBody
    public  ResponseTransfer validerInscriptionsNouveauxPost(@PathVariable("id") Long id)  {





        if(etudiantServiceImpl.findIfEtudiantExists(id)){

            return  new ResponseTransfer("Etudiant existe deja dans la base  de donnees.Donc pas queston de l'inscrire");
        }
        else{
            Etudiant etudiant=new Etudiant();
            Utilisateur u=new Utilisateur();
            List<ExcellFileRowObject> excellFileRowObjectsNotExistsInDatabase= (List<ExcellFileRowObject>) session.getAttribute("pasInscrits");
            for(ExcellFileRowObject ex:excellFileRowObjectsNotExistsInDatabase){
                if(ex.getId_etudiant()==id){
                     int niveauId= Math.toIntExact(ex.getId_niveau());
                    System.out.println(niveauId);
                     etudiant.setIdUtilisateur(id);
                     etudiant.setDateNaissance(new Date());
                     etudiant.setCne(ex.getCne());
                     etudiant.setNom(ex.getNom());
                     etudiant.setPrenom(ex.getPrenom());
                     etudiant.setCne(ex.getCne());
                     etudiant.setEmail(id+"jkfjkdfjk@gmail.com");
                     etudiant.setCin("jksdjkfjk"+id);
                     etudiant.setTelephone("89238932"+id);
                     etudiant.setIdNiveauTemporaire(ex.getId_niveau());

                     if(!niveauxImpossibles.contains(Math.toIntExact(etudiant.getIdNiveauTemporaire()))){

                           inscriptionService.inscrireEtudiant(etudiant);

                     }else {

                         if (niveauId==12) {
                             System.out.println("Cycle prepap n est pas filiere");
                             return new ResponseTransfer("Cycle d'ingenieur n;est pas niveau precis pour"+etudiant.getPrenom());
                         }
                         if(niveauId==5) return new ResponseTransfer("Genie informatique 3 n'est pas un niveau precis pour "+etudiant.getPrenom());
                         if(niveauId==8) return new ResponseTransfer("Genie Civil 3 n'est pas un niveau precis pour "+etudiant.getPrenom());
                     }

                }
            }

        }

        return new ResponseTransfer("success");

    }



}
