package message.ihm.controller;

import message.core.EntityManager;
import message.core.database.IDatabase;
import message.datamodel.Message;
import message.datamodel.User;
import message.ihm.session.ISession;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class MessageController {
    protected IDatabase database;
    protected EntityManager entityManager;
    protected ISession session;

    public MessageController(IDatabase database, EntityManager entityManager, ISession session){
        this.database = database;
        this.entityManager = entityManager;
        this.session = session;
    }

    public String addMessage(User connectedUser, String messageContent){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        Message message = new Message(connectedUser, messageContent);
        database.addMessage(message);
        entityManager.writeMessageFile(message);
        return "<html><i>" + dateFormat.format(new Date(message.getEmissionDate())) + "</i>" + " - " + message.getSender().getUserTag() + " : " + message.getText() + "</html>";
    }

    public Set<Message> getAllMessages(){
        return database.getMessages();
    }

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

}
