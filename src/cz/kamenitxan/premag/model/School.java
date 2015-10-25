package cz.kamenitxan.premag.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Kamenitxan (kamenitxan@me.com) on 08.09.15.
 */
@DatabaseTable
public class School {
	@DatabaseField(generatedId = true)
	private int Id;
	@DatabaseField
	private String name = "";
	@DatabaseField
	private String address = "";
	@DatabaseField
	private String email = "";
	@DatabaseField
	private String contactName = "";
	@DatabaseField
	private String contactAddress = "";
	@DatabaseField
	private String phone = "";
	@DatabaseField
	private String fax = "";
	@DatabaseField(foreign = true, foreignAutoRefresh = true)
	private User user = null;

	public School() {
	}

	public int getId() {
		return Id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
