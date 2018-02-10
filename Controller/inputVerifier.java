package Controller;

import java.util.Calendar;
import java.util.Date;

import javax.swing.JTextPane;

import Databases.Ad;

public class inputVerifier {
	Calendar calendar = Calendar.getInstance();
	Controller control = Controller.getInstance();

	public boolean checkIfSignIsBusy(Date startDate, Date endDate, long signID) {
		if (control.returnAdsOnThisSign(signID).isEmpty())
			return false;
		for (Ad thisAd : control.returnAdsOnThisSign(signID)) {// iterates over the ads to be inserted . checks if the
																// signs are busy
			if (thisAd.getEndDate().after(startDate)) {
				if (thisAd.getStartDate().before(endDate)) {
					return true;
				}
			}
		}
		return false;

	}

	public boolean fileTypeCheck(String fileName) {
		fileName = fileName.substring(0, fileName.length() - 2);
		String[] parsedFileName = fileName.split("\\.");

		switch (parsedFileName[1]) { // returns true if the file is of type .txt / .MP4 / .JPG -- returns false
										// otherwise
		case "txt":
		case "MP4":
		case "jpg":
			return true;
		default:
			return false;
		}

	}

	public boolean checkTimeInputValidity(Date startDate) {
		if (startDate.before(calendar.getTime())) { // compares the start date to the current time.
			return false; // returns false if start time is in the past
		}
		return true;
	}

	public boolean checkEndDate(long startDate, long endDate) {
		if (startDate > endDate) { // compares start date to end date.
			return false; // returns false if start date is before end date
		}
		return true;
	}

	public boolean checkIfFileUploaded(JTextPane browseField) {
		if (browseField.getText().isEmpty()) {
			return false;
		}
		return true;
	}

}