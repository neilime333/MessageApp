package message.ihm.view;

import message.datamodel.Message;
import message.datamodel.User;
import message.ihm.controller.MainController;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

public class MainView extends JPanel {
    JFrame mFrame = new JFrame();
    protected JTextField recherche;
    protected MainController mainController;
    protected JButton logOutButton;
    protected JList<String> userList;
    protected JList<String> messageList;
    protected DefaultListModel<String> listUserModel;
    protected DefaultListModel<String> messageModel;
    protected JTextField messageToSend;
    protected JLabel userInfos;
    protected ImageIcon avatarIcon;
    public MainView(MainController mainController) {
        this.mainController = mainController;
        initGUI();
    }

    public void initGUI(){

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        User connectedUser = mainController.getConnectedUser();

        //Images
        ImageIcon imageIcon = new ImageIcon("src/main/resources/images/logo_20.png");
        ImageIcon searchIcon = new ImageIcon("src/main/resources/images/search.png");
        ImageIcon logoutIcon = new ImageIcon("src/main/resources/images/logout.png");
        ImageIcon followIcon = new ImageIcon("src/main/resources/images/follow.png");
        ImageIcon unfollowIcon = new ImageIcon("src/main/resources/images/unfollow.png");
        ImageIcon messageIcon = new ImageIcon("src/main/resources/images/message.png");

        mFrame.setTitle("Accueil");
        mFrame.setIconImage(imageIcon.getImage());
        mFrame.setSize(800, 500);
        mFrame.setLocationRelativeTo(null);
        mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mFrame.setLayout(new BorderLayout());

        // Panel pour afficher les informations utilisateurs
        JPanel profilPanel = new JPanel(new BorderLayout());
        profilPanel.setPreferredSize(new Dimension(200, 0));

        JLabel userProfileLabel = new JLabel("Profil", SwingConstants.CENTER);
        userInfos = new JLabel("", SwingConstants.CENTER);
        userInfos.setHorizontalTextPosition(JLabel.CENTER);
        userInfos.setVerticalTextPosition(JLabel.BOTTOM);

        if (connectedUser != null) {
            setUserInfos(connectedUser);
        }

        profilPanel.add(userProfileLabel, BorderLayout.NORTH);
        profilPanel.add(userInfos, BorderLayout.CENTER);

        logOutButton = new JButton("Déconnexion");
        logOutButton.setIcon(logoutIcon);
        profilPanel.add(logOutButton, BorderLayout.SOUTH);

        // Panel pour afficher les utilisateurs inscrits
        JPanel usersListPanel = new JPanel(new BorderLayout());
        usersListPanel.setPreferredSize(new Dimension(300,0));

        JPanel recherchePanel = new JPanel(new BorderLayout());
        JLabel usersListLabel = new JLabel("Utilisateurs", SwingConstants.CENTER);

        recherche = new JTextField("");

        JButton searchedButton = new JButton("Rechercher");
        searchedButton.setIcon(searchIcon);

        searchedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = recherche.getText();
                listUserModel = (DefaultListModel<String>) userList.getModel();
                listUserModel.clear();
                for (String user : mainController.searchUser(searchText))
                    listUserModel.addElement(user);
            }
        });

        ArrayList<String> arrayList = mainController.getAllUsers();
        listUserModel = new DefaultListModel<>();
        for (String user : arrayList) {
            listUserModel.addElement(user);
        }

        userList = new JList<>(listUserModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel actionsPanel = new JPanel(new BorderLayout());
        JButton followButton = new JButton("S'abonner");
        followButton.setIcon(followIcon);
        JButton unfollowButton = new JButton("Se désabonner");
        unfollowButton.setIcon(unfollowIcon);

        followButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = userList.getSelectedIndex();
                if (index != -1) {
                    String selectedUser = userList.getSelectedValue();
                    if(mainController.followSelectedUser(connectedUser, selectedUser)){
                        JOptionPane.showMessageDialog(mFrame,"Abonnement à " + selectedUser + " réussi!","Abonnement réussi!", JOptionPane.INFORMATION_MESSAGE);
                        setUserInfos(connectedUser);
                    } else {
                        JOptionPane.showMessageDialog(mFrame,"Vous êtes déjà abonné à " + selectedUser,"Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        unfollowButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = userList.getSelectedIndex();
                if (index != -1) {
                    String selectedUser = userList.getSelectedValue();
                    if(mainController.unFollowSelectedUser(connectedUser, selectedUser)){
                        JOptionPane.showMessageDialog(mFrame,"Vous vous êtes désabonnés de " + selectedUser + "!","Désabonnement réussi!", JOptionPane.INFORMATION_MESSAGE);
                        setUserInfos(connectedUser);
                    } else {
                        JOptionPane.showMessageDialog(mFrame,"Vous n'êtes pas abonné à " + selectedUser,"Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        actionsPanel.add(followButton, BorderLayout.CENTER);
        actionsPanel.add(unfollowButton, BorderLayout.EAST);

        recherchePanel.add(usersListLabel, BorderLayout.NORTH);
        recherchePanel.add(recherche, BorderLayout.CENTER);
        recherchePanel.add(searchedButton, BorderLayout.EAST);

        usersListPanel.add(recherchePanel, BorderLayout.NORTH);
        usersListPanel.add(actionsPanel, BorderLayout.SOUTH);
        usersListPanel.add(userList, BorderLayout.CENTER);

        // Panel pour afficher les messages
        JPanel searchMessagePanel = new JPanel(new BorderLayout());
        JPanel messagesPanel = new JPanel(new BorderLayout());
        JPanel envoyerMessagePanel = new JPanel(new BorderLayout());
        messagesPanel.setPreferredSize(new Dimension(300,0));
        JLabel messagesLabel = new JLabel("Messages", SwingConstants.CENTER);

        JTextField searchedMessage = new JTextField(15);
        JButton searchedMessageButton = new JButton("Rechercher");
        searchedMessageButton.setIcon(searchIcon);

        messageToSend = new JTextField(15);
        JButton messageButton = new JButton("Envoyer");
        messageButton.setIcon(messageIcon);

        messageToSend.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkLength();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkLength();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkLength();
            }
        });

        Set<Message> messages = mainController.getAllMessages();
        messageModel = new DefaultListModel<>();
        for (Message message : messages) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            messageModel.addElement("<html><i>" + dateFormat.format(new Date(message.getEmissionDate())) + "</i>" + " - " + message.getSender().getUserTag() + " : " + message.getText() + "</html>");
        }

        messageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String msgToSend = messageToSend.getText();
                if(msgToSend.length() <= 200) {
                    messageModel.addElement(mainController.addMessage(connectedUser, msgToSend));
                } else {
                    JOptionPane.showMessageDialog(mFrame,"Le texte rentré dépasse la limite de 200 caractères","Erreur", JOptionPane.ERROR_MESSAGE);
                }
                messageToSend.setText("");
            }
        });

        messageList = new JList<>(messageModel);
        messageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        messageList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2){
                    int index = messageList.getSelectedIndex();
                    if (index != -1) {
                        String selectedItem = messageList.getModel().getElementAt(index);
                        JOptionPane.showMessageDialog(mFrame,selectedItem,"Information sur le message", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        searchedMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchedMessage.getText();
                messageModel = (DefaultListModel<String>) messageList.getModel();
                messageModel.clear();
                for (Message message : mainController.searchMessage(searchText)){
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                    messageModel.addElement(dateFormat.format(new Date(message.getEmissionDate())) + " - " + message.getSender().getUserTag() + " : " + message.getText());
                }
            }
        });

        searchMessagePanel.add(messagesLabel, BorderLayout.NORTH);
        searchMessagePanel.add(searchedMessage, BorderLayout.WEST);
        searchMessagePanel.add(searchedMessageButton, BorderLayout.CENTER);

        envoyerMessagePanel.add(messageToSend, BorderLayout.WEST);
        envoyerMessagePanel.add(messageButton, BorderLayout.CENTER);

        messagesPanel.add(searchMessagePanel, BorderLayout.NORTH);
        messagesPanel.add(messageList, BorderLayout.CENTER);
        messagesPanel.add(envoyerMessagePanel, BorderLayout.SOUTH);

        profilPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
        usersListPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
        messagesPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));

        mFrame.add(profilPanel, BorderLayout.WEST);
        mFrame.add(usersListPanel, BorderLayout.CENTER);
        mFrame.add(messagesPanel, BorderLayout.EAST);

        mFrame.setVisible(true);
    }

    public void checkLength() {
        String text = messageToSend.getText();
        if (text.length() > 200) {
            messageToSend.setBackground(Color.decode("#e35a5a"));
        } else {
            messageToSend.setBackground(Color.WHITE);
        }
    }

    public void setUserInfos(User connectedUser){
        userInfos.setText("<html>Nom: " + connectedUser.getName() +
                "<br>Tag: " + connectedUser.getUserTag() +
                "<br>Nombre de follow: " + connectedUser.getFollows().size() +
                "<br>Follow: " + Arrays.toString(connectedUser.getFollows().toArray()) + "</html>");
        avatarIcon = new ImageIcon(connectedUser.getAvatarPath());
        userInfos.setIcon(avatarIcon);
    }

    public void destroy(){
        mFrame.dispose();
        mFrame.getContentPane().removeAll();
        mFrame.revalidate();
        mFrame.repaint();
    }

    public void logOutOnClick(MainListener mainListener){
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainListener.logOutOnClick();
                destroy();
            }
        });
    }

    public interface MainListener {
        void logOutOnClick();
    }

}
