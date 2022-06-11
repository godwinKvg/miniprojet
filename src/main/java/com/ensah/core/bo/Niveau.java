package com.ensah.core.bo;

import java.util.*;

import javax.persistence.*;

@Entity
public class Niveau {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idNiveau;

	private String alias;

	private String titre;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "niveauSuivan", referencedColumnName = "idNiveau")
	private Niveau niveauSuivant;
	@OneToMany(mappedBy = "niveau" , cascade = CascadeType.ALL, targetEntity = Module.class)
	private List<Module> modules;

	@OneToMany(mappedBy = "niveau" , cascade = CascadeType.ALL, targetEntity = InscriptionAnnuelle.class)
	private List<InscriptionAnnuelle> inscriptions;

	public Niveau getNiveauSuivant() {
		return niveauSuivant;
	}

	public void setNiveauSuivant(Niveau niveauSuivant) {
		this.niveauSuivant = niveauSuivant;
	}

	@ManyToOne
	@JoinColumn(name="idFiliere")
	private Filiere filiere;

	public Long getIdNiveau() {
		return idNiveau;
	}

	public void setIdNiveau(Long idNiveau) {
		this.idNiveau = idNiveau;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public List<Module> getModules() {
		return modules;
	}

	public void setModules(List<Module> modules) {
		this.modules = modules;
	}

	public List<InscriptionAnnuelle> getInscriptions() {
		return inscriptions;
	}

	public void setInscriptions(List<InscriptionAnnuelle> inscriptions) {
		this.inscriptions = inscriptions;
	}

	public Filiere getFiliere() {
		return filiere;
	}

	public void setFiliere(Filiere filiere) {
		this.filiere = filiere;
	}

	
	
}