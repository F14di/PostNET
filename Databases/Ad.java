package Databases;

import java.util.Date;

public class Ad {

	static long counter = DatabaseFunctions.getMaxAdID()[0]++; // next ad to be inserted will hold the maximum ID number
																// in the database incremented by 1 .
	int adID = 0;
	private String adType; // text / JPG / MP4
	private String email; // publisher username / Email
	Date startDate;
	Date endDate;
	Sign sign; // streaming Sign
	private String filename;
	long publicationID;
	/*
	 * In case a user chooses to upload several ads in a series /
	 * continuous-repetitive Ad for a period of time, all of the publications added
	 * get the same AdID, but every publication gets a unique publicationID.
	 */

	Databases DBObject = Databases.getInstance();

	// constructor used to add a new ad from the GUI
	public Ad(Date startDate, Date endDate, long publicationID, String adType, String email, long signID,
			String filename) {
		this.adType = adType;
		this.publicationID = publicationID;
		this.email = email;
		this.startDate = startDate;
		this.filename = filename;
		this.endDate = endDate;
		adID = (int) counter;
		this.sign = Databases.getSignList().stream().filter(e -> e.getSignID() == signID).findFirst().get();
		counter++;
	}

	// constructor used to upload ads from the SQL
	public Ad(long AdID, int publicationID, Date startDate, Date endDate, long signID, String filename, String email,
			String format, String location) {
		this.publicationID = publicationID;
		this.startDate = startDate;
		this.endDate = endDate;
		this.adType = format;
		this.email = email;
		this.adID = (int) AdID;
		this.filename = filename;
		this.sign = Databases.getSignList().stream().filter(e -> e.getSignID() == signID).findFirst().get();
	}

	public static long getCounter() {
		return counter;
	}

	public long getPublicationID() {
		return publicationID;
	}

	public String getFileName() {
		return filename;
	}

	public long getAdID() {
		return adID;
	}

	public Sign getSign() {
		return sign;
	}

	public String getAdType() {
		return adType;
	}

	public String getEmail() {
		return email;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	@Override
	public String toString() {
		return " " + publicationID;
	}

}