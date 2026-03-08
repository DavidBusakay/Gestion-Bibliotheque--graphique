package gui;

import models.Bibliotheque;
import models.documents.*;
import models.utilisateurs.*;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import gui.dialogsBox.AjouterDocumentDialog;
import gui.dialogsBox.AjouterUtilisateurDialog;
import gui.panels.DocumentsPanel;
import gui.panels.EmpruntsPanel;
import gui.panels.StatistiquesPanel;
import gui.panels.UtilisateursPanel;

public class BibliothequeGUI extends JFrame {
  private Bibliotheque bibliotheque;
  
  // Panels
  private DocumentsPanel documentsPanel;
  private UtilisateursPanel utilisateursPanel;
  private EmpruntsPanel empruntsPanel;
  private StatistiquesPanel statistiquesPanel;
  
  public BibliothequeGUI() {
    // Initialisation de la bibliothèque avec les données de test
    bibliotheque = new Bibliotheque();
    initialiserDonneesTest();
    
    // Configuration de la fenetre principale
    setTitle("Gestion de Bibliothèque");
    setSize(900, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    
    // Création d'une barre de menu
    creerMenuBar();
    
    // Création d'un panel principal avec onglets
    creerPanneauPrincipal();
    
    setVisible(true);
  }
  
  private void initialiserDonneesTest() {
    bibliotheque.ajouterDocument(new Livre("Apprendre Java pour débutants", 2026, "John Doe"));
    bibliotheque.ajouterDocument(new Revue("Science et Vie", 2023, 1250));
    bibliotheque.ajouterDocument(new Livre("Les bases de la programmation", 2022, "Jean-Michel"));
    bibliotheque.ajouterDocument(new Revue("L'Express", 2024, 1300));
    bibliotheque.ajouterUtilisateur(new Etudiant("Luc"));
    bibliotheque.ajouterUtilisateur(new Etudiant("Matthieu"));
    bibliotheque.ajouterUtilisateur(new Professeur("Professeur Efrem"));
  }
  
  private void creerMenuBar() {
    JMenuBar menuBar = new JMenuBar();
    
    // Menu Fichier
    JMenu fichierMenu = new JMenu("Fichier");
    JMenuItem quitterItem = new JMenuItem("Quitter");
    quitterItem.addActionListener(e -> System.exit(0));
    fichierMenu.add(quitterItem);
    
    // Menu Actions
    JMenu actionsMenu = new JMenu("Actions");
    
    JMenuItem ajouterDocItem = new JMenuItem("Ajouter un document");
    ajouterDocItem.addActionListener(e -> afficherDialogueAjoutDocument());
    actionsMenu.add(ajouterDocItem);
    
    JMenuItem ajouterUserItem = new JMenuItem("Ajouter un utilisateur");
    ajouterUserItem.addActionListener(e -> afficherDialogueAjoutUtilisateur());
    actionsMenu.add(ajouterUserItem);
    
    JMenuItem effectuerEmpruntItem = new JMenuItem("Effectuer un emprunt");
    effectuerEmpruntItem.addActionListener(e -> empruntsPanel.afficherDialogueEmprunt());
    actionsMenu.add(effectuerEmpruntItem);
    
    menuBar.add(fichierMenu);
    menuBar.add(actionsMenu);
    
    setJMenuBar(menuBar);
  }
  
  private void creerPanneauPrincipal() {
    documentsPanel = new DocumentsPanel(this, bibliotheque);
    utilisateursPanel = new UtilisateursPanel(this, bibliotheque);
    empruntsPanel = new EmpruntsPanel(this, bibliotheque);
    statistiquesPanel = new StatistiquesPanel(bibliotheque);
    
    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.addTab("Documents", documentsPanel);
    tabbedPane.addTab("Utilisateurs", utilisateursPanel);
    tabbedPane.addTab("Emprunts", empruntsPanel);
    tabbedPane.addTab("Statistiques", statistiquesPanel);
    
    add(tabbedPane);
  }
  
  public void afficherDialogueAjoutDocument() {
    AjouterDocumentDialog dialog = new AjouterDocumentDialog(this, bibliotheque);
    dialog.setVisible(true);
    documentsPanel.rafraichir();
  }
  
  public void afficherDialogueAjoutUtilisateur() {
    AjouterUtilisateurDialog dialog = new AjouterUtilisateurDialog(this, bibliotheque);
    dialog.setVisible(true);
    utilisateursPanel.rafraichir();
  }
  
  public Bibliotheque getBibliotheque() {
    return bibliotheque;
  }
  
  public void rafraichirTous() {
    documentsPanel.rafraichir();
    utilisateursPanel.rafraichir();
    empruntsPanel.rafraichir();
    statistiquesPanel.rafraichir();
  }
  
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception e) {
        e.printStackTrace();
      }
      new BibliothequeGUI();
    });
  }
}
