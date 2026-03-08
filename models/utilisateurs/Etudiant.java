package models.utilisateurs;

public class Etudiant extends Utilisateur {
  
  public Etudiant(String nom) {
    super(nom);
  }

  @Override
  public int getLimiteEmprunt() {
    return 5;
  }

  @Override
  public int getDureeMaximale() {
    return 14;
  }
}
