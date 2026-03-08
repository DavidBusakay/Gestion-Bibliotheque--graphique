package gui.dialogsBox;

import models.Bibliotheque;
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
import javax.swing.JTextField;

public class AjouterUtilisateurDialog extends JDialog {
  private Bibliotheque bibliotheque;
  private JComboBox<String> cmbType;
  private JTextField txtNom;
  private boolean confirmed = false;
  
  public AjouterUtilisateurDialog(JFrame parent, Bibliotheque bibliotheque) {
    super(parent, "Ajouter un utilisateur", true);
    this.bibliotheque = bibliotheque;
    
    setSize(350, 200);
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
    mainPanel.add(new JLabel("Type d'utilisateur:"), gbc);
    
    cmbType = new JComboBox<>(new String[]{"Étudiant", "Professeur"});
    gbc.gridx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    mainPanel.add(cmbType, gbc);
    
    // Nom
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.NONE;
    mainPanel.add(new JLabel("Nom:"), gbc);
    
    txtNom = new JTextField(20);
    gbc.gridx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    mainPanel.add(txtNom, gbc);
    
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
  
  private void ajouter() {
    String nom = txtNom.getText().trim();
    
    if (nom.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Veuillez entrer un nom.", "Erreur", JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    String type = (String) cmbType.getSelectedItem();
    Utilisateur utilisateur;
    
    if ("Étudiant".equals(type)) {
      utilisateur = new Etudiant(nom);
    } else {
      utilisateur = new Professeur(nom);
    }
    
    bibliotheque.ajouterUtilisateur(utilisateur);
    confirmed = true;
    dispose();
  }
  
  public boolean isConfirmed() {
    return confirmed;
  }
}
