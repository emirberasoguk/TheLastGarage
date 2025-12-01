import gui.GameFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main (String[] arg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GameFrame();
            }
        });
    }
}