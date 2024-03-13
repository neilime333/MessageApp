package message.ihm.view;

import message.datamodel.User;
import message.ihm.controller.MainController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class ProfilView extends JPanel {
    private JLabel userProfileLabel;
    private JLabel userInfos;
    private JButton logOutButton;
    protected MainController mainController;

    public ProfilView(JFrame mFrame, User connectedUser) {
        setPreferredSize(new Dimension(200, 0));

        userProfileLabel = new JLabel("Profil", SwingConstants.CENTER);
        JLabel userInfos = new JLabel("", SwingConstants.CENTER);

        if (connectedUser != null) {
            userInfos.setText("<html>Nom: " + connectedUser.getName() +
                    "<br>Tag: " + connectedUser.getUserTag() +
                    "<br>Nombre de follow: " + connectedUser.getFollows().size() +
                    "<br>Follow: " + Arrays.toString(connectedUser.getFollows().toArray()) +
                    "<br>Avatar: " + connectedUser.getAvatarPath() + "</html>");
        }

        add(userProfileLabel, BorderLayout.NORTH);
        add(userInfos, BorderLayout.CENTER);

        logOutButton = new JButton("Déconnexion");
        add(logOutButton, BorderLayout.SOUTH);

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainController.logOutOnClick();
                mFrame.dispose();
                mFrame.getContentPane().removeAll();
                mFrame.revalidate();
                mFrame.repaint();
            }
        });
    }

    public JLabel getUserInfosLabel() {
        return userInfos;
    }

    public JButton getLogOutButton() {
        return logOutButton;
    }
}
