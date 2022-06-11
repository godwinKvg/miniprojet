package com.ensah.core.bo;


import com.ensah.core.web.models.UserAndAccountInfos;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Entity
public class Journal {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "event")
    String evenement;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEvenement() {
        return evenement;
    }


    public void setEvenement(String nom,String prenom,String cne,String nom2,String prenom2,String cne2,HttpSession session) {

        UserAndAccountInfos userInfo = (UserAndAccountInfos) session.getAttribute("userInfo");
          this.evenement="L'etudiant "+nom+" "+prenom+" de cne  "+cne+".   a ete modifie par "+userInfo.getNom()+" "+userInfo.getPrenom()+
                  "par l'import automatique du fichier.Ces nouvelles informati" +
                  "ons sont  nom:"+nom2+" et de prenom:"+prenom2+" avec pour cne:"+cne2+"  a la date suivante: "+new Date().toString();
    }
}
