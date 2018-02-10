package Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import Databases.Ad;
import Databases.DatabaseFunctions;
import Databases.Databases;
import Databases.Sign;
import Databases.User;

public class Controller {

	/*
	 * 
	 * the controller connects between the GUI and the Databases all of the data
	 * entered by the user to the databases passes through the controller class
	 * 
	 * 
	 */

	private static Controller instance;

	private static int port = 1433;
	static DatabaseFunctions DBfunctionsControl = new DatabaseFunctions(port); // create a new DBFunctions instance and
	Databases DatabaseObject = Databases.getInstance(); // get an instance of the Databases
	Calendar postNetCal = Calendar.getInstance();
	private final int MINUTES = 10; // remove ads that are out of date every MINUTES minutes
	Timer timer = new Timer();
	Function<String, Predicate<User>> usersFilter = pivot -> user -> user.getEmail().equals(pivot); // returns a

	private Controller() throws IOException { // singleton class. initialized after the user clicks log-in
		if (connect()) { // if connection was successfully established, uploads the databases from the
							// SQL
			// DBfunctionsControl = new DatabaseFunctions();
			Comparator<Ad> byDate = (Ad Ad1, Ad Ad2) -> Ad1.getStartDate().compareTo(Ad2.getStartDate()); // Comparator
																											// on AdList
																											// - by
																											// startDate

			DatabaseObject.getAdList().addAll(DBfunctionsControl.returnAllAds()); // upload all ads from the SQL to the
																					// AdList in the databases class
			DatabaseObject.getAdList().sort(byDate);// sort adList by the startDate

			for (Ad thisAd : DatabaseObject.getAdList()) { // adds each ad to the relevant adsListBySign
				long signID = thisAd.getSign().getSignID();
				DatabaseObject.getSignList().stream().filter(e -> e.getSignID() == signID).findFirst().get()
						.getThisSignAds().add(thisAd);
			}

			new Thread() {
				public void run() { // removes ads that are in the past from the database
					updateAdDatabase();
				}
			}.start();
		} else
			return;
	}

	public static Controller getInstance() {
		if (instance == null) {
			try {
				instance = new Controller();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	public boolean connect() throws IOException {
		try {
			new DatabaseFunctions(port);
			return true;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return false;
	}

	/*
	 * public void DBrefresh() throws Exception {
	 * DatabaseFunctions.closeConnection(); connect(); }
	 */

	public List<Ad> getAdList() {
		return Databases.getAdList();
	}

	public List<Sign> getAllSigns() {
		return DatabaseObject.getSignList();
	}

	public User returnUser(String username) {
		// returns the user "username" from the users ArrayList
		return Databases.getUserList().stream().filter(e -> e.getEmail().equals(username)).findFirst().get();
	}

	public Databases getDatabaseObject() {
		return DatabaseObject;
	}

	public void updateAdDatabase() { // a thread to delete the ads that are out of date
										// from database once every 10 minutes
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				removeAds(DatabaseObject.updateAdDatabase());
			}
		}, 0, 1000 * 60 * MINUTES);
	}

	public boolean newUser(String email, String password, String creditCard, int userType) {
		if (DBfunctionsControl.insertPublisher(email, password, creditCard) == 0) { // add new registration to SQL
																					// database and to
			// the users arrayList
			Databases.getUserList().add(new User(email, password, false, creditCard, 0));
			return true;
		} else
			return false;
	}

	public boolean checkLogin(String email, String password) {
		if (DBfunctionsControl.loginCheck(email, password) == 0) {
			return true;
		} else
			return false;
	}

	public List<Ad> AdsByThisUser(String username) { // find the Username in the users database, get his AdList
		return DatabaseObject.getUserList().stream().filter(e -> e.getEmail().equals(username)).findFirst().get()
				.getUsersAdList();
	}

	public void insertNewAd(List<Ad> tempAdArrayList, String username) {
		// Add a List of new Ads (tempAdArrayList ) to the databases
		new Thread() {
			public void run() {
				// add the new ads to the Ads ArrayLists
				AdsByThisUser(username).addAll(tempAdArrayList);
				DatabaseObject.getAdList().addAll(tempAdArrayList);
			}
		}.start();

		new Thread() {
			public void run() {
				// add the new ads to the SQL database
				DBfunctionsControl.insertPublications(tempAdArrayList);
			}
		}.start();
	}

	public void addListOfListsOfAds(List<List<Ad>> continuousAdsList, String username) {
		// for continuous ads, a list of lists of ads should be added to the databases
		// List of dates for every sign chosen

		for (List<Ad> ThisList : continuousAdsList) {
			// iterates over the specified dates, insert the ads according to the chosen
			// signs
			insertNewAd(ThisList, username);
		}
	}

	public void removeAds(List<Ad> removeAds, String username) {
		// removes the ads that belongs to "username" user
		Databases.getUserList().stream().filter(usersFilter.apply(username)).findFirst().get().getUsersAdList()
				.removeAll(removeAds);
		DBfunctionsControl.deleteAds(removeAds);
	}

	public void removeAds(List<Ad> removeAds) {
		// used in order to remove ads that are in the past
		Databases.getAdList().removeAll(removeAds);
		if (!removeAds.isEmpty())
			DBfunctionsControl.deleteAds(removeAds);

	}

	public void uploadUserAds(String username) {
		// runs after a user logs in. Uploads his ads to the ArrayList
		Databases.getUserList().stream().filter(user -> user.getEmail().equals(username)).findFirst().get()
				.getUsersAdList().addAll(DBfunctionsControl.returnUserAds(username));
	}

	public List<Ad> distinctPublications(String username) {
		// returns all the ads by "username" user where publicationID is distinct
		List<Long> PublicationIDsList = DBfunctionsControl.distinctPublications(username);
		List<Ad> filteredAdList = new ArrayList<>();

		for (Long publicationID : PublicationIDsList) {
			// gets the Ads that have the publicationIDs that are in the list
			// PublicationIDsList
			filteredAdList.add(DatabaseObject.getUserList().stream().filter(name -> name.getEmail().equals(username))
					.findFirst().get().getUsersAdList().stream().filter(ID -> ID.getPublicationID() == publicationID)
					.findFirst().get());
		}
		return filteredAdList;

	}

	public static void updateUserBalance(String username, int debt) {
		DBfunctionsControl.updateUserBalance(username, debt);
	}

	public List<Ad> returnAdsOnThisSign(long SignID) {

		try {
			return DatabaseObject.getAdList().stream().filter(e -> e.getSign().getSignID() == SignID).findFirst().get()
					.getSign().getThisSignAds();

		} catch (NoSuchElementException e) {
			return new ArrayList<>();
		}
	}

	public Ad returnAdsOfThisPublicationID(long publicationID) {
		// used in order to filter all the ads that hold the PublicationID value
		return getAdList().stream().filter(e -> e.getPublicationID() == publicationID).findFirst().get();
	}

	public List<Ad> AdsofThisPublicationID(long publicationID) {
		ArrayList<Ad> allPublicationIDs = new ArrayList<>();
		// allPublicationIDs= (ArrayList<Ad>) getAdList().stream().filter(e ->
		// e.getPublicationID() == publicationID).collect(Collectors.toList());
		return (ArrayList<Ad>) getAdList().stream().filter(e -> e.getPublicationID() == publicationID)
				.collect(Collectors.toList());

	}

}