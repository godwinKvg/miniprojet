package com.ensah.core.services.impl;

import com.ensah.core.bo.Etudiant;
import com.ensah.core.bo.InscriptionAnnuelle;
import com.ensah.core.bo.Niveau;
import com.ensah.core.dao.NiveauDao;
import com.ensah.core.services.NiveauService;
import com.ensah.core.services.exceptions.InscriptionFailureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class NiveauServiceImpl implements NiveauService {

    @Autowired
    NiveauDao niveauDao;
    List<Integer>niveauxPossiblesCp2= Arrays.asList(3,6,9,13,16,23,26);
    //Ces niveaux ne peuvent pas etre acceptes car ils ne  sont  pas des niveaux a proprement parler.
    List<Integer>niveauxImpossibles=Arrays.asList(12,5,8);
    List<Integer>niveauxPasSuivant=Arrays.asList(19,18,17,20);
    public static  String message="";

    @Override
    public boolean findIfNiveauExists(Long id) {

        return  !niveauDao.existsById(id);

    }

    @Override
    public boolean validerNiveau(Etudiant etudiant, Long id)  {
        boolean errorOccured=false;

        Niveau niveauActuel=niveauDao.getById(id);
        Integer identifiant= Math.toIntExact(id);

        //On compare la filiere d;inscription de l'utilisateur a l'ancienne filiere
        //Attention a tenir compte des classes prepas dont les filieres sont differentes d ou la 2 e condition
        List<InscriptionAnnuelle> inscriptions=etudiant.getInscriptions();
        InscriptionAnnuelle derniereInscriptionAnnulle=inscriptions.get(inscriptions.size()-1);
        //Un bug est remarque ici.En effet tout utilisateur voulant s'inscrire en derniere annee n'est pas soumise
        //aux restrictions.A revoir un peu tard
          if(!niveauxPasSuivant.contains(identifiant)){
              //On cherche a savoir si l'utilisateur a ete specifique en ce qui concerne son niveau
              //en ne choisissant pas les niveaux impossibles


              if(!niveauxImpossibles.contains(identifiant)){

                  if(niveauActuel.getFiliere().getTitreFiliere().equals(derniereInscriptionAnnulle.getNiveau().getFiliere().getTitreFiliere()
                  ) || derniereInscriptionAnnulle.getNiveau().getIdNiveau()==Long.valueOf(2) && niveauxPossiblesCp2.contains(identifiant))
                  {
                      //On verifie s'il est admis l'annee derniere
                      if(derniereInscriptionAnnulle.getValidation().equals("oui")){

                          System.out.println(derniereInscriptionAnnulle.getNiveau().getNiveauSuivant().getIdNiveau()+"suivant" +id);

                          //On verifie si le niveau dans le fichier correspond au niveau suivant:
                          if(id==derniereInscriptionAnnulle.getNiveau().getNiveauSuivant().getIdNiveau()
                                  || niveauxPossiblesCp2.contains(identifiant))
                          {


                          }else{
                              errorOccured=true;
                              message="Niveau incompatible pour l'etudiant "+etudiant.getPrenom();
                              System.out.println("Niveau incompatible pour l'etudiant "+etudiant.getPrenom());
                          }


                      }
                      //S'il n'avait pas admis,il doit avoir le meme niveau id
                      else{
                          System.out.println(derniereInscriptionAnnulle.getNiveau().getNiveauSuivant().getIdNiveau()+"suivant"+id);

                          if(derniereInscriptionAnnulle.getNiveau().getIdNiveau()==id){

                          }
                          else{
                              errorOccured=true;
                              message="L'etudiant "+etudiant.getPrenom()+" a echoue et ne peut donc pas changer de niveau";
                              System.out.println("L'etudiant "+etudiant.getPrenom()+" a echoue et ne peut donc pas changer de niveau");
                          }

                      }

                  }
                  else{
                      errorOccured=true;
                      message="Attention les filieres ne sont pas les memes pour "+etudiant.getPrenom()
                              +" il etait dans "+derniereInscriptionAnnulle.getNiveau().getFiliere().getTitreFiliere()+"" +
                              " mais veut continuer en "+niveauActuel.getFiliere().getTitreFiliere();
                      System.out.println("Attention les filieres ne sont pas les memes pour "+etudiant.getPrenom()
                              +" il etait dans "+derniereInscriptionAnnulle.getNiveau().getFiliere().getTitreFiliere()+"" +
                              " mais veut continuer en "+niveauActuel.getFiliere().getTitreFiliere());
                  }

              }
              else{
                  errorOccured=true;
                  message="Une option ne peut etre choisi comme niveau\n" +
                          "";
                  System.out.println("Une option ne peut etre choisi comme niveau\n" +
                          "");
                  if (id == 12){ System.out.println("Cycle d'ingenieur n;est pas niveau precis pour"+etudiant.getPrenom())
                  ;message+="Cycle d'ingenieur n;est pas niveau precis pour"+etudiant.getPrenom();}
                  if(id==5){ System.out.println("Genie informatique 3 n'est pas un niveau precis pour "+etudiant.getPrenom());
                  message+="Genie informatique 3 n'est pas un niveau precis pour "+etudiant.getPrenom();}
                  if(id==8){ System.out.println("Genie Civil 3 n'est pas un niveau precis pour "+etudiant.getPrenom());
                  message="Genie Civil 3 n'est pas un niveau precis pour "+etudiant.getPrenom();}
              }
          }


         return  errorOccured;


    }

    @Override
    public boolean checkLevelFaisability(int id_niveau) {

        return  !niveauxImpossibles.contains(id_niveau);
    }


}
