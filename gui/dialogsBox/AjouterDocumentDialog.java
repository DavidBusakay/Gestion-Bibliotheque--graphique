package gui.dialogsBox;

import models.Bibliotheque;
import models.documents.*;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AjouterDocumentDialog extends JDialog {
  private Bibliotheque bibliotheque;
  private JComboBox<String> cmbType;
  private JTextField txtTitre;
  private JTextField txtAnnee;
  private JTextField txtAuteurNumero;
  private boolean confirmed = false;
  
  public AjouterDocumentDialog(JFrame parent, Bibliotheque bibliotheque) {
    super(parent, "Ajouter un document", true);
    this.bibliotheque = bibliotheque;
    
    setSize(400, 300);
    setLocationRelativeTo(parent);
    setLayout(new BorderLayout(10, 10));
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    
    // Panel principal
    JPanel mainPanel = new JPanel(new GridBagLayout());
    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.anchor = GridBagConstraints.WEST;
    
    // Type
    gbc.gridx = 0;
    gbc.gridy = 0;
    mainPanel.add(new JLabel("Type de document:"), gbc);
    
    cmbType = new JComboBox<>(new String[]{"Livre", "Revue"});
    cmbType.addActionListener(e -> updateFields());
    gbc.gridx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    mainPanel.add(cmbType, gbc);
    
    // Titre
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.NONE;
    mainPanel.add(new JLabel("Titre:"), gbc);
    
    txtTitre = new JTextField(20);
    gbc.gridx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    mainPanel.add(txtTitre, gbc);
    
    // Année
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.fill = GridBagConstraints.NONE;
    mainPanel.add(new JLabel("Année:"), gbc);
    
    txtAnnee = new JTextField(20);
    gbc.gridx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    mainPanel.add(txtAnnee, gbc);
    
    // Auteur/Numéro
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.fill = GridBagConstraints.NONE;
    lblAuteurNumero = new JLabel("Auteur:");
    mainPanel.add(lblAuteurNumero, gbc);
    
    txtAuteurNumero = new JTextField(20);
    gbc.gridx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    mainPanel.add(txtAuteurNumero, gbc);
    
    add(mainPanel, BorderLayout.CENTER);
    
    // Buttons
    JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton btnAjouter = new JButton("Ajouter");
    btnAjouter.addActionListener(e -> ajouter());
    buttonsPanel.add(btnAjouter);
    
    JButton btnAnnuler = new JButton("Annuler");
    btnAnnuler.addActionListener(e -> dispose());
    buttonsPanel.add(btnAnnuler);
    
    add(buttonsPanel, BorderLayout.SOUTH);
  }
  
  private JLabel lblAuteurNumero;
  
  private void updateFields() {
    String type = (String) cmbType.getSelectedItem();
    if ("Livre".equals(type)) {
      lblAuteurNumero.setText("Auteur:");
    } else {
      lblAuteurNumero.setText("Numéro d'édition:");
    }
  }
  
  private void ajouter() {
    String titre = txtTitre.getText().trim();
    String anneeStr = txtAnnee.getText().trim();
    String auteurNumero = txtAuteurNumero.getText().trim();
    
    if (titre.isEmpty() || anneeStr.isEmpty() || auteurNumero.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    int annee;
    try {
      annee = Integer.parseInt(anneeStr);
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(this, "L'année doit être un nombre.", "Erreur", JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    String type = (String) cmbType.getSelectedItem();
    Document doc;
    
    if ("Livre".equals(type)) {
      doc = new Livre(titre, annee, auteurNumero);
    } else {
      int numero;
      try {
        numero = Integer.parseInt(auteurNumero);
      } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Le numéro d'édition doit être un nombre.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
      }
      doc = new Revue(titre, annee, numero);
    }
    
    bibliotheque.ajouterDocument(doc);
    confirmed = true;
    dispose();
  }
  
  public boolean isConfirmed() {
    return confirmed;
  }
}
