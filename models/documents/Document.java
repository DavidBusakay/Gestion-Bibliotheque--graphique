package models.documents;

public abstract class Document implements Empruntable {
  
  protected String titre;
  protected int anneePublication;
  protected boolean disponible;
  protected String typeDocument;

  public Document(String titre, int anneePublication, String typeDocument) {
    this.titre = titre;
    this.anneePublication = anneePublication;
    this.disponible = true;
    this.typeDocument = typeDocument;
  }

  // Polymorphisme
  public abstract double calculerPenalite(int joursRetard);

  public String getTitre() {
    return this.titre;
  }

  public int getAnneePublication() {
    return this.anneePublication;
  }

  public String getTypeDocument() {
    return this.typeDocument;
  }

  public boolean estDisponible() {
    return this.disponible;
  }
  
  @Override
  public void emprunter() {
    this.disponible = false;
  }

  @Override
  public void retourner() {
    this.disponible = true;
  }

  public String toStringDetails() {
    String etat = estDisponible() ? "Disponible" : "Emprunté";
    return "[" + typeDocument + "] " + titre + " (" + anneePublication + ") - " + etat;
  }
}
