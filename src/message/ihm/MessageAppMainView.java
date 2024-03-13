package message.ihm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

/**
 * Classe de la vue principale de l'application.
 */
public class MessageAppMainView {
    protected JFrame mFrame;

    private JLabel fileName = new JLabel();

    public void showGUI() {
        // Init auto de l'IHM au cas ou ;)
        if (mFrame == null) {
            this.fenetreMenuPopupGUI(); //TODO change call
        }

        // Affichage dans l'EDT
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Custom de l'affichage
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                JFrame frame = mFrame;
                Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
                frame.setLocation((screenSize.width - frame.getWidth()) / 6,
                        (screenSize.height - frame.getHeight()) / 4);

                // Affichage
                mFrame.setVisible(true);

                mFrame.pack();
            }
        });
    }

    //Fenetre avec menu et popup
    protected void fenetreMenuPopupGUI() {
        Dimension dimension = new Dimension(900, 450);
        ImageIcon imageIcon = new ImageIcon("src/main/resources/images/logo_20.png");

        mFrame = new JFrame("Fenetre");
        mFrame.setLayout(new GridBagLayout());
        mFrame.setPreferredSize(dimension);
        mFrame.setIconImage(imageIcon.getImage());

        JMenuBar menuBar = new JMenuBar();
        JMenu fichierMenu = new JMenu("Fichier");
        JMenu autreMenu = new JMenu("?");

        JMenuItem ouvrirMenuItem = new JMenuItem("Ouvrir");
        ImageIcon openIcon = new ImageIcon("src/main/resources/images/editIcon_20.png");
        ouvrirMenuItem.setIcon(openIcon);
        ouvrirMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selecteurFichier();
            }
        });
        JMenuItem quitterMenuItem = new JMenuItem("Quitter");
        ImageIcon crossIcon = new ImageIcon("src/main/resources/images/exitIcon_20.png");
        quitterMenuItem.setIcon(crossIcon);
        quitterMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fichierMenu.add(ouvrirMenuItem);
        fichierMenu.add(quitterMenuItem);

        autreMenu.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                ImageIcon imageIcon = new ImageIcon("src/main/resources/images/logo_50.png");
                JLabel jLabel = new JLabel("<html><center>UBO M2-TIIL<br>Département Informatique");
                JOptionPane.showMessageDialog(mFrame,jLabel,"A propos",JOptionPane.INFORMATION_MESSAGE,imageIcon);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        menuBar.add(fichierMenu);
        menuBar.add(autreMenu);

        mFrame.setJMenuBar(menuBar);
    }

    protected void selecteurFichier() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Ouvrir");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int result = fileChooser.showOpenDialog(mFrame);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            fileName.setText("Fichier sélectionné : " + selectedFile.getAbsolutePath());
        } else {
            fileName.setText("Aucun fichier sélectionné");
        }
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        mFrame.add(fileName, gbc);
        mFrame.pack();
    }

//    protected void connexionInscriptionUI(){
//        Dimension dimension = new Dimension(400, 200);
//        ImageIcon imageIcon = new ImageIcon("src/main/resources/images/logo_20.png");
//
//        mFrame = new JFrame("Connexion/Inscription");
//        mFrame.setPreferredSize(dimension);
//        mFrame.setIconImage(imageIcon.getImage());
//
//        JPanel panel = new JPanel(new GridBagLayout());
//        JLabel loginLabel = new JLabel("Déjà un compte ?");
//        JButton loginButton = new JButton("Connexion");
//        loginButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                connexionUI();
//            }
//        });
//        JLabel registerLabel = new JLabel("Pas encore de compte ?");
//        JButton registerButton = new JButton("Inscription");
//        registerButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                inscriptionUI();
//            }
//        });
//
//        panel.add(loginLabel, new GridBagConstraints(0, 0, 2, 1, 1, 1, GridBagConstraints.CENTER,
//                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
//        panel.add(loginButton, new GridBagConstraints(0, 1, 2, 1, 1, 1, GridBagConstraints.CENTER,
//                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
//        panel.add(registerLabel, new GridBagConstraints(0, 2, 2, 1, 1, 1, GridBagConstraints.CENTER,
//                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
//        panel.add(registerButton, new GridBagConstraints(0, 3, 2, 1, 1, 1, GridBagConstraints.CENTER,
//                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
//
//        mFrame.add(panel);
//        mFrame.setLocationRelativeTo(null);
//    }
//
//    protected void connexionUI(){
//        mFrame.getContentPane().removeAll();
//        mFrame.add(new ConnexionView());
//        mFrame.revalidate();
//        mFrame.repaint();
//}
//
//    protected void inscriptionUI(){
//        mFrame.getContentPane().removeAll();
//        mFrame.add(new InscriptionView());
//        mFrame.revalidate();
//        mFrame.repaint();
//    }

    protected void setVisible(){
        mFrame.setVisible(true);
    }
}
