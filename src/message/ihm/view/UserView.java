package message.ihm.view;

import message.datamodel.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

public class UserView extends JPanel{
    protected JTextField recherche;
    protected JList<String> userList;
    protected DefaultListModel<String> listUserModel;
    protected JButton searchedButton;
    protected JButton followButton;
    protected JButton unfollowButton;

    public UserView(User connectedUser, JLabel userInfos){
        initUserView(connectedUser, userInfos);
    }

    public void initUserView(User connectedUser, JLabel userInfos) {

        setPreferredSize(new Dimension(300, 0));

        JPanel recherchePanel = new JPanel(new BorderLayout());
        JLabel usersListLabel = new JLabel("Utilisateurs", SwingConstants.CENTER);

        recherche = new JTextField("");

        searchedButton = new JButton("Rechercher");

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("test");
        arrayList.add("test");
        arrayList.add("test");
        listUserModel = new DefaultListModel<>();
        for (String user : arrayList) {
            listUserModel.addElement(user);
        }

        userList = new JList<>(listUserModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel actionsPanel = new JPanel(new BorderLayout());
        followButton = new JButton("S'abonner");
        unfollowButton = new JButton("Se désabonner");

        actionsPanel.add(followButton, BorderLayout.CENTER);
        actionsPanel.add(unfollowButton, BorderLayout.EAST);

        recherchePanel.add(usersListLabel, BorderLayout.NORTH);
        recherchePanel.add(recherche, BorderLayout.CENTER);
        recherchePanel.add(searchedButton, BorderLayout.EAST);

        add(recherchePanel, BorderLayout.NORTH);
        add(actionsPanel, BorderLayout.SOUTH);
        add(userList, BorderLayout.CENTER);
    }

    public void searchUser(UserListener userListener){
        searchedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = recherche.getText();
                listUserModel = (DefaultListModel<String>) userList.getModel();
                listUserModel.clear();
                for (String user : userListener.searchUser(searchText))
                    listUserModel.addElement(user);
            }
        });
    }

    public void followUser(UserListener userListener, User connectedUser, JLabel userInfos){
                followButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                int index = userList.getSelectedIndex();
                if (index != -1) {
                    String selectedUser = userList.getSelectedValue();
                    userListener.followSelectedUser(connectedUser, selectedUser);
                    userInfos.setText("<html>Nom: " + connectedUser.getName() +
                            "<br>Tag: " + connectedUser.getUserTag() +
                            "<br>Nombre de follow: " + connectedUser.getFollows().size() +
                            "<br>Follow: " + Arrays.toString(connectedUser.getFollows().toArray()) +
                            "<br>Avatar: " + connectedUser.getAvatarPath() + "</html>");
                }
            }
        });
    }

    public void getAllUsers(UserListener userListener){
        userListener.getAllUsers();
    }

    public void unfollowUser(UserListener userListener, User connectedUser, JLabel userInfos){
        unfollowButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = userList.getSelectedIndex();
                if (index != -1) {
                    String selectedUser = userList.getSelectedValue();
                    userListener.unFollowSelectedUser(connectedUser, selectedUser);
                    userInfos.setText("<html>Nom: " + connectedUser.getName() +
                            "<br>Tag: " + connectedUser.getUserTag() +
                            "<br>Nombre de follow: " + connectedUser.getFollows().size() +
                            "<br>Follow: " + Arrays.toString(connectedUser.getFollows().toArray()) +
                            "<br>Avatar: " + connectedUser.getAvatarPath() + "</html>");
                }
            }
        });
    }

    public interface UserListener {
        ArrayList<String> searchUser(String searchText);
        void followSelectedUser(User connectedUser, String userTag);
        void unFollowSelectedUser(User connectedUser, String userTag);
        ArrayList<String> getAllUsers();

    }
}
