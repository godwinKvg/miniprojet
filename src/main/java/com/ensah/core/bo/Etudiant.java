package com.ensah.core.bo;


import java.util.*;

import javax.persistence.*;


/**
 * Represente un Etudiant.
 * 
 * Un enseignant est un cas spéciale de l'Etudiant
 * 
 * @author T. BOUDAA
 *
 */

@Entity
@PrimaryKeyJoinColumn(name = "idEtudiant")
public class Etudiant extends Utilisateur {

	private String cne;

	private Date dateNaissance;


	@Transient
	Long idNiveauTemporaire;

	public Long getIdNiveauTemporaire() {
		return idNiveauTemporaire;
	}

	public void setIdNiveauTemporaire(Long idNiveauTemporaire) {
		this.idNiveauTemporaire = idNiveauTemporaire;
	}

	@OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL , targetEntity = InscriptionAnnuelle.class, fetch = FetchType.EAGER)
	private List<InscriptionAnnuelle> inscriptions;


	public String getCne() {
		return cne;
	}

	public void setCne(String cne) {
		this.cne = cne;
	}

	public Date getDateNaissance() {
		return dateNaissance;
	}

	public void setDateNaissance(Date  dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public List<InscriptionAnnuelle> getInscriptions() {
		return inscriptions;
	}

	public void setInscriptions(List<InscriptionAnnuelle> inscriptions) {
		this.inscriptions = inscriptions;
	}

	@Override
	public String toString() {
		System.out.println(super.toString());
		return "Etudiant [cne=" + cne + ", dateNaissance=" + dateNaissance + ", inscriptions=" + inscriptions + "]";
	}


	

}