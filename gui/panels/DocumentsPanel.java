package gui.panels;

import models.Bibliotheque;
import models.documents.*;
import gui.BibliothequeGUI;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class DocumentsPanel extends JPanel {
  private BibliothequeGUI parent;
  private Bibliotheque bibliotheque;
  private JTable tableDocuments;
  private DefaultTableModel tableModel;
  private JTextField txtRecherche;
  
  public DocumentsPanel(BibliothequeGUI parent, Bibliotheque bibliotheque) {
    this.parent = parent;
    this.bibliotheque = bibliotheque;
    
    setLayout(new BorderLayout(10, 10));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    // Title
    JLabel lblTitre = new JLabel("Gestion des Documents");
    lblTitre.setFont(new Font("Arial", Font.BOLD, 18));
    add(lblTitre, BorderLayout.NORTH);
    
    // Création de la table
    String[] columnNames = {"Type", "Titre", "Année", "Auteur/Édition", "Statut"};
    tableModel = new DefaultTableModel(columnNames, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    
    tableDocuments = new JTable(tableModel);
    tableDocuments.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    JScrollPane scrollPane = new JScrollPane(tableDocuments);
    add(scrollPane, BorderLayout.CENTER);
    
    // Panel du bas avec des boutons
    JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
    
    // Pannel de recherche
    JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    searchPanel.add(new JLabel("Rechercher:"));
    txtRecherche = new JTextField(20);
    searchPanel.add(txtRecherche);
    JButton btnRechercher = new JButton("Rechercher");
    btnRechercher.addActionListener(e -> rechercher());
    searchPanel.add(btnRechercher);
    JButton btnEffacerRecherche = new JButton("Afficher tout");
    btnEffacerRecherche.addActionListener(e -> rafraichir());
    searchPanel.add(btnEffacerRecherche);
    
    bottomPanel.add(searchPanel, BorderLayout.NORTH);
    
    // Boutons dans le panel
    JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton btnAjouter = new JButton("Ajouter un document");
    btnAjouter.addActionListener(e -> parent.afficherDialogueAjoutDocument());
    buttonsPanel.add(btnAjouter);
    
    JButton btnSupprimer = new JButton("Supprimer");
    btnSupprimer.addActionListener(e -> supprimerDocument());
    buttonsPanel.add(btnSupprimer);
    
    JButton btnRafraichir = new JButton("Rafraîchir");
    btnRafraichir.addActionListener(e -> rafraichir());
    buttonsPanel.add(btnRafraichir);
    
    bottomPanel.add(buttonsPanel, BorderLayout.SOUTH);
    
    add(bottomPanel, BorderLayout.SOUTH);
    
    rafraichir();
  }
  
  public void rafraichir() {
    txtRecherche.setText("");
    tableModel.setRowCount(0);
    
    for (Document doc : bibliotheque.getListeDocuments()) {
      Object[] row = new Object[5];
      row[0] = doc.getTypeDocument();
      row[1] = doc.getTitre();
      row[2] = doc.getAnneePublication();
      
      if (doc instanceof Livre) {
        row[3] = ((Livre) doc).getAuteur();
      } else if (doc instanceof Revue) {
        row[3] = "Édition #" + ((Revue) doc).getNumeroEdition();
      }
      
      row[4] = doc.estDisponible() ? "Disponible" : "Emprunté";
      
      tableModel.addRow(row);
    }
  }
  
  private void rechercher() {
    String recherche = txtRecherche.getText().trim();
    if (recherche.isEmpty()) {
      rafraichir();
      return;
    }
    
    tableModel.setRowCount(0);
    
    for (Document doc : bibliotheque.getListeDocuments()) {
      if (doc.getTitre().toLowerCase().contains(recherche.toLowerCase())) {
        Object[] row = new Object[5];
        row[0] = doc.getTypeDocument();
        row[1] = doc.getTitre();
        row[2] = doc.getAnneePublication();
        
        if (doc instanceof Livre) {
          row[3] = ((Livre) doc).getAuteur();
        } else if (doc instanceof Revue) {
          row[3] = "Édition #" + ((Revue) doc).getNumeroEdition();
        }
        
        row[4] = doc.estDisponible() ? "Disponible" : "Emprunté";
        
        tableModel.addRow(row);
      }
    }
  }
  
  private void supprimerDocument() {
    int selectedRow = tableDocuments.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(this, "Veuillez sélectionner un document à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    int confirm = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer ce document?", "Confirmer", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
      bibliotheque.getListeDocuments().remove(selectedRow);
      rafraichir();
      parent.rafraichirTous();
    }
  }
}
