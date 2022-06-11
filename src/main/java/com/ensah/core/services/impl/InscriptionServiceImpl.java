package com.ensah.core.services.impl;

import com.ensah.core.bo.*;
import com.ensah.core.bo.Module;
import com.ensah.core.dao.*;
import com.ensah.core.services.InscriptionService;
import com.ensah.core.services.ModuleService;
import com.ensah.core.services.exceptions.InscriptionFailureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class InscriptionServiceImpl implements InscriptionService {

    @Autowired
    InscriptionDao inscriptionDao;
    @Autowired
    ModuleService moduleService;
    @Autowired
    NiveauDao niveauDao;

    @Autowired
    EtudiantServiceImpl etudiantServiceimpl;

    @Autowired
    InscriptionModuleDao inscriptionModuleDao;
    @Autowired
    IUtilisateurDao utilisateurDao;

    List<Integer>niveauxPossiblesCp2= Arrays.asList(3,6,9,13,16,23,26);
    List<Integer>niveauxOptions=Arrays.asList(17,18,19,20,29);

    //A ces niveaux il faut valider,si non pas de niveaux suivants meme  pour les modules
    List<Integer>niveauxAvaliderAbsolument=Arrays.asList(12,7,5,11,15,17,18,19,20);
    //Annee actuelle
    int year=new  Date().getYear()+1900;
     public static String message="";

    



    @Override
    public String reinscrireEtudiant(Etudiant etudiant) {
        int niveau= Math.toIntExact(etudiant.getIdNiveauTemporaire());
        System.out.println(etudiant.getIdNiveauTemporaire());
        Niveau niveau1= niveauDao.getById(etudiant.getIdNiveauTemporaire());

        System.out.println(niveau1);
        //Cette affectation est necessaire au cas ou l'utilisateur rafraichit la page
        //apres avoir soumis la premiere fois.
         Etudiant etu=etudiantServiceimpl.getEtudiant(etudiant.getIdUtilisateur());

        List<InscriptionAnnuelle>inscs=etu.getInscriptions();
        InscriptionAnnuelle derniereInscriptionAnnuelle=inscs.get(inscs.size()-1);


        //Si la derniere inscription a une date anterieure ,bref s'il s'est inscrit deja pour cette annee
        boolean bool=derniereInscriptionAnnuelle.getAnnee()<year;

        if(derniereInscriptionAnnuelle.getAnnee()<year){
            //On verifie si l'etudiant etait admis
            if(derniereInscriptionAnnuelle.getValidation().equals("oui")){
                //On verfie s'il vient de CP2 et que le niveau suivant correspond a un niveau de Cp
                if(niveauxPossiblesCp2.contains(niveau)){
                    InscriptionAnnuelle inscriptionAnnuelle=new InscriptionAnnuelle();

                    //On l'inscrit d'abord au cycle d'ingenieur
                    process(niveau1,etudiant,1,"R");
                    //S'il est admis
                    //On l'inscrit dans la nouvelle filiere
                    process(niveau1,etudiant,2,"R");
                }
                //On verifie s'il veut s'inscrire dans les niveaux d'options GL,BI,BPC..
                else if(niveauxOptions.contains(niveau)){
                    //Inscrire dans la filiere GC3,GI3.....
                    process(niveau1,etudiant,3,"R");
                    //Inscrire dans l'option
                    process(niveau1,etudiant,4,"R");

                }
                else{
                    process(niveau1,etudiant,5,"R");
                }

            }
            //S'il n'etait pas admis,alors on le reinscrit dans son ancienne classe puis on l'inscrit
            //dans les modules de l'annee suivante exception faite pour CP2,GI2,GC2
            else {
                //Si c'est GI2,GC2,CP2,on le reinscrit et aussi dans les modules non valides
                if(niveauxAvaliderAbsolument.contains(niveau)){

                    failedStudentProcess(niveau1,etudiant,derniereInscriptionAnnuelle,1);
                }
                else{//Sinon,on le reinscrit dans les modules non valides et les modules de l'annee suivante
                    failedStudentProcess(niveau1,etudiant,derniereInscriptionAnnuelle,2);
                }


            }

        }
        else{

            System.out.println("Deja inscrits");
            message="L'etudiant "+etudiant.getNom()+"est deja inscrits\n Annnee passee "+derniereInscriptionAnnuelle.getAnnee()+"\n"+
            "Cette annee "+year;
            System.out.println("Annnee passee "+derniereInscriptionAnnuelle.getAnnee());
            System.out.println("Cette anneee "+year );

        }
        return message;

    }

    @Override
    public void inscrireEtudiant(Etudiant etudiant){
        Utilisateur u=new Utilisateur();
        u.setCin(etudiant.getCin());
        u.setNomArabe(etudiant.getNomArabe());
        u.setTelephone(etudiant.getTelephone());
        u.setNom(etudiant.getNom());
        u.setEmail(etudiant.getEmail());
        u.setIdUtilisateur(etudiant.getIdUtilisateur());
        Long idTemp=etudiant.getIdNiveauTemporaire();
        System.out.println(idTemp+" est l'Id temporaire");
        //Utilisateur u2=utilisateurDao.save(u);
         etudiantServiceimpl.saveEtudiant(etudiant);
        etudiant=etudiantServiceimpl.getEtudiant(etudiant.getIdUtilisateur());
        etudiant.setIdNiveauTemporaire(idTemp);

        Niveau niveau1= niveauDao.getById(idTemp);
        //On verfie s'il vient de CP2 et que le niveau suivant correspond a un niveau de Cp
        if(niveauxPossiblesCp2.contains(Math.toIntExact(idTemp))){
            InscriptionAnnuelle inscriptionAnnuelle=new InscriptionAnnuelle();

            //On l'inscrit d'abord au cycle d'ingenieur
            process(niveau1,etudiant,1,"I");
            //S'il est admis
            //On l'inscrit dans la nouvelle filiere
            process(niveau1,etudiant,2,"I");
        }
        //On verifie s'il veut s'inscrire dans les niveaux d'options GL,BI,BPC..
        else if(niveauxOptions.contains(Math.toIntExact(idTemp ))){
            //Inscrire dans la filiere GC3,GI3.....
            process(niveau1,etudiant,3,"I");
            //Inscrire dans l'option
            process(niveau1,etudiant,4,"I");

        }
        else{
            process(niveau1,etudiant,5,"I");
        }



    }


    public  void process(Niveau niveau,Etudiant etudiant,int i,String type){
        InscriptionAnnuelle inscriptionAnnuelle=new InscriptionAnnuelle();

        inscriptionAnnuelle.setEtudiant(etudiant);
        if("R".equals(type)) inscriptionAnnuelle.setType("REINSCRIPTION");
        else  inscriptionAnnuelle.setType("INSCRIPTION");
        inscriptionAnnuelle.setAnnee(new Date().getYear()+1900);
        inscriptionAnnuelle.setValidation("non");

        //Si l'etudiant quitte les prepas  pour un cycle d'ingenieur,on l'inscrit au cycle d'ingenieur
        if(i==1){

            Niveau n=new Niveau();
            n.setIdNiveau(12L);
            inscriptionAnnuelle.setNiveau(n);

        }
        else{

           //Cette condition pour des filieres  a option:derniere annnee
            if(i==3) {

                Long idT = etudiant.getIdNiveauTemporaire();
                //GC3 on l'inscrit d'abord dans GC3
                if (idT == 17 || idT == 18) {
                    Niveau n = new Niveau();
                    n.setIdNiveau(8L);
                    inscriptionAnnuelle.setNiveau(n);
                }
                //GI3 on l'inscrit d'abord dans GI3
                else if (idT == 19 || idT == 20 || idT == 29) {
                    Niveau n = new Niveau();
                    n.setIdNiveau(5L);
                    inscriptionAnnuelle.setNiveau(n);

                }
                }//Ici pour des filieres dont le suivant n'est pas une option
                else{

                      inscriptionAnnuelle.setNiveau(niveau);

                }




        }

        //PARTIE INSCRIPTION MODULE
        //Si l'inscription ne concerne pas  une etape telle que CP2,GI3,GC3 qui ne sont pas
        //de vrais niveaux donc n'ont pas de modules
        //1,3 sont les parametres que nous envoyons pour marquer CP2,GI3,GC3

        if(i==1 || i==3){

        }
        else {

            Set<InscriptionModule>inscM=new HashSet<>();
            for(Module m:niveau.getModules()){
                InscriptionModule inscriptionModule=new InscriptionModule();
                inscriptionModule.setModule(m);

                inscriptionModule.setInscriptionAnnuelle(inscriptionAnnuelle);
                inscM.add(inscriptionModule);
            }
            inscriptionAnnuelle.setInscriptionModules(inscM);
        }
        inscriptionDao.save(inscriptionAnnuelle);




    }


    public  void failedStudentProcess(Niveau niveau,Etudiant etudiant,InscriptionAnnuelle derniereInscriptionAnnulle,int i){
       // int niveau_suivant= Math.toIntExact(niveau.getNiveauSuivant().getIdNiveau());
        InscriptionAnnuelle inscriptionAnnuelle=new InscriptionAnnuelle();

        inscriptionAnnuelle.setEtudiant(etudiant);
        inscriptionAnnuelle.setNiveau(niveau);
        inscriptionAnnuelle.setType("REINSCRIPTION");
        inscriptionAnnuelle.setAnnee(new Date().getYear()+1900);
        inscriptionAnnuelle.setValidation("non");
        Set<InscriptionModule> inscriptionModuleSet=derniereInscriptionAnnulle.getInscriptionModules();

        inscriptionDao.save(inscriptionAnnuelle);
        for(InscriptionModule inscm:inscriptionModuleSet){

            if("non".equals(inscm.getValidation()) || inscm.getValidation()==null){
                InscriptionModule inM=new InscriptionModule();
                inM.setModule(inscm.getModule());
                inM.setInscriptionAnnuelle(inscriptionAnnuelle);
               inscriptionModuleDao.save(inM);

            }
        }
        //Si l'utilisateur peut s'inscrire dans les modules de l'annee suivante
        //Il ne peut s'inscrire que dans 2 modules
        int j=0;


        if(i==2){
         //Pour les niveau qui ont un suivant
            Set<InscriptionModule>inscM=new HashSet<>();
            Niveau niveauSuivant=niveau.getNiveauSuivant();
            for(Module m:niveauSuivant.getModules()){
                InscriptionModule inscriptionModule=new InscriptionModule();
                inscriptionModule.setModule(m);
                inscriptionModule.setInscriptionAnnuelle(inscriptionAnnuelle);
                inscriptionModuleDao.save(inscriptionModule);
                j++;
                if(j==2) break;
            }


        }





    }
}
