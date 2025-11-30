package gui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Ana Pencere
public class GameFrame extends JFrame {

    public GameFrame() {
        this.setTitle("Sanayi Hurdalığı - Tower Defense");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        
        GamePanel panel = new GamePanel();
        this.add(panel, BorderLayout.CENTER);
        
        // Kontrol Paneli
        JPanel controlPanel = new JPanel();
        
        JButton btnWave = new JButton("Sonraki Dalga");
        btnWave.addActionListener(e -> panel.startNextWave());
        
        JButton btnCivata = new JButton("Civata (50)");
        btnCivata.addActionListener(e -> panel.setSelectedTower("Civata"));
        
        JButton btnAnahtar = new JButton("Anahtar (75)");
        btnAnahtar.addActionListener(e -> panel.setSelectedTower("Anahtar"));
        
        JButton btnYaglama = new JButton("Yağlama (70)");
        btnYaglama.addActionListener(e -> panel.setSelectedTower("Yaglama"));
        
        controlPanel.add(btnWave);
        controlPanel.add(btnCivata);
        controlPanel.add(btnAnahtar);
        controlPanel.add(btnYaglama);
        
        this.add(controlPanel, BorderLayout.SOUTH);
        
        this.pack(); 
        // this.setSize(800, 600); // Pack kullandığımız için boyutu panel belirler + butonlar
        this.setLocationRelativeTo(null); 
        this.setVisible(true);
        
        panel.startGame(); 
    }
}
