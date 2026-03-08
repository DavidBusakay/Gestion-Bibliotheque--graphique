package gui.panels;

import models.Bibliotheque;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class StatistiquesPanel extends JPanel {
  private Bibliotheque bibliotheque;
  private JTextArea textArea;
  
  public StatistiquesPanel(Bibliotheque bibliotheque) {
    this.bibliotheque = bibliotheque;
    
    setLayout(new BorderLayout(10, 10));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    // Titre
    JLabel lblTitre = new JLabel("Statistiques de la Bibliothèque");
    lblTitre.setFont(new Font("Arial", Font.BOLD, 18));
    add(lblTitre, BorderLayout.NORTH);
    
    // TextArea pour afficher les statistiques
    textArea = new JTextArea();
    textArea.setEditable(false);
    textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
    JScrollPane scrollPane = new JScrollPane(textArea);
    add(scrollPane, BorderLayout.CENTER);
    
    // Panel du bas avec le bouton Rafraichir
    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    
    JButton btnRafraichir = new JButton("Rafraîchir");
    btnRafraichir.addActionListener(e -> rafraichir());
    bottomPanel.add(btnRafraichir);
    
    add(bottomPanel, BorderLayout.SOUTH);
    
    rafraichir();
  }
  
  public void rafraichir() {
    String stats = bibliotheque.getStatistiquesAsString();
    textArea.setText(stats);
  }
}
