package gui.panels;

import models.Bibliotheque;
import models.utilisateurs.*;
import gui.BibliothequeGUI;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class UtilisateursPanel extends JPanel {
  private BibliothequeGUI parent;
  private Bibliotheque bibliotheque;
  private JTable tableUtilisateurs;
  private DefaultTableModel tableModel;
  
  public UtilisateursPanel(BibliothequeGUI parent, Bibliotheque bibliotheque) {
    this.parent = parent;
    this.bibliotheque = bibliotheque;
    
    setLayout(new BorderLayout(10, 10));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    // Title
    JLabel lblTitre = new JLabel("Gestion des Utilisateurs");
    lblTitre.setFont(new Font("Arial", Font.BOLD, 18));
    add(lblTitre, BorderLayout.NORTH);
    
    // Création de la table
    String[] columnNames = {"ID", "Nom", "Type", "Emprunts actifs", "Limite", "Durée max"};
    tableModel = new DefaultTableModel(columnNames, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    
    tableUtilisateurs = new JTable(tableModel);
    tableUtilisateurs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    JScrollPane scrollPane = new JScrollPane(tableUtilisateurs);
    add(scrollPane, BorderLayout.CENTER);
    
    // Panel du bas avec des boutons
    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    
    JButton btnAjouter = new JButton("Ajouter un utilisateur");
    btnAjouter.addActionListener(e -> parent.afficherDialogueAjoutUtilisateur());
    bottomPanel.add(btnAjouter);
    
    JButton btnSupprimer = new JButton("Supprimer");
    btnSupprimer.addActionListener(e -> supprimerUtilisateur());
    bottomPanel.add(btnSupprimer);
    
    JButton btnVoirEmprunts = new JButton("Voir mes emprunts");
    btnVoirEmprunts.addActionListener(e -> voirEmprunts());
    bottomPanel.add(btnVoirEmprunts);
    
    JButton btnRafraichir = new JButton("Rafraîchir");
    btnRafraichir.addActionListener(e -> rafraichir());
    bottomPanel.add(btnRafraichir);
    
    add(bottomPanel, BorderLayout.SOUTH);
    
    rafraichir();
  }
  
  public void rafraichir() {
    tableModel.setRowCount(0);
    
    for (Utilisateur user : bibliotheque.getListeUtilisateurs()) {
      Object[] row = new Object[6];
      row[0] = user.getId();
      row[1] = user.getNom();
      row[2] = user.getClass().getSimpleName();
      row[3] = user.getNombreEmpruntsActifs();
      row[4] = user.getLimiteEmprunt();
      row[5] = user.getDureeMaximale() + " jours";
      
      tableModel.addRow(row);
    }
  }
  
  private void supprimerUtilisateur() {
    int selectedRow = tableUtilisateurs.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(this, "Veuillez sélectionner un utilisateur à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    int confirm = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer cet utilisateur?", "Confirmer", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
      bibliotheque.getListeUtilisateurs().remove(selectedRow);
      rafraichir();
      parent.rafraichirTous();
    }
  }
  
  private void voirEmprunts() {
    int selectedRow = tableUtilisateurs.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(this, "Veuillez sélectionner un utilisateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    Utilisateur utilisateur = bibliotheque.getListeUtilisateurs().get(selectedRow);
    String[] emprunts = bibliotheque.getEmpruntsUtilisateurAsArray(utilisateur);
    
    StringBuilder sb = new StringBuilder();
    sb.append("=== EMPRUNTS DE ").append(utilisateur.getNom().toUpperCase()).append(" ===\n\n");
    
    if (emprunts.length == 0) {
      sb.append("Aucun emprunt.");
    } else {
      for (String emp : emprunts) {
        sb.append(emp).append("\n\n");
      }
    }
    
    JTextArea textArea = new JTextArea(sb.toString());
    textArea.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setPreferredSize(new Dimension(400, 300));
    
    JOptionPane.showMessageDialog(this, scrollPane, "Emprunts de " + utilisateur.getNom(), JOptionPane.INFORMATION_MESSAGE);
  }
}
