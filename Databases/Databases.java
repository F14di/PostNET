package Databases;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import Controller.Controller;

public class Databases {

	private static Databases instance;
	DatabaseFunctions DBObject = new DatabaseFunctions();
	Date currentTime = new Date();

	private static List<Sign> signList = new ArrayList<>();
	private static List<Ad> adList = new ArrayList<>();
	private static List<User> userList = new ArrayList<>();

	/*
	 * The Databases class contains three main array Lists that hold the Signs
	 * database, the Ads database and the users database those arrayLists are
	 * initialized and populated as the program is started, and de-populated
	 * whenever it is terminated
	 */

	private Databases() {
		signList.addAll(DBObject.displayAllSigns());
		userList.addAll(DBObject.displayAllUsers());
	}

	public static Databases getInstance() {
		if (instance == null) {
			instance = new Databases();
		}
		return instance;
	}

	public static List<Sign> getSignList() {
		return signList;
	}

	public static List<User> getUserList() {
		return userList;
	}

	public static List<Ad> getAdList() {
		return adList;
	}

	public List<Ad> returUserAd(String userName) {
		// returns the ads arrayList that belongs to "username" user.
		return userList.stream().filter(user -> user.getEmail().equals(userName)).findFirst().get().getUsersAdList();
	}

	public List<Ad> updateAdDatabase() {
		// fired once every 10 minutes
		int price;
		List<Ad> adsToBeDeleted = new ArrayList<>();// ArrayList to hold the ads that are outdated.
		adsToBeDeleted = adList.stream().filter(e -> e.getStartDate().before((currentTime)))
				.collect(Collectors.toList());

		if (!adsToBeDeleted.isEmpty()) {
			for (Ad thisAd : adsToBeDeleted) {
				String username = thisAd.getEmail();
				price = thisAd.getSign().getUrbanRural().equals("Rural") ? 50 : 100; // before every ad gets deleted,
																						// the publisher
																						// will be charged 50$ for Rural
																						// ads and 100$ for
																						// Urban ads
				userList.stream().filter(e -> e.getEmail().equals(username)).findFirst().get()
						.updateAccountBalance(price);
				Controller.updateUserBalance(username, userList.stream().filter(e -> e.getEmail().equals(username))
						.findFirst().get().getAccountBalance());
			}
		}
		return adsToBeDeleted;
	}
}