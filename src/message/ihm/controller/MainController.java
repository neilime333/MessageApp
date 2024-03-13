package message.ihm.controller;

import message.ihm.session.ISession;
import message.core.EntityManager;
import message.core.database.IDatabase;
import message.datamodel.Message;
import message.datamodel.User;
import message.ihm.view.ConnexionView;
import message.ihm.view.MainView;

import java.text.SimpleDateFormat;
import java.util.*;

public class MainController implements MainView.MainListener {
    protected IDatabase database;
    protected EntityManager entityManager;
    protected ISession session;
    protected MainView mainView;
    protected ConnexionView connexionView;
    protected UserController userController;

    public MainController(IDatabase database, EntityManager entityManager, ISession mSession) {
        this.database = database;
        this.entityManager = entityManager;
        this.session = mSession;
        this.mainView = new MainView(this);
        this.mainView.logOutOnClick(this);
        this.userController = new UserController(database,entityManager,mSession);
    }

    //Users

    public ArrayList<String> getAllUsers() {
        ArrayList<String> userList = new ArrayList<>();
        for (User user : database.getUsers()) {
            if(!user.getUserTag().equals(this.session.getConnectedUser().getUserTag())){
                userList.add(user.getUserTag());
            }
        }
        return userList;
    }

    public ArrayList<String> searchUser(String searchedUser){
        ArrayList<String> users = new ArrayList<>();
        if(!searchedUser.isEmpty()) {
            for (User user : database.getUsers()) {
                if (user.getUserTag().contains(searchedUser) && !user.getUserTag().equals(this.session.getConnectedUser().getUserTag())) {
                    users.add(user.getUserTag());
                }
            }
            return users;
        } else {
            return getAllUsers();
        }
    }


    public User getConnectedUser() {
        return this.session.getConnectedUser();
    }

    public boolean followSelectedUser(User connectedUser, String userTag){
        if (!connectedUser.getFollows().contains(userTag)) {
            connectedUser.addFollowing(userTag);
            entityManager.writeUserFile(getConnectedUser());
            return true;
        } else {
            return false;
        }
    }

    public boolean unFollowSelectedUser(User connectedUser, String userTag){
        if (connectedUser.getFollows().contains(userTag)) {
            connectedUser.removeFollowing(userTag);
            entityManager.writeUserFile(connectedUser);
            return true;
        } else {
            return false;
        }
    }

    //Messages

    public Set<Message> searchMessage(String searchedMessage){
        Set<Message> messages = new HashSet<>();
        if(!searchedMessage.isEmpty()) {
            if (searchedMessage.contains("@")) { //ping un utilisateur
                String userTag = searchedMessage.replace("@", "");
                messages = database.getMessagesWithUserTag(userTag);
            } else if (searchedMessage.contains("#")) { //# comme sur twitter
                String userTag = searchedMessage.replace("#", "");
                messages = database.getMessagesWithTag(userTag);
            } else if (!searchedMessage.contains("@") && !searchedMessage.contains("#")) {
                for (Message message : getAllMessages()) {
                    if (message.containsTag(searchedMessage) || message.containsUserTag(searchedMessage)) {
                        messages.add(message);
                    }
                }
            }
        } else {
            messages = getAllMessages();
        }
        return messages;
    }

    public Set<Message> getAllMessages(){
        return database.getMessages();
    }

    public String addMessage(User connectedUser, String messageContent){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        Message message = new Message(connectedUser, messageContent);
        database.addMessage(message);
        entityManager.writeMessageFile(message);
        return "<html><i>" + dateFormat.format(new Date(message.getEmissionDate())) + "</i>" + " - " + message.getSender().getUserTag() + " : " + message.getText() + "</html>";
    }

    //Notifications

//    public void notifyUserWhenFollowSendMessage(){
//        if ()
//    }

    public void logOutOnClick() {
        session.disconnect();
        connexionView = new ConnexionView();
        new ConnexionController(connexionView, database, entityManager, session);
        new InscriptionController(connexionView, database, entityManager, session);
    }

}
