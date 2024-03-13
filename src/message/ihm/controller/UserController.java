package message.ihm.controller;

import message.core.EntityManager;
import message.core.database.IDatabase;
import message.datamodel.User;
import message.ihm.session.ISession;
import message.ihm.view.UserView;

import java.util.ArrayList;

public class UserController implements UserView.UserListener {
    protected IDatabase database;
    protected EntityManager entityManager;
    protected ISession session;

    public UserController(IDatabase database, EntityManager entityManager, ISession session){
        this.database = database;
        this.entityManager = entityManager;
        this.session = session;
    }

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

    public void followSelectedUser(User connectedUser, String userTag){
        connectedUser.addFollowing(userTag);
        entityManager.writeUserFile(getConnectedUser());
    }

    public void unFollowSelectedUser(User connectedUser, String userTag){
        connectedUser.removeFollowing(userTag);
        entityManager.writeUserFile(connectedUser);
    }

}
