package gui;

import javax.swing.JFrame;

// Ana Pencere
public class GameFrame extends JFrame {

    public GameFrame() {
        this.setTitle("Sanayi Hurdalığı - Tower Defense");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        
        GamePanel panel = new GamePanel();
        this.add(panel);
        this.pack(); // İçeriğe göre boyutu ayarla (Panel boyutu ayarlandığında işe yarar)
        this.setSize(800, 600); // Şimdilik sabit boyut
        this.setLocationRelativeTo(null); // Ekranın ortasında aç
        this.setVisible(true);
        
        panel.startGame(); // Oyunu başlat
    }
}
