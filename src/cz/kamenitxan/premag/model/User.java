package cz.kamenitxan.premag.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Kamenitxan (kamenitxan@me.com) on 16.09.15.
 */
@DatabaseTable
public class User {
	@DatabaseField(generatedId = true)
	private int id = 0;
	@DatabaseField
	private String userName;
	@DatabaseField
	private String password;
	@DatabaseField
	private String email;
	@DatabaseField
	private boolean enabled;
	@DatabaseField
	private boolean isReferee = false;
	@DatabaseField
	private Year RefereeYear = null;
	@DatabaseField
	private int RefereeNumber = 0;

	public User() {
	}

	public User(String userName, String password, String email) {
		this.userName = userName;
		this.password = password;
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isReferee() {
		return isReferee;
	}

	public void setIsReferee(boolean isReferee) {
		this.isReferee = isReferee;
	}

	public Year getRefereeYear() {
		return RefereeYear;
	}

	public void setRefereeYear(Year refereeYear) {
		RefereeYear = refereeYear;
	}

	public int getRefereeNumber() {
		return RefereeNumber;
	}

	public void setRefereeNumber(int refereeNumber) {
		RefereeNumber = refereeNumber;
	}
}
