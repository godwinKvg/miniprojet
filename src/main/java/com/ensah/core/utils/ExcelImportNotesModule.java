package com.ensah.core.utils;

import com.ensah.core.bo.*;
import com.ensah.core.bo.Module;
import com.ensah.core.services.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Year;
import java.util.*;

public class ExcelImportNotesModule {
    /** the unique instance of this class */
    private static final ExcelImportNotesModule instance = new ExcelImportNotesModule();
    private Sheet sheet = null;
    private Module module = null;

    IEnseignantService enseignantService = null;
    ModuleService moduleService = null;
    Model model = null;

    InscriptionAnnuelleService inscriptionAnnuelleService;
    InscriptionMatiereService inscriptionMatiereService;
    InscriptionModuleService inscriptionModuleService;
    EtudiantService etudiantService;
    ElementService elementService;
    Map<String,String> ERROR_MESSAGES = new HashMap<>();

    /**
     * private constructor (singleton DP)
     */
    private ExcelImportNotesModule(){
        ERROR_MESSAGES.put("format","Format du fichier invalide");
        ERROR_MESSAGES.put("structure","La structure du fichier a été modifiée");
        ERROR_MESSAGES.put("note","les notes doivent être des réels entre 0.0 et 20.0");
        ERROR_MESSAGES.put("enseignantM","L'enseignant ne correspond pas au module");
        ERROR_MESSAGES.put("enseignant","L'enseignant n'existe pas");
        ERROR_MESSAGES.put("module","Le module n'existe pas");
        ERROR_MESSAGES.put("annee","L'annee universitaire n'est pas correcte");
        ERROR_MESSAGES.put("niveauM","Le module n'est pas cohérent avec le niveau");
    }

    /**
     * returns the unique instance of this class
     */
    public static ExcelImportNotesModule getInstance(){
        return instance;
    }

    public static String EXCEL_FORMAT = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final List<String> HEADER1 = new ArrayList<>(Arrays.asList("Module","Semestre","Année"));
    public static final List<String> HEADER2 =  new ArrayList<>(Arrays.asList("Enseignant","Session","Classe"));

    // Cette constante nous servir pour la verification des formules
    private static final char[] ALPHABETS = {'E','F','G','H','I','J'};

    public void importProcess(MultipartFile file, Model model, ModuleService moduleService, IEnseignantService enseignantService, EtudiantService etudiantService, InscriptionAnnuelleService inscriptionAnnuelleService, InscriptionModuleService inscriptionModuleService,InscriptionMatiereService inscriptionMatiereService,ElementService elementService){

        this.model = model;
        this.enseignantService = enseignantService;
        this.moduleService = moduleService;
        this.etudiantService = etudiantService;
        this.inscriptionAnnuelleService = inscriptionAnnuelleService;
        this.inscriptionModuleService = inscriptionModuleService;
        this.inscriptionMatiereService = inscriptionMatiereService;

        Workbook workbook = null;

        try {

            workbook = new XSSFWorkbook(file.getInputStream());

            sheet = workbook.getSheetAt(0);

            // Format fichier
            if(!isExcelFormat(file)){
                setModelError("format");
                return;
            }

            // Annee Universitaire
            if (!checkSchoolYear()){
                setModelError("annee");
                return;
            }

            // Module Verification
            module = getModule(getExcelModuleName(),moduleService);

            System.out.println(module.getElements());

            if (module==null) {

                setModelError("module");
                return;
            }

            if (!checkModuleNiveau()){
                setModelError("niveauM");
                return;
            }

            // Enseignant
            if (!checkEnseignant()){
                setModelError("enseignant");
                return;
            }

            // Enseignant
            if (!checkModuleEnsignant()){
                setModelError("enseignantM");
                return;
            }

            // Verification des Notes
            if (!checkNotes()){
                setModelError("note");
                return;
            }

            // File Structure
            if(!checkFileStructure()){
                setModelError("structure");
                return;
            }

            // File Structure
            if(!checkFileStructure()){
                setModelError("structure");
                return;
            }



            System.out.println("Data importation processing...");

            saveNotes();

            System.out.println("Fin ");





            workbook.close();
        } catch (IOException e) {
            System.out.println("erreur : ");

            setModelError("");
        }
    }

    private void setModelError(String errorMsg){
        if (errorMsg!="")
            model.addAttribute("error",ERROR_MESSAGES.get(errorMsg) != null ?ERROR_MESSAGES.get(errorMsg)  : errorMsg);
        else
            model.addAttribute("error","Une erreur est intervenue. Veuillez reessayer");
    }



    private List<String> generatePrincipalHeader(){
        List<String> principalHeader =new ArrayList<>(Arrays.asList("ID","CNE", "NOM", "PRENOM")) ;

        for (Element elt:module.getElements()) {
            principalHeader.add(elt.getNom());
        }

        principalHeader.add("Moyenne");
        principalHeader.add("Validation");

        return  principalHeader;
    }

    public static boolean isExcelFormat(MultipartFile file){
        return EXCEL_FORMAT.equals(file.getContentType());
    }

    private boolean checkFileStructure(){

        boolean correct = true;

        List<String> headers = null;

        List<String> excelHeader1 = new ArrayList<>();
        List<String> excelHeader2 = new ArrayList<>();
        List<String> excelHeader3 = new ArrayList<>();

        /** Recuperation des deux premieres lignes
         du fichier. Nous allons récupérer le module le nom du prof etc..*/

        Row row1 = sheet.getRow(0);
        Row row2 = sheet.getRow(1);
        Row row3 = sheet.getRow(3);

        excelHeader1.add(row1.getCell(0).getStringCellValue());
        excelHeader1.add(row1.getCell(2).getStringCellValue());
        excelHeader1.add(row1.getCell(4).getStringCellValue());

        excelHeader2.add(row2.getCell(0).getStringCellValue());
        excelHeader2.add(row2.getCell(2).getStringCellValue());
        excelHeader2.add(row2.getCell(4).getStringCellValue());

        correct = correct && checkFileHeaders(HEADER1,excelHeader1);

        if (correct) {
            correct = checkFileHeaders(HEADER2, excelHeader2);
        }

        if (correct) {

            Iterator<Cell> row3Iterator = row3.cellIterator();

            while (row3Iterator.hasNext()){
                Cell currentCell=row3Iterator.next();
                excelHeader3.add(currentCell.getStringCellValue());
            }

            headers = generatePrincipalHeader();

            correct = checkFileHeaders(headers,excelHeader3);

        }
        return  correct;
    }

    private boolean checkSchoolYear(){
        Row row = sheet.getRow(0);
        int today =  Year.now().getValue();
        String year = row.getCell(5).getStringCellValue();

        String normalYear = today-1+"/"+today;

        return  normalYear.equals(year);
    }


    private boolean checkNotes(){
        boolean correct = true;


        Iterator<Row> rowIterator = sheet.rowIterator();


        final double maxNoteValue = 20;
        final int moduleFirstElementExcelPosition = 4;
        final int studentDataIndex = 3;

        int numberOfElementsOfModule = getModuleElementsNumber(module);



        int counter = 0;
        while (rowIterator.hasNext()){

            if (counter<studentDataIndex) {
                counter++;
                rowIterator.next();
                continue;

            }
            Row row = rowIterator.next();
            for (int i = moduleFirstElementExcelPosition; i < moduleFirstElementExcelPosition+numberOfElementsOfModule; i++) {
                Cell cell = row.getCell(i);

                double note =  cell.getNumericCellValue();

                if (note > maxNoteValue){
                    correct = false;
                    break;
                }

            }

            counter++;
        }


        return correct;
    }

    private boolean checkFormuleMoyenne(int rowNumber){


        int numberOfElements = getModuleElementsNumber(module);

        String formule = "SUM(E"+rowNumber+":"+ALPHABETS[numberOfElements-1]+rowNumber+")/"+numberOfElements;

        System.out.println("normal formula : "+formule);
        System.out.println("excel formula : "+getMoyenneFormulaFromExcelByRow(rowNumber));


        return  formule.equals(getMoyenneFormulaFromExcelByRow(rowNumber));
    }

    public String getMoyenneFormulaFromExcelByRow(int rowNumber){
        Row row = sheet.getRow(rowNumber);
        short lastCellNum = row.getLastCellNum();
        Cell moyenneCell = getCellFromExcel(rowNumber,lastCellNum-2);

        return moyenneCell.getCellFormula();
    }

    public String getValidationFormulaFromExcelByRow(int rowNumber){
        Row row = sheet.getRow(rowNumber);
        int lastCellNum = row.getLastCellNum();
        Cell validationCell = getCellFromExcel(rowNumber,lastCellNum-1);
        return validationCell.getCellFormula();
    }

    private boolean checkFormuleValidation(int rowNumber){
        Row row = sheet.getRow(rowNumber);
        int lastCellNum = row.getLastCellNum();

        Cell moyenneCell = row.getCell(lastCellNum-2);
        CellAddress adresse = moyenneCell.getAddress();

        String formule ="IF(" +adresse+">=10,\"V\",\"R\")";

        System.out.println(formule);
        System.out.println(getValidationFormulaFromExcelByRow(rowNumber));

        return getValidationFormulaFromExcelByRow(rowNumber).equals(formule);
    }

    public boolean checkFormules(){
        boolean correct = true;


        Iterator<Row> rowIterator = sheet.rowIterator();

        final int studentDataIndex = 3;

        int counter = 0;
        while (rowIterator.hasNext()){

            if (counter<studentDataIndex) {
                counter++;
                rowIterator.next();
                continue;
            }

            Row row = rowIterator.next();

            int rowNum = row.getRowNum();

            if (!checkFormuleMoyenne(rowNum)) return false;
           if(!checkFormuleValidation(rowNum)) return false;

            counter++;
        }


        return correct;
    }

    private boolean checkEnseignant(){
        String fullname = getExcelEnseignantName();

        String[] composedName = fullname.split(" ");
        String nom = composedName[composedName.length-1].toLowerCase();

        return enseignantService.findByNom(nom) != null;
    }

    private boolean checkModuleEnsignant(){
       Enseignant enseignant = module.getEnseignant();
       if (enseignant==null)
           return false;

       String fullname = enseignant.getPrenom() + " " + enseignant.getNom();

       return getExcelEnseignantName().equals(fullname);
    }

    private boolean checkModuleNiveau(){
        String alias = module.getNiveau().getAlias();
        if (alias==null)
            return false;
        return getExcelNiveau().equals(alias);
    }

    private String getExcelModuleName(){
        return getCellFromExcel(0,1).getStringCellValue();
    }

    private String getExcelEnseignantName(){
       return getCellFromExcel(1,1).getStringCellValue();
    }

    private String getExcelNiveau(){
       return getCellFromExcel(1,5).getStringCellValue();
    }

    private Module getModule(String moduleName,ModuleService moduleService){
        return moduleService.moduleByTitre(moduleName);
    }

    private int getModuleElementsNumber(Module module){
        List<Element> elements = module.getElements();
        return elements.size();
    }

    private String getExamSessionExcel(){
        return getCellFromExcel(1,3).getStringCellValue();
    }

    private String getSemestreExcel(){
        return getCellFromExcel(0,3).getStringCellValue();
    }

    private boolean checkFileHeaders(List<String> headers,List<String> excelHeaders){
        boolean correct=true;
        for(int i=0;i<headers.size();i++){
            if(!headers.get(i).equals(excelHeaders.get(i))){
                correct =  false;
                break;
            }
        }
        return correct;
    }




    // Saving notes to database

    private void saveNotes(){

        Iterator<Row> rowIterator = sheet.rowIterator();

        int counter = 0;
        final int studentDataIndex = 3;
        int moduleFirstElementExcelPosition = 4;
        int numberOfElementsOfModule = getModuleElementsNumber(module);

        while (rowIterator.hasNext()){
            if (counter<studentDataIndex){
                rowIterator.next();
                counter++;
                continue;
            }
            Row row = rowIterator.next();

            for (int i=moduleFirstElementExcelPosition; i < moduleFirstElementExcelPosition+numberOfElementsOfModule; i++) {

                System.out.println(row.getCell(i));

                double note = row.getCell(i).getNumericCellValue();

                Element element = module.getElements().get(i-moduleFirstElementExcelPosition);

                int lastCellNum = row.getLastCellNum();
                String validation = row.getCell(lastCellNum-1).getStringCellValue();

                long idEtd =(long)row.getCell(0).getNumericCellValue();

                double moyenne = row.getCell(lastCellNum-2).getNumericCellValue();

                /*System.out.println("moyenne "+moyenne+" idEdt "+idEtd+" valiadtion : "+validation+" element "+element+ " note"+note);*/

               if(!saveElementNoteForEtudiant(idEtd,element,note,validation)){
                   setModelError("");
                   return;
               }
               if (!saveNoteModuleEtudiant(idEtd,moyenne,validation)){
                   setModelError("");
                   return;
               }

            }

            // boolean contains = IntStream.of(a).anyMatch(x -> x == 4);


            counter++;

        }

    }

    private boolean saveElementNoteForEtudiant(Long idEtudiant,Element elt,double note, String validation){
        boolean isSaved = true;

        Etudiant etd = etudiantService.getEtudiant(idEtudiant);

        InscriptionAnnuelle inscriptionAnnuelle = inscriptionAnnuelleService.findByEtudiant(etd);

        if (!inscriptionAnnuelle.getNiveau().getAlias().equals(getExcelNiveau())) {
            setModelError("Etudiant n'appartient pas au niveau");
            return false;
        }

        InscriptionMatiere inscriptionMatiere = inscriptionMatiereService.findByElementAndInscriptionAnnuelle(elt,inscriptionAnnuelle);

        inscriptionMatiere.setValidation(validation);

        if (getExamSessionExcel().equals("Normale")){
            inscriptionMatiere.setNoteSN(note);
        }

        if(getExamSessionExcel().equals("Rattrapage")){
            inscriptionMatiere.setNoteSR(note);
        }

        InscriptionMatiere inscm =  inscriptionMatiereService.save(inscriptionMatiere);

        if (inscm==null){
            isSaved = false;
        }

        return isSaved;
    }

    private  boolean saveNoteModuleEtudiant(Long idEtudiant, double moyenne,String validation){
        boolean isSaved = true;

        Etudiant etd = etudiantService.getEtudiant(idEtudiant);

        InscriptionAnnuelle inscriptionAnnuelle = inscriptionAnnuelleService.findByEtudiant(etd);

        InscriptionModule inscriptionModule = inscriptionModuleService.findByInscriptionAnnuelle(inscriptionAnnuelle);

        inscriptionModule.setValidation(validation);

        if (getExamSessionExcel().equals("Normale")){
            inscriptionModule.setNoteSN(moyenne);
        }

        if(getExamSessionExcel().equals("Rattrapage")){
            inscriptionModule.setNoteSR(moyenne);
        }

        if (inscriptionModuleService.save(inscriptionModule)==null)
            isSaved = false;

        return isSaved;

    }



    private Cell getCellFromExcel(int rowIndex,int cellIndex){
        return sheet.getRow(rowIndex).getCell(cellIndex);
    }
}
