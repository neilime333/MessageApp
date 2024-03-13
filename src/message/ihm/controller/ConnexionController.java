package message.ihm.controller;

import message.core.EntityManager;
import message.core.database.IDatabase;
import message.datamodel.User;
import message.ihm.session.ISession;
import message.ihm.view.ConnexionView;

import java.util.Objects;

public class ConnexionController implements ConnexionView.ConnexionListener {
    protected IDatabase database;
    protected EntityManager entityManager;
    protected ISession session;
    protected boolean doUserExists = false;
    protected ConnexionView connexionView;

    public ConnexionController(ConnexionView connexionView, IDatabase database, EntityManager entityManager, ISession mSession) {
        this.connexionView = connexionView;
        this.connexionView.loginOnClick(this);
        this.database = database;
        this.entityManager = entityManager;
        this.session = mSession;
    }

    @Override
    public String loginOnClick(String username, String tag) {
        initFolder();
        User userWhoWantsToLogin = null;

        for(User user : database.getUsers()){
            if(Objects.equals(user.getUserTag(), tag)){
                userWhoWantsToLogin = user;
                doUserExists = true;
                break;
            }
        }

        if(doUserExists){
            session.connect(userWhoWantsToLogin);
            this.connexionView.destroy();
            new MainController(database, entityManager, session);
            return "L'utilisateur " + session.getConnectedUser().getUserTag() + " est connecté!";
        } else {
            System.out.println("L'utilisateur n'existe pas");
            return "L'utilisateur n'existe pas, veuillez vous inscrire";
        }

    }

    @Override
    public String registerOnClick(String username, String tag, String filename) {
        return null;
    }

    public void initFolder(){
        entityManager.setExchangeDirectory("src/main/resources/users/");
    }

}
