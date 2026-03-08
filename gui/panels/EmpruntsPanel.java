package gui.panels;

import models.Bibliotheque;
import models.documents.*;
import models.utilisateurs.*;
import gui.BibliothequeGUI;
import gui.dialogsBox.EmpruntDialog;

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

public class EmpruntsPanel extends JPanel {
  private BibliothequeGUI parent;
  private Bibliotheque bibliotheque;
  private JTable tableEmprunts;
  private DefaultTableModel tableModel;
  
  public EmpruntsPanel(BibliothequeGUI parent, Bibliotheque bibliotheque) {
    this.parent = parent;
    this.bibliotheque = bibliotheque;
    
    setLayout(new BorderLayout(10, 10));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    // Titre
    JLabel lblTitre = new JLabel("Gestion des Emprunts");
    lblTitre.setFont(new Font("Arial", Font.BOLD, 18));
    add(lblTitre, BorderLayout.NORTH);
    
    // Création de la table
    String[] columnNames = {"Document", "Emprunteur", "Date emprunt", "Date échéance", "Statut"};
    tableModel = new DefaultTableModel(columnNames, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    
    tableEmprunts = new JTable(tableModel);
    tableEmprunts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    JScrollPane scrollPane = new JScrollPane(tableEmprunts);
    add(scrollPane, BorderLayout.CENTER);
    
    // Panel du bas avec des boutons
    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    
    JButton btnEmprunter = new JButton("Effectuer un emprunt");
    btnEmprunter.addActionListener(e -> afficherDialogueEmprunt());
    bottomPanel.add(btnEmprunter);
    
    JButton btnRetourner = new JButton("Retourner un document");
    btnRetourner.addActionListener(e -> retournerDocument());
    bottomPanel.add(btnRetourner);
    
    JButton btnRafraichir = new JButton("Rafraîchir");
    btnRafraichir.addActionListener(e -> rafraichir());
    bottomPanel.add(btnRafraichir);
    
    add(bottomPanel, BorderLayout.SOUTH);
    
    rafraichir();
  }
  
  public void rafraichir() {
    tableModel.setRowCount(0);
    
    for (Emprunt emp : bibliotheque.getListeEmprunts()) {
      Object[] row = new Object[5];
      row[0] = emp.getDocument().getTitre();
      row[1] = emp.getUtilisateur().getNom() + " (ID: " + emp.getUtilisateur().getId() + ")";
      row[2] = emp.getDateEmprunt().toString();
      row[3] = emp.getDateEcheance().toString();
      
      if (emp.isRetourne()) {
        row[4] = "RETOURNÉ";
      } else if (emp.estEnRetard()) {
        row[4] = "EN RETARD (" + emp.getJoursRetard() + " jours)";
      } else {
        row[4] = "Actif (" + emp.getJoursRestants() + " jours restants)";
      }
      
      tableModel.addRow(row);
    }
  }
  
  public void afficherDialogueEmprunt() {
    EmpruntDialog dialog = new EmpruntDialog(parent, bibliotheque);
    dialog.setVisible(true);
    if (dialog.isConfirmed()) {
      rafraichir();
      parent.rafraichirTous();
    }
  }
  
  private void retournerDocument() {
    int selectedRow = tableEmprunts.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(this, "Veuillez sélectionner un emprunt à retourner.", "Erreur", JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    // Trouver l'emprunt actif correspondant
    int counter = -1;
    Emprunt selectedEmprunt = null;
    for (Emprunt emp : bibliotheque.getListeEmprunts()) {
      if (!emp.isRetourne()) {
        counter++;
        if (counter == selectedRow) {
          selectedEmprunt = emp;
          break;
        }
      }
    }
    
    if (selectedEmprunt == null) {
      JOptionPane.showMessageDialog(this, "Emprunt introuvable.", "Erreur", JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    Utilisateur utilisateur = selectedEmprunt.getUtilisateur();
    Document document = selectedEmprunt.getDocument();
    
    String result = bibliotheque.retournerDocumentGUI(utilisateur, document);
    
    JTextArea textArea = new JTextArea(result);
    textArea.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setPreferredSize(new Dimension(350, 200));
    
    JOptionPane.showMessageDialog(this, scrollPane, "Retour de document", JOptionPane.INFORMATION_MESSAGE);
    
    rafraichir();
    parent.rafraichirTous();
  }
}
