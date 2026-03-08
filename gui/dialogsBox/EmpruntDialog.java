package gui.dialogsBox;

import models.Bibliotheque;
import models.documents.*;
import models.utilisateurs.*;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class EmpruntDialog extends JDialog {
  private Bibliotheque bibliotheque;
  private JComboBox<String> cmbUtilisateur;
  private JComboBox<String> cmbDocument;
  private boolean confirmed = false;
  
  public EmpruntDialog(JFrame parent, Bibliotheque bibliotheque) {
    super(parent, "Effectuer un emprunt", true);
    this.bibliotheque = bibliotheque;
    
    setSize(450, 200);
    setLocationRelativeTo(parent);
    setLayout(new BorderLayout(10, 10));
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    
    // Panel principal
    JPanel mainPanel = new JPanel(new GridBagLayout());
    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.anchor = GridBagConstraints.WEST;
    
    // Utilisateur
    gbc.gridx = 0;
    gbc.gridy = 0;
    mainPanel.add(new JLabel("Utilisateur:"), gbc);
    
    cmbUtilisateur = new JComboBox<>();
    for (Utilisateur u : bibliotheque.getListeUtilisateurs()) {
      cmbUtilisateur.addItem("[" + u.getId() + "] " + u.getNom() + " - " + u.getClass().getSimpleName());
    }
    gbc.gridx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    mainPanel.add(cmbUtilisateur, gbc);
    
    // Document
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.NONE;
    mainPanel.add(new JLabel("Document:"), gbc);
    
    cmbDocument = new JComboBox<>();
    for (Document d : bibliotheque.getListeDocuments()) {
      String status = d.estDisponible() ? " (Disponible)" : " (Emprunté)";
      cmbDocument.addItem(d.getTitre() + status);
    }
    gbc.gridx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    mainPanel.add(cmbDocument, gbc);
    
    add(mainPanel, BorderLayout.CENTER);
    
    // Boutons
    JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton btnEmprunter = new JButton("Emprunter");
    btnEmprunter.addActionListener(e -> emprunter());
    buttonsPanel.add(btnEmprunter);
    
    JButton btnAnnuler = new JButton("Annuler");
    btnAnnuler.addActionListener(e -> dispose());
    buttonsPanel.add(btnAnnuler);
    
    add(buttonsPanel, BorderLayout.SOUTH);
  }
  
  private void emprunter() {
    int userIndex = cmbUtilisateur.getSelectedIndex();
    int docIndex = cmbDocument.getSelectedIndex();
    
    if (userIndex == -1 || docIndex == -1) {
      JOptionPane.showMessageDialog(this, "Veuillez sélectionner un utilisateur et un document.", "Erreur", JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    Utilisateur utilisateur = bibliotheque.getListeUtilisateurs().get(userIndex);
    Document document = bibliotheque.getListeDocuments().get(docIndex);
    
    String result = bibliotheque.effectuerEmpruntGUI(utilisateur, document);
    
    JTextArea textArea = new JTextArea(result);
    textArea.setEditable(false);
    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setPreferredSize(new Dimension(350, 200));
    
    JOptionPane.showMessageDialog(this, scrollPane, "Résultat de l'emprunt", JOptionPane.INFORMATION_MESSAGE);
    
    confirmed = true;
    dispose();
  }
  
  public boolean isConfirmed() {
    return confirmed;
  }
}
