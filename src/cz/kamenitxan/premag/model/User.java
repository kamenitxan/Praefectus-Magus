package cz.kamenitxan.premag.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import spark.Request;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Kamenitxan (kamenitxan@me.com) on 16.09.15.
 */
@DatabaseTable
public class User {
	@DatabaseField(generatedId = true)
	private int id = 0;
	@DatabaseField
	private String password;
	@DatabaseField
	private String email;
	@DatabaseField
	private boolean enabled = false;
	@DatabaseField
	private boolean isReferee = false;
	@DatabaseField
	private int RefereeNumber = 0;
	@DatabaseField
	private boolean isAdmin = false;
	@DatabaseField
	private String verification = "";


	public User() {
	}

	public User(String email, String password) {
		this.password = password;
		this.email = email;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			byte byteData[] = md.digest();
			StringBuffer sb = new StringBuffer();
			for (byte aByteData : byteData) {
				sb.append(Integer.toString((aByteData & 0xff) + 0x100, 16).substring(1));
			}
			this.verification = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			this.verification = "verf" + email.length();
		}
	}

	public int getId() {
		return id;
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

	public int getRefereeNumber() {
		return RefereeNumber;
	}

	public void setRefereeNumber(int refereeNumber) {
		RefereeNumber = refereeNumber;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getVerification() {
		return verification;
	}

	public static MenuItems getMenuItems(Request request) {
		MenuItems mi = new MenuItems();
		if (request.session().attribute("ir")) {
			mi.referee = true;
		}
		if (request.session().attribute("ia")) {
			mi.admin = true;
		}
		return mi;
	}
}
