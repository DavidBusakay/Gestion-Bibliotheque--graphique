package models.documents;

public class Revue extends Document {
  
  private int numeroEdition;

  public Revue(String titre, int annee, int numero) {
    super(titre, annee, "Revue");
    this.numeroEdition = numero;
  }

  public int getNumeroEdition() {
    return this.numeroEdition;
  }

  @Override
  public double calculerPenalite(int joursRetard) {
    return joursRetard * 0.7;
  }

  @Override
  public String toStringDetails() {
    return super.toStringDetails() + " - Edition n°" + numeroEdition;
  }
}
