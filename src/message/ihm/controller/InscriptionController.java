package message.ihm.controller;

import message.core.EntityManager;
import message.core.database.IDatabase;
import message.datamodel.User;
import message.ihm.session.ISession;
import message.ihm.view.ConnexionView;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class InscriptionController implements ConnexionView.ConnexionListener {

    protected IDatabase database;
    protected EntityManager entityManager;
    protected ISession session;
    protected boolean doUserExists = false;


    public InscriptionController(ConnexionView connexionView, IDatabase database, EntityManager entityManager, ISession session){
        connexionView.registerOnClick(this);
        this.database = database;
        this.entityManager = entityManager;
        this.session = session;
    }

    @Override
    public String registerOnClick(String username, String tag, String filename) {
        initFolder();

        User user = new User(UUID.randomUUID(), tag, "password", username, new HashSet<>(), filename);

        for(User u : database.getUsers()){
            if(Objects.equals(u.getUserTag(), user.getUserTag())){
                doUserExists = true;
                break;
            }
        }

        if(isUsernameNull(username) || isTagNull(tag)) {
            return "Veuillez remplir les champs d'inscription";
        } else if(doUserExists) {
            return "Le tag rentré est déjà associé à un compte existant";
        } else {
            database.addUser(user);
            entityManager.writeUserFile(user);
            return "Inscription réussie, vous pouvez maintenant vous connecter";
        }
    }

    public boolean isUsernameNull(String username){
        return username == null || username.isEmpty();
    }

    public boolean isTagNull(String tag){
        return tag == null || tag.isEmpty();
    }

    public void initFolder(){
        entityManager.setExchangeDirectory("src/main/resources/users/");
    }

    public Set<User> getUsersRegistered(){
        return database.getUsers();
    }

    @Override
    public String loginOnClick(String username, String tag) {
        return null;
    }

}
