package models.documents;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import models.utilisateurs.Utilisateur;

public class Emprunt {
  
  private Document document;
  private Utilisateur utilisateur;
  private LocalDate dateEmprunt;
  private LocalDate dateEcheance;
  private boolean retourne;

  public Emprunt(Document document, Utilisateur utilisateur, int dureeMaximale) {
    this.document = document;
    this.utilisateur = utilisateur;
    this.dateEmprunt = LocalDate.now();
    this.dateEcheance = dateEmprunt.plusDays(dureeMaximale);
    this.retourne = false;
  }

  public Document getDocument() {
    return this.document;
  }

  public Utilisateur getUtilisateur() {
    return this.utilisateur;
  }

  public LocalDate getDateEmprunt() {
    return this.dateEmprunt;
  }

  public LocalDate getDateEcheance() {
    return this.dateEcheance;
  }

  public boolean isRetourne() {
    return this.retourne;
  }

  public void setRetorne(boolean retourne) {
    this.retourne = retourne;
  }

  public long getJoursRetard() {
    if (retourne) {
      return 0;
    }
    LocalDate today = LocalDate.now();
    if (today.isAfter(dateEcheance)) {
      return ChronoUnit.DAYS.between(dateEcheance, today);
    }
    return 0;
  }

  public boolean estEnRetard() {
    return !retourne && LocalDate.now().isAfter(dateEcheance);
  }

  public int getJoursRestants() {
    if (retourne) {
      return 0;
    }
    LocalDate today = LocalDate.now();
    if (today.isBefore(dateEcheance)) {
      return (int) ChronoUnit.DAYS.between(today, dateEcheance);
    }
    return 0;
  }
}
