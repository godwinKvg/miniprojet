package com.ensah.core.utils;

import com.ensah.core.bo.Etudiant;
import com.ensah.core.dao.NiveauDao;
import com.ensah.core.services.NiveauService;
import com.ensah.core.services.exceptions.InscriptionFailureException;
import com.ensah.core.services.impl.EtudiantServiceImpl;
import com.ensah.core.services.impl.NiveauServiceImpl;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@MultipartConfig
public class ExcellImporter {

    boolean errorOccured=false;
    static String[] HEADERS = {"ID ETUDIANT","CNE", "NOM", "PRENOM","ID NIVEAU ACTUEL","TYPE"};
    static String SHEET = "etudiants";
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";





    public  static boolean hasExcelFormat(MultipartFile file){
        if(TYPE.equals(file.getContentType())){
            return  true;
        }
        return  false;
    }

    public  void excellFileDataPreprocessing(EtudiantServiceImpl etudiantServiceImpl, InputStream is, HttpSession session, NiveauService niveauService) throws  InscriptionFailureException{

        List<ExcellFileRowObject> excellFileRowObjectsNotExistsInDatabase=new ArrayList<>();
        List<ExcellFileRowObject> excellFileRowObjectsExistsInDatabaseWithErrors=new ArrayList<>();
        List<ExcellFileRowObject> excellFileRowObjectsExistsWithoutErrors=new ArrayList<>();
        List<Etudiant>alreadyRegisteredStudents=new ArrayList<>();
        List <Etudiant>alreadyRegisteredStudentsWithErrors=new ArrayList<>();
        cleanSessions(session);
        String message="";


        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();


            int rowNumber = 0;//Ligne 0 correspond aux headers
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                Iterator<Cell> cellsInRow = currentRow.iterator();
                String []excelHeaders=new String[7];
                if(rowNumber==0){
                    rowNumber++;
                    int i=0;
                    while (cellsInRow.hasNext()){
                        Cell currentCell=cellsInRow.next();
                        excelHeaders[i]=currentCell.getStringCellValue();
                        i++;
                    }

                    System.out.println("Occured 1 "+errorOccured);
                   if(i==6){//Si le nombre de colonnes est convenable
                       if(checkFileHeaders(HEADERS,excelHeaders)){//Si les entetes sont les memes
                       continue;
                       }
                       else {
                           message="Entetes differentes";
                           System.out.println("Entetes differentes");
                           errorOccured=true;
                           break;
                       }
                   }
                   else{
                       message="Le nombre de colonnes n'est pas convenable";
                       System.out.println("Le nombre de colonnes n'est pas convenable");
                       errorOccured=true;
                       break;
                   }

                }

                int cellX=0;
                long id=0,id_niveau=0;
                String nom="",prenom="",cne="",type="";
                while (cellsInRow.hasNext()) {
                    Cell currentCell=cellsInRow.next();

                    switch (cellX){
                        case 0:
                            id=(long) currentCell.getNumericCellValue();
                            break;
                        case 1: cne=currentCell.getStringCellValue();
                            break;
                        case 2:nom=currentCell.getStringCellValue();
                            break;
                        case 3:prenom =currentCell.getStringCellValue();
                            break;
                        case 4: id_niveau=(long)currentCell.getNumericCellValue();
                            break;
                        case 5:
                            type=currentCell.getStringCellValue();
                    }
                    cellX++;
                }



                 ExcellFileRowObject excellFileRowObject=new ExcellFileRowObject(cne,nom,prenom,type,id_niveau,id);
                errorOccured=niveauService.findIfNiveauExists(excellFileRowObject.getId_niveau());

                if(errorOccured){
                    message="Le niveau pour l'etudiant " + excellFileRowObject.getNom() + " est " + excellFileRowObject.getId_niveau() +
                            " Ce niveau n'exite pas";
                    System.out.println("Le niveau pour l'etudiant " + excellFileRowObject.getNom() + " est " + excellFileRowObject.getId_niveau() +
                            " Ce niveau n'exite pas");
                    break;
                };//Si le niveau n'est pas convenable,on arrete
                System.out.println("Occured 3 "+errorOccured);
                if(etudiantServiceImpl.findIfEtudiantExists(id)){
                    Etudiant etudiant=etudiantServiceImpl.getEtudiant(id);
                    System.out.println(id_niveau+" est l'id temporaire");
                    etudiant.setIdNiveauTemporaire(id_niveau);


                        errorOccured=  niveauService.validerNiveau(etudiant,excellFileRowObject.getId_niveau());


                    if(errorOccured){
                        message= NiveauServiceImpl.message;
                        break;
                    }


                    if(!checkReInscriptionValidity(excellFileRowObject)){
                        message="Le type d'inscription de M."+excellFileRowObject.getNom()+"" +
                        " existant dans la base est  "+excellFileRowObject.getType()+" Ce qui ne convient pas";
                        System.out.println("Le type d'inscription de M."+excellFileRowObject.getNom()+"" +
                                " existant dans la base est  "+excellFileRowObject.getType()+" Ce qui ne convient pas");
                        break;
                    }
                    if(!excellFileRowObject.getCne().equals(etudiant.getCne()) ||
                            !excellFileRowObject.getNom().equals(etudiant.getNom()) ||
                            !excellFileRowObject.getPrenom().equals(etudiant.getPrenom())){

                          alreadyRegisteredStudentsWithErrors.add(etudiant);
                          excellFileRowObjectsExistsInDatabaseWithErrors.add(excellFileRowObject);
                    }

                    else{
                        excellFileRowObjectsExistsWithoutErrors.add(excellFileRowObject);
                        alreadyRegisteredStudents.add(etudiant);
                    }
                }
                else{
                    excellFileRowObjectsNotExistsInDatabase.add(excellFileRowObject);
                    if(!checkInscriptionValidity(excellFileRowObject)){
                        System.out.println("Le type d'inscription de M."+excellFileRowObject.getNom()+"" +
                                " "+excellFileRowObject.getType()+" Ce qui ne convient pas");
                        message="Le type d'inscription de M."+excellFileRowObject.getNom()+"" +
                                " "+excellFileRowObject.getType()+" Ce qui ne convient pas";
                        errorOccured=true;
                        break;
                    }
                }

                rowNumber++;

            }


           if(errorOccured){//S'il y a eu d'erreur
                session.setAttribute("erreur",true);
           }
           else{
               session.setAttribute("inscritspaserreur",excellFileRowObjectsExistsWithoutErrors);
            session.setAttribute("dejaInscrits",alreadyRegisteredStudents);
              session.setAttribute("pasInscrits",excellFileRowObjectsNotExistsInDatabase);

               System.out.println(alreadyRegisteredStudents.size()+" est la taille du fichier");
               if(alreadyRegisteredStudentsWithErrors.size()>0 ){
                   session.setAttribute("badInfos",alreadyRegisteredStudentsWithErrors);
                   session.setAttribute("badInfoExcell",excellFileRowObjectsExistsInDatabaseWithErrors);
               }

           }

            workbook.close();
           if(errorOccured) throw new InscriptionFailureException(message);

        } catch (InscriptionFailureException inscriptionFailureException){
             throw  new InscriptionFailureException(message);
        }
        catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());

        }

    }


    public  boolean checkFileHeaders(String [] headers,String[] excelHeaders){
        boolean correct=true;
        for(int i=0;i<6;i++){
            if(!headers[i].equals(excelHeaders[i])){
                return  false;
            }
        }
        return  correct;
    }

    public  boolean checkReInscriptionValidity(ExcellFileRowObject row){
             String type=row.getType();
             type=type.toLowerCase();
        return type.equals("reinscription");

    }

    public  boolean checkInscriptionValidity(ExcellFileRowObject row){

        String type=row.getType();
        type=type.toLowerCase();
        return type.equals("inscription");

    }

    public  boolean checkNiveauConvenable(){
        boolean flag=true;
        return flag;
    }

    public  void cleanSessions(HttpSession session){
        if(session.getAttribute("inscritspaserreur")!=null) session.removeAttribute("inscritspaserreur");
        if(session.getAttribute("badInfos")!=null) session.removeAttribute("badInfos");
        if(session.getAttribute("badInfoExcell")!=null) session.removeAttribute("badInfoExcell");
        if(session.getAttribute("dejaInscrits")!=null) session.removeAttribute("dejaInscrits");
        if(session.getAttribute("erreur")!=null) session.removeAttribute("erreur");
        if(session.getAttribute("pasInscrits")!=null) session.removeAttribute("pasInscrits");


    }

}
