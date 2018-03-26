package loadapplication;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class SplashScreen extends javax.swing.JFrame {

    private final File file = new File("Images/Splash_Screen.jpg");

    private void checkImageExists() {
        if (!file.exists()) {
            new File("Images").mkdir();
            try {
                BufferedImage image = ImageIO.read(getClass().getResource("Splash_Screen.jpg"));
                ImageIO.write(image, "jpg", file);
            } catch (IOException | NullPointerException ex) {
                Logger.getLogger(SplashScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void setBackgroundImage() {
        checkImageExists();
        try {
            this.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("Images/Splash_Screen.jpg")))));
        } catch (IOException | NullPointerException | ArrayIndexOutOfBoundsException ex) {
        }
    }

    public SplashScreen() {
        setBackgroundImage();
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jProgressBar2 = new javax.swing.JProgressBar()
        ;

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jProgressBar2.setString("");
        jProgressBar2.setStringPainted(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jProgressBar2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(294, Short.MAX_VALUE)
                .addComponent(jProgressBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JProgressBar jProgressBar2;
    // End of variables declaration//GEN-END:variables
}
