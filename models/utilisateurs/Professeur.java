package models.utilisateurs;

public class Professeur extends Utilisateur {
  
  public Professeur(String nom) {
    super(nom);
  }

  @Override
  public int getLimiteEmprunt() {
    return 20;
  }

  @Override
  public int getDureeMaximale() {
    return 90;
  }
}
