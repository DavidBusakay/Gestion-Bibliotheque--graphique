package models.documents;

public class Livre extends Document {
  
  private String auteur;

  public Livre(String titre, int annee, String auteur) {
    super(titre, annee, "Livre");
    this.auteur = auteur;
  }

  public String getAuteur() {
    return this.auteur;
  }

  @Override
  public double calculerPenalite(int joursRetard) {
    return joursRetard * 0.5;
  }

  @Override
  public String toStringDetails() {
    return super.toStringDetails() + " - Auteur: " + auteur;
  }
}
