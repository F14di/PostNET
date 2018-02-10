package GUI;

import java.util.Date;

import javax.swing.JFrame;

import Controller.Controller;
import Databases.Ad;
import Databases.Sign;

/*
 * Whenever a user chooses to modify an ad, a new ad dialog shows up, the user can then 
 * modify his ad details. whenever the user hits finish, if the ad is accepted in the 
 * database, the old (modified) ad is deleted, and a new ad with the new parameters is added
 * */
public class modifyAd extends newAdDialogUI {
	Controller control = Controller.getInstance();

	private static final long serialVersionUID = 1L;
	private long publicationID;

	public modifyAd(JFrame parent, String userName, long publicationID) {
		super(parent, userName);
		this.publicationID = publicationID;
		getValues();

	}

	public void getValues() {

		Ad thisTempAd = control.returnAdsOfThisPublicationID(publicationID);
		String filename = thisTempAd.getFileName();
		String adType = thisTempAd.getAdType();
		Sign adPlayedOn = thisTempAd.getSign();
		Date startDate = thisTempAd.getStartDate();
		Date endDate = thisTempAd.getEndDate();
		filenameSetter(filename);
		locationSetter(adPlayedOn.getLocation());
		typeSetter(adType);

	}

	public void filenameSetter(String fileName) {
		browseTextPane.setText(fileName);
	}

	public void locationSetter(String location) {
		switch (location) {
		case "Haifa":
			chckbxHaifa.setSelected(true);
		case "Akko":
			chckbxAkko.setSelected(true);
		case "Tiberias":
			chckbxTiberias.setSelected(true);
		case "Galile":
			chckbxGalile.setSelected(true);
		case "Nazareth":
			chckbxNazareth.setSelected(true);
		case "DeadSea":
			chckbxDeadsea.setSelected(true);
		case "Tel Aviv":
			chckbxEilat.setSelected(true);
		case "Eilat":
			chckbxEilat.setEnabled(true);
		}
		LocationSignFilter(true, location);
	}

	public void typeSetter(String type) {
		switch (type) {
		case "Text":
			rdbtnText.setSelected(true);
		case "Graphic":
			rdbtnGraphic.setSelected(true);

		}
		SignTypeFilter(true, type);
	}

	public void signSetter() {

	}
}