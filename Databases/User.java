package Databases;

import java.util.ArrayList;
import java.util.List;

public class User {

	private String email;
	private String password;
	private final boolean isOfficial;
	private String creditCard;
	private int AccountBalance;
	private List<Ad> usersAdList = new ArrayList<>(); // all of the Ads by this user

	public User(String email, String password, boolean isOfficial, String creditCard, int AccountBalance) {
		this.email = email;
		this.password = password;
		this.isOfficial = isOfficial;
		this.creditCard = creditCard;
		this.AccountBalance = AccountBalance;
	}

	public void setUsersAdList(List<Ad> usersAdList) {
		this.usersAdList = usersAdList;
	}

	public int getAccountBalance() {
		return AccountBalance;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public List<Ad> getUsersAdList() {
		return usersAdList;
	}

	public boolean isOfficial() {
		return isOfficial;
	}

	@Override
	public String toString() {
		return email + " " + password + " " + isOfficial;
	}

	public void updateAccountBalance(int price) {
		this.AccountBalance += price;
	}
}