package Databases;

import java.util.ArrayList;
import java.util.List;

public class Sign {

	private final long signID;
	private final String type;
	private final String Location;
	private final String UrbanRural;
	private List<Ad> thisSignAds = new ArrayList<>(); // an ArrayList that stores the ads to be streamed on this Sign

	public Sign(long signID, String type, String location, String UrbanRural) {
		this.signID = signID;
		this.Location = location;
		this.type = type;
		this.UrbanRural = UrbanRural;
	}

	public long getSignID() {
		return signID;
	}

	public String getType() {
		return type;
	}

	public List<Ad> getThisSignAds() {
		return thisSignAds;
	}

	public String getLocation() {
		return Location;
	}

	public String getUrbanRural() {
		return UrbanRural;
	}

	@Override
	public String toString() {
		return " " + signID;
	}

}