package message.ihm.view;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ConnexionView extends JPanel {

    protected JFrame mFrame = new JFrame();
    protected JTextField username;
    protected JTextField tag;
    protected JButton avatar;
    protected JLabel messageLabel;
    protected JButton connexionButton;
    protected JButton inscriptionButton;
    protected String filename;

    public ConnexionView() {
        ImageIcon imageIcon = new ImageIcon("src/main/resources/images/logo_20.png");
        ImageIcon loginIcon = new ImageIcon("src/main/resources/images/enter.png");
        ImageIcon registerIcon = new ImageIcon("src/main/resources/images/register.png");

        mFrame.setTitle("Connexion/Inscription");
        mFrame.setIconImage(imageIcon.getImage());
        mFrame.setSize(600, 400);
        mFrame.setLocationRelativeTo(null);
        mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mFrame.setLayout(new GridBagLayout());

        JPanel connexionPanel = new JPanel();
        connexionPanel.setLayout(new GridBagLayout());

        JLabel usernameLabel = new JLabel("Nom d'utilisateur", SwingConstants.CENTER);
        connexionPanel.add(usernameLabel, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        JLabel tagLabel = new JLabel("Tag", SwingConstants.CENTER);
        connexionPanel.add(tagLabel, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        JLabel avatarLabel = new JLabel("Avatar", SwingConstants.CENTER);
        connexionPanel.add(avatarLabel, new GridBagConstraints(0, 2, 1, 1, 1, 1, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        username = new JTextField(20);
        connexionPanel.add(username, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        tag = new JTextField(20);
        connexionPanel.add(tag, new GridBagConstraints(1, 1, 1, 1, 1, 1, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        avatar = new JButton("Choisir son avatar");
        connexionPanel.add(avatar, new GridBagConstraints(1, 2, 1, 1, 1, 1, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        avatar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filename = selecteurFichier();
                System.out.println(filename);
            }
        });

        connexionButton = new JButton("Connexion");
        connexionButton.setIcon(loginIcon);

        connexionPanel.add(connexionButton, new GridBagConstraints(0, 3, 2, 1, 1, 1, GridBagConstraints.LINE_END,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        JLabel inscriptionLabel = new JLabel("Pas inscrit ?", SwingConstants.CENTER);

        connexionPanel.add(inscriptionLabel, new GridBagConstraints(0, 3, 1, 1, 1, 1, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        inscriptionButton = new JButton("S'inscrire");
        inscriptionButton.setIcon(registerIcon);

        connexionPanel.add(inscriptionButton, new GridBagConstraints(1, 3, 2, 1, 1, 1, GridBagConstraints.LINE_START,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        messageLabel = new JLabel("", SwingConstants.CENTER);
        connexionPanel.add(messageLabel, new GridBagConstraints(0, 4, 2, 1, 1, 1, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        mFrame.add(connexionPanel);
        mFrame.setVisible(true);
    }

    public String selecteurFichier(){
        String filename = "";
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Ouvrir");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image Files", "jpg","png"));

        int result = fileChooser.showOpenDialog(mFrame);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            filename = selectedFile.getPath();
        }
        return filename;
    }

    public void loginOnClick(ConnexionListener connexionListener){
        connexionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messageLabel.setText(connexionListener.loginOnClick(username.getText(), tag.getText()));
            }
        });
    }

    public void registerOnClick(ConnexionListener connexionListener){
        inscriptionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messageLabel.setText(connexionListener.registerOnClick(username.getText(), tag.getText(), filename));
            }
        });
    }

    public void destroy(){
        mFrame.dispose();
        mFrame.getContentPane().removeAll();
        mFrame.revalidate();
        mFrame.repaint();
    }

    public interface ConnexionListener {
        String loginOnClick(String username, String tag);

        String registerOnClick(String username, String tag, String filename);
    }

}
