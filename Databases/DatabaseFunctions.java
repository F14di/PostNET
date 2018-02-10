package Databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatabaseFunctions {

	/*
	 * This class is used in order to perform queries / update the SQL database
	 */

	static Connection conn;
	int userType = 0;
	static List<Sign> tempSignList; // used in order to initialize the Sign class
	static Calendar postNetCalendar = Calendar.getInstance();

	public DatabaseFunctions(int port) {
		try {
			conn = connect(port);
			tempSignList = new ArrayList<>();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DatabaseFunctions() {

	}

	public static Connection connect(int port) throws Exception { // Connects the JDBC server
		String dbURL = "jdbc:sqlserver://localhost:" + port
				+ ";databaseName=PostNet;integratedSecurity=true;user=username ;password=password;";
		try {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {

				throw new Exception("Driver not found");
			}
			return DriverManager.getConnection(dbURL);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static int insertPublication(long AdID, Timestamp StartTime, long SignID, String FileName, String Email) {
		// Adds a single ad to the SQL database
		postNetCalendar.setTimeInMillis(StartTime.getTime());
		postNetCalendar.add(Calendar.SECOND, 30);
		Timestamp EndTime = new Timestamp(postNetCalendar.getTime().getTime());
		Statement insertSinglePublication = null;
		String addSinglePublication = "INSERT INTO AdDetails (AdID, StartTime, EndTime, AdPlayedOn, FileName, Email) "
				+ "VALUES ('" + AdID + "','" + StartTime + "','" + EndTime + "','" + SignID + "','" + FileName + "','"
				+ Email + "')";
		try {
			insertSinglePublication = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
		try {
			insertSinglePublication.executeUpdate(addSinglePublication);
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}

	public static int insertPublications(List<Ad> adsToInsert) {// Adds a list of ads to the SQL database
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		Statement insertPublications = null;
		String addPublications = "INSERT INTO AdDetails VALUES ";
		try {
			insertPublications = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}

		for (Ad thisAd : adsToInsert) {
			start.setTime(thisAd.getStartDate());
			end.setTime(thisAd.getEndDate());

			addPublications = addPublications + "(" + thisAd.getAdID() + "," + thisAd.getPublicationID() + ",'"
					+ new Timestamp(start.getTimeInMillis()) + "','" + new Timestamp(end.getTimeInMillis()) + "',"
					+ thisAd.getSign().getSignID() + ",'" + thisAd.getFileName() + "','" + thisAd.getEmail() + "'),";
		}
		addPublications = addPublications.substring(0, addPublications.length() - 1); // Removing the last comma
		try {
			insertPublications.executeUpdate(addPublications);
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}

	public static int insertRepeatedPublication(long PublicationID, long Rnumber, Timestamp FromDatetime,
			Timestamp ToDatetime) {
		Statement insertRepeatedPublication = null;
		String addRepeatedPublication = "INSERT INTO RepeatedPublication " + "VALUES ('" + PublicationID + "','"
				+ Rnumber + "','" + FromDatetime + "','" + ToDatetime + "')";
		try {
			insertRepeatedPublication = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
		try {
			insertRepeatedPublication.executeUpdate(addRepeatedPublication);
			// notification
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}

	}

	public static int insertPublisher(String email, String password, String creditCard) {
		// adds a new user registration to the users database in the SQL
		Statement userCheck = null;
		Statement newUser = null;
		ResultSet usersQuery = null;
		String userCheckQuery = "SELECT '" + email + "' FROM Users " + "WHERE Email = '" + email + "'";
		String addNewUser = "INSERT INTO Users (Email, Password, UserType, CreditCard) VALUES ('" + email + "','"
				+ password + "'," + 0 + " , '" + creditCard + "' )";
		try {
			userCheck = conn.createStatement();
			// connection successful
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
		try {
			usersQuery = userCheck.executeQuery(userCheckQuery);
			if (usersQuery.next()) {
				return 1;
			} else {
				try {
					newUser = conn.createStatement();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					newUser.executeUpdate(addNewUser);
					// Success! The user was added to PostNet
					return 0;
				} catch (SQLException e) {
					// The user cannot be added
					e.printStackTrace();
				}
				return 1;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			return 1;
		}
	}

	public static int loginCheck(String email, String password) { // check user details before login
		Statement userLoginCheck = null;
		Statement userAndPasswordCheck = null;
		ResultSet userAndPasswordQuery = null;
		String userCheckQuery = "SELECT '" + email + "' FROM Users " + "WHERE Email = '" + email + "'";
		String userAndPasswordCheckQuery = "SELECT * FROM Users WHERE Email = '" + email + "' AND Password = '"
				+ password + "'";
		try {
			userLoginCheck = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
		try {

			userAndPasswordQuery = userLoginCheck.executeQuery(userCheckQuery);
			if (userAndPasswordQuery.next()) {
				try {
					userAndPasswordCheck = conn.createStatement();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					userAndPasswordQuery = userAndPasswordCheck.executeQuery(userAndPasswordCheckQuery);
					if (userAndPasswordQuery.next()) {
						// Success
						return 0;
					} else {
						// wrong password
						return 1;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				// The user is not registered in PostNet
				return 1;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			return 1;
		}
		return 1;
	}

	public static List<User> displayAllUsers() {
		// returns all the users from the SQL database
		Databases.getUserList().clear();
		Statement displayUsers = null;
		ResultSet returnAllUsers = null;
		String displayUsersQuery = "SELECT * FROM Users";
		try {
			displayUsers = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			returnAllUsers = displayUsers.executeQuery(displayUsersQuery);
			while (returnAllUsers.next()) {
				Databases.getUserList().add(new User(returnAllUsers.getString(1), returnAllUsers.getString(2),
						returnAllUsers.getBoolean(3), returnAllUsers.getString(4), returnAllUsers.getInt(5)));
			}
			// Success! All Users are in the UsersArray
			return Databases.getUserList();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Databases.getUserList();
	}

	public static List<Sign> displayAllSigns() {
		// returns all the Signs from the SQL database
		Databases.getSignList().clear();
		Statement displaySigns = null;
		ResultSet returnAllSigns = null;
		String displaySignsQuery = "SELECT * FROM Signs";

		try {
			displaySigns = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			returnAllSigns = displaySigns.executeQuery(displaySignsQuery);
			while (returnAllSigns.next()) {
				Databases.getSignList().add(new Sign(returnAllSigns.getLong(1), returnAllSigns.getString(2),
						returnAllSigns.getString(3), returnAllSigns.getString(4)));
			}
			return Databases.getSignList();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Databases.getSignList();
	}

	public static List<Ad> returnAllAds() {
		// returns all the adds that are stored in the SQL database
		Statement allAds = null;
		ResultSet allAdsResults = null;
		String allAdsQuery = "SELECT AdID, PublicationID, StartTime, EndTime, SignID, FileName, Email, SignType, Location FROM AdDetails Ad JOIN Signs S ON Ad.AdPlayedOn = S.SignID ";
		List<Ad> _ARRAYLIST = new ArrayList<>();

		try {
			allAds = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {

			allAdsResults = allAds.executeQuery(allAdsQuery);
			while (allAdsResults.next()) {
				Timestamp startDate = allAdsResults.getTimestamp(3);
				Timestamp EndDate = allAdsResults.getTimestamp(4);

				_ARRAYLIST.add(new Ad(allAdsResults.getInt(1), allAdsResults.getInt(2), startDate, EndDate,
						allAdsResults.getLong(5), allAdsResults.getString(6), allAdsResults.getString(7),
						allAdsResults.getString(8), allAdsResults.getString(9)));
			}
			return _ARRAYLIST;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return _ARRAYLIST;
	}

	public static List<Ad> returnUserAds(String Email) {
		// returns all the ads that were added by "Email" username to the database
		List<Ad> userAds = new ArrayList<>();
		Statement displayUserAds = null;
		ResultSet returnUserAds = null;
		Calendar fromCalendar = Calendar.getInstance();
		Calendar untilCalendar = Calendar.getInstance();
		String displayUserAdsQuery = "SELECT AdID, PublicationID, StartTime, EndTime, SignID, FileName, Email, SignType, Location FROM AdDetails Ad JOIN Signs S ON Ad.AdPlayedOn = S.SignID "
				+ "WHERE Email = '" + Email + "'";
		try {
			displayUserAds = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			returnUserAds = displayUserAds.executeQuery(displayUserAdsQuery);
			while (returnUserAds.next()) {

				userAds.add(new Ad(returnUserAds.getInt(1), returnUserAds.getInt(2), returnUserAds.getTimestamp(3),
						returnUserAds.getTimestamp(4), returnUserAds.getLong(5), returnUserAds.getString(6),
						returnUserAds.getString(7), returnUserAds.getString(8), returnUserAds.getString(9)));
			}
			// Success! All user ads are in the AdArray object
			return userAds;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userAds;
	}

	public static long[] getMaxAdID() {
		// performed before an ad is inserted to the database.
		// returns the highest AdID
		long[] _maxIDs = new long[2];
		Statement MaxID = null;
		ResultSet returnMaxID = null;
		String returnMaxIDQuery = "SELECT MAX(AdID),MAX(PublicationID) FROM AdDetails";
		try {
			MaxID = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			returnMaxID = MaxID.executeQuery(returnMaxIDQuery);
			returnMaxID.next();
			_maxIDs[0] = returnMaxID.getLong(1);
			_maxIDs[1] = returnMaxID.getLong(2);

			return _maxIDs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return _maxIDs;
	}

	public static int deleteAds(List<Ad> deleteList) {
		// deletes a list of ads from the database
		Statement deleteAd = null;
		String deleteAdsList = "DELETE FROM AdDetails WHERE AdID= ";
		String OR = "";
		try {
			deleteAd = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}

		for (Ad deleteThis : deleteList) {
			OR = OR + deleteThis.getAdID() + " OR AdID= ";
		}

		try {
			deleteAdsList = deleteAdsList + OR;
			deleteAdsList = deleteAdsList.substring(0, deleteAdsList.length() - 10);
			deleteAd.executeUpdate(deleteAdsList);
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}

	}

	public static List<Long> distinctPublications(String username) {
		// returns all of the ads that were added by a user where the Publication ID is
		// distinct
		List<Long> distinctPublications = new ArrayList<>();
		Statement publicationIDs = null;
		ResultSet publicationIDsResult = null;
		String publicationIDsQuery = "SELECT DISTINCT PublicationID FROM AdDetails Ad join Signs S ON Ad.AdPlayedOn=s.SignID WHERE Email='"
				+ username + "'";
		try {
			publicationIDs = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {

			publicationIDsResult = publicationIDs.executeQuery(publicationIDsQuery);
			while (publicationIDsResult.next()) {
				distinctPublications.add(publicationIDsResult.getLong(1));
			}
			return distinctPublications;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return distinctPublications;
	}

	public static int updateUserBalance(String username, int debt) {
		// runs every time an ad is streamed in order to update the user account balance
		Statement insertDebt = null;
		String addDebt = "UPDATE USERS SET Debt = " + debt + " WHERE Email = '" + username + "'";
		try {
			insertDebt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
		try {
			insertDebt.executeUpdate(addDebt);
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 1;
	}

}