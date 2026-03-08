package models;
import java.util.ArrayList;
import java.util.List;

import models.documents.Document;
import models.documents.Emprunt;
import models.utilisateurs.Utilisateur;

public class Bibliotheque {
  
  public ArrayList<Document> listeDocuments;
  public ArrayList<Utilisateur> listeUtilisateurs;
  public ArrayList<Emprunt> listeEmprunts;

  public Bibliotheque() {
    this.listeDocuments = new ArrayList<>();
    this.listeUtilisateurs = new ArrayList<>();
    this.listeEmprunts = new ArrayList<>();
  }

  public ArrayList<Document> getListeDocuments() {
    return listeDocuments;
  }

  public ArrayList<Utilisateur> getListeUtilisateurs() {
    return listeUtilisateurs;
  }

  public ArrayList<Emprunt> getListeEmprunts() {
    return listeEmprunts;
  }

  public void ajouterDocument(Document document) {
    listeDocuments.add(document);
  }

  public void ajouterUtilisateur(Utilisateur utilisateur) {
    listeUtilisateurs.add(utilisateur);
  }

  public String[] getTousDocumentsAsArray() {
    String[] result = new String[listeDocuments.size()];
    for (int i = 0; i < listeDocuments.size(); i++) {
      result[i] = listeDocuments.get(i).toStringDetails();
    }
    return result;
  }

  public String[] getTousUtilisateursAsArray() {
    String[] result = new String[listeUtilisateurs.size()];
    for (int i = 0; i < listeUtilisateurs.size(); i++) {
      Utilisateur u = listeUtilisateurs.get(i);
      result[i] = "[" + u.getId() + "] " + u.getNom() + " - " + u.getClass().getSimpleName() + 
                  " - Emprunts: " + u.getNombreEmpruntsActifs() + "/" + u.getLimiteEmprunt();
    }
    return result;
  }

  public String[] rechercherDocumentsAsArray(String titreRecherche) {
    List<String> resultats = new ArrayList<>();
    for (Document d : listeDocuments) {
      if (d.getTitre().toLowerCase().contains(titreRecherche.toLowerCase())) {
        resultats.add(d.toStringDetails());
      }
    }
    return resultats.toArray(new String[0]);
  }

  public String[] getEmpruntsActifsAsArray() {
    List<String> resultats = new ArrayList<>();
    for (Emprunt e : listeEmprunts) {
      if (!e.isRetourne()) {
        StringBuilder sb = new StringBuilder();
        sb.append("Document: ").append(e.getDocument().getTitre()).append("\n");
        sb.append("Emprunteur: ").append(e.getUtilisateur().getNom()).append(" (ID: ").append(e.getUtilisateur().getId()).append(")\n");
        sb.append("Date d'emprunt: ").append(e.getDateEmprunt()).append("\n");
        sb.append("Date d'échéance: ").append(e.getDateEcheance()).append("\n");
        if (e.estEnRetard()) {
          sb.append("*** EN RETARD: ").append(e.getJoursRetard()).append(" jours ***");
        } else {
          sb.append("Jours restants: ").append(e.getJoursRestants());
        }
        resultats.add(sb.toString());
      }
    }
    return resultats.toArray(new String[0]);
  }

  public String[] getEmpruntsUtilisateurAsArray(Utilisateur utilisateur) {
    List<String> resultats = new ArrayList<>();
    List<Emprunt> emprunts = utilisateur.getEmprunts();
    for (Emprunt e : emprunts) {
      StringBuilder sb = new StringBuilder();
      sb.append("- ").append(e.getDocument().getTitre());
      String statut = e.isRetourne() ? "RETOURNÉ" : "ACTIF";
      if (e.estEnRetard()) {
        statut += " - EN RETARD (" + e.getJoursRetard() + " jours)";
      }
      sb.append(" [").append(statut).append("]\n");
      sb.append("  Échéance: ").append(e.getDateEcheance());
      resultats.add(sb.toString());
    }
    return resultats.toArray(new String[0]);
  }

  public String getStatistiquesAsString() {
    StringBuilder sb = new StringBuilder();
    sb.append("=== STATISTIQUES DE LA BIBLIOTHÈQUE ===\n\n");
    sb.append("Total des documents: ").append(listeDocuments.size()).append("\n");
    sb.append("Total des utilisateurs: ").append(listeUtilisateurs.size()).append("\n");
    
    int documentsDisponibles = 0;
    int documentsEmpruntes = 0;
    for (Document d : listeDocuments) {
      if (d.estDisponible()) {
        documentsDisponibles++;
      } else {
        documentsEmpruntes++;
      }
    }
    sb.append("Documents disponibles: ").append(documentsDisponibles).append("\n");
    sb.append("Documents empruntés: ").append(documentsEmpruntes).append("\n");
    
    int empruntsActifs = 0;
    int empruntsEnRetard = 0;
    for (Emprunt e : listeEmprunts) {
      if (!e.isRetourne()) {
        empruntsActifs++;
        if (e.estEnRetard()) {
          empruntsEnRetard++;
        }
      }
    }
    sb.append("Emprunts actifs: ").append(empruntsActifs).append("\n");
    sb.append("Emprunts en retard: ").append(empruntsEnRetard);
    
    return sb.toString();
  }

  public String effectuerEmpruntGUI(Utilisateur utilisateur, Document document) {
    try {
      if (!document.estDisponible()) {
        throw new Exception("Le document '" + document.getTitre() + "' n'est pas disponible.");
      }

      if (!utilisateur.peutEmprunter()) {
        throw new Exception(utilisateur.getNom() + " a atteint sa limite d'emprunt (" + utilisateur.getLimiteEmprunt() + " documents).");
      }

      int dureeMaximale = utilisateur.getDureeMaximale();
      Emprunt emprunt = new Emprunt(document, utilisateur, dureeMaximale);
      
      document.emprunter();
      
      listeEmprunts.add(emprunt);
      utilisateur.ajouterEmprunt(emprunt);
      
      StringBuilder sb = new StringBuilder();
      sb.append("=== EMPRUNT EFFECTUÉ AVEC SUCCÈS ===\n");
      sb.append("Document: ").append(document.getTitre()).append("\n");
      sb.append("Emprunteur: ").append(utilisateur.getNom()).append(" (ID: ").append(utilisateur.getId()).append(")\n");
      sb.append("Date d'emprunt: ").append(emprunt.getDateEmprunt()).append("\n");
      sb.append("Date d'échéance: ").append(emprunt.getDateEcheance()).append("\n");
      sb.append("Durée: ").append(dureeMaximale).append(" jours\n");
      sb.append("Emprunts restants: ").append(utilisateur.getEmpruntsRestants());
      return sb.toString();
    } catch (Exception e) {
      return "*** ERREUR: " + e.getMessage() + " ***";
    }
  }

  public String retournerDocumentGUI(Utilisateur utilisateur, Document document) {
    try {
      Emprunt empruntTrouve = null;
      for (Emprunt e : listeEmprunts) {
        if (e.getDocument().equals(document) && 
            e.getUtilisateur().equals(utilisateur) && 
            !e.isRetourne()) {
          empruntTrouve = e;
          break;
        }
      }

      if (empruntTrouve == null) {
        throw new Exception("Aucun emprunt actif trouvé pour ce document par cet utilisateur.");
      }

      long joursRetard = empruntTrouve.getJoursRetard();
      StringBuilder sb = new StringBuilder();
      
      if (joursRetard > 0) {
        double penalite = document.calculerPenalite((int)joursRetard);
        sb.append("*** ATTENTION: Document en retard de ").append(joursRetard).append(" jours! ***\n");
        sb.append("Pénalité calculée: ").append(penalite).append(" euros\n");
      }

      empruntTrouve.setRetorne(true);
      document.retourner();
      utilisateur.supprimerEmprunt(empruntTrouve);

      sb.append("=== DOCUMENT RETOURNÉ AVEC SUCCÈS ===\n");
      sb.append("Document: ").append(document.getTitre()).append("\n");
      sb.append("Emprunteur: ").append(utilisateur.getNom());
      return sb.toString();
    } catch (Exception e) {
      return "*** ERREUR: " + e.getMessage() + " ***";
    }
  }
}
