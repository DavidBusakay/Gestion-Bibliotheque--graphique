package models.utilisateurs;

import java.util.ArrayList;
import java.util.List;

import models.documents.Emprunt;

public abstract class Utilisateur {

  protected int id;
  protected String nom;
  protected List<Emprunt> emprunts;

  private static int compteurId = 0;

  public Utilisateur(String nom) {
    this.nom = nom;
    this.emprunts = new ArrayList<>();
    compteurId++;
    this.id = compteurId;
  }

  public abstract int getLimiteEmprunt();
  public abstract int getDureeMaximale();

  public int getId() {
    return this.id;
  }

  public String getNom() {
    return this.nom;
  }

  public List<Emprunt> getEmprunts() {
    return this.emprunts;
  }

  public void ajouterEmprunt(Emprunt emprunt) {
    this.emprunts.add(emprunt);
  }

  public void supprimerEmprunt(Emprunt emprunt) {
    this.emprunts.remove(emprunt);
  }

  public int getNombreEmpruntsActifs() {
    int count = 0;
    for (Emprunt e : emprunts) {
      if (!e.isRetourne()) {
        count++;
      }
    }
    return count;
  }

  public boolean peutEmprunter() {
    return getNombreEmpruntsActifs() < getLimiteEmprunt();
  }

  public int getEmpruntsRestants() {
    return getLimiteEmprunt() - getNombreEmpruntsActifs();
  }

  public List<Emprunt> getEmpruntsEnRetard() {
    List<Emprunt> retards = new ArrayList<>();
    for (Emprunt e : emprunts) {
      if (e.estEnRetard()) {
        retards.add(e);
      }
    }
    return retards;
  }
}
