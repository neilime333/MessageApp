package message.ihm;

import java.io.File;

import message.core.EntityManager;
import message.core.database.IDatabase;
import message.core.directory.IWatchableDirectory;
import message.core.directory.WatchableDirectory;
import message.datamodel.User;
import message.ihm.controller.ConnexionController;
import message.ihm.controller.InscriptionController;
import message.ihm.session.ISession;
import message.ihm.session.ISessionObserver;
import message.ihm.view.ConnexionView;
import message.ihm.view.MainView;

import javax.swing.*;

/**
 * Classe principale l'application.
 *
 * @author S.Lucas
 */
public class MessageApp implements ISessionObserver {
	/**
	 * Base de données.
	 */
	protected IDatabase mDatabase;

	/**
	 * Session
	 */
	protected ISession mSession;

	/**
	 * Gestionnaire des entités contenu de la base de données.
	 */
	protected EntityManager mEntityManager;

	/**
	 * Vue principale de l'application.
	 */
	protected MessageAppMainView mMainView;

	protected ConnexionView mConnexionView;

	protected MainView mainView;

	/**
	 * Classe de surveillance de répertoire
	 */
	protected IWatchableDirectory mWatchableDirectory;

	/**
	 * Répertoire d'échange de l'application.
	 */
	protected String mExchangeDirectoryPath;

	/**
	 * Nom de la classe de l'UI.
	 */
	protected String mUiClassName;

//	protected JFrame mFrame;

	/**
	 * Constructeur.
	 *
	 * @param entityManager
	 * @param database
	 */
	public MessageApp(IDatabase database, EntityManager entityManager, ISession session) {
		this.mDatabase = database;
		this.mEntityManager = entityManager;
		this.mSession = session;
		this.mSession.addObserver(this);
	}

	/**
	 * Initialisation de l'application.
	 */
	public void init() {
		// Init du look and feel de l'application
		this.initLookAndFeel();

		// Initialisation du répertoire d'échange
		this.initDirectory();

		// Initialisation de l'IHM
		this.initGui();
	}

	/**
	 * Initialisation du look and feel de l'application.
	 */
	protected void initLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initialisation de l'interface graphique.
	 */
	protected void initGui() {
		this.mConnexionView = new ConnexionView();
		ConnexionController connexionController = new ConnexionController(mConnexionView, mDatabase, mEntityManager, mSession);
		InscriptionController inscriptionController = new InscriptionController(mConnexionView, mDatabase, mEntityManager, mSession);
	}

	/**
	 * Initialisation du répertoire d'échange (depuis la conf ou depuis un file
	 * chooser). <br/>
	 * <b>Le chemin doit obligatoirement avoir été saisi et être valide avant de
	 * pouvoir utiliser l'application</b>
	 */
	protected void initDirectory() {
		this.initDirectory("src/main/resources/users/");
	}

	/**
	 * Indique si le fichier donné est valide pour servir de répertoire d'échange
	 *
	 * @param directory , Répertoire à tester.
	 */
	protected boolean isValideExchangeDirectory(File directory) {
		// Valide si répertoire disponible en lecture et écriture
		return directory != null && directory.exists() && directory.isDirectory() && directory.canRead()
				&& directory.canWrite();
	}

	/**
	 * Initialisation du répertoire d'échange.
	 *
	 * @param directoryPath
	 */
	protected void initDirectory(String directoryPath) {
		mExchangeDirectoryPath = directoryPath;
		mWatchableDirectory = new WatchableDirectory(directoryPath);
		mEntityManager.setExchangeDirectory(directoryPath);

		mWatchableDirectory.initWatching();
		mWatchableDirectory.addObserver(mEntityManager);
	}

	public void show() {
	}

	@Override
	public void notifyLogin(User connectedUser) {
		System.out.println("L'utilisateur " + connectedUser.getUserTag() + " est connecté!");
	}

	@Override
	public void notifyLogout() {
		System.out.println("Déconnexion réussie!");
	}
}
