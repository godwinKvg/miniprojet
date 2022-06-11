package com.ensah.core.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExcellFileRowObject {

    private String cne,nom,prenom,type;
    private Long id_niveau,id_etudiant;
}
