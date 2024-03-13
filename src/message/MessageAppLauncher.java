package message;

import message.ihm.MessageApp;
import message.core.EntityManager;
import message.core.database.Database;
import message.core.database.DatabaseObserver;
import message.core.database.IDatabase;
import message.core.database.IDatabaseObserver;
import message.ihm.session.ISession;
import message.ihm.session.Session;

/**
 * Classe de lancement de l'application.
 *
 * @author S.Lucas
 */
public class MessageAppLauncher {

	/**
	 * Indique si le mode bouchoné est activé.
	 */
	protected static boolean IS_MOCK_ENABLED = true;

	/**
	 * Launcher.
	 *
	 * @param args
	 */
	public static void main(String[] args) {

		IDatabaseObserver databaseObserver = new DatabaseObserver();
		IDatabase database = new Database();
		ISession session = new Session();
		EntityManager entityManager = new EntityManager(database);
		database.addObserver(databaseObserver);

//		if (IS_MOCK_ENABLED) {
//			MessageAppMock mock = new MessageAppMock(database, entityManager);
//			mock.showGUI();
//		}

		MessageApp messageApp = new MessageApp(database, entityManager, session);
		messageApp.init();
		messageApp.show();
	}
}
