package cz.kamenitxan.premag.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Kamenitxan (kamenitxan@me.com) on 16.09.15.
 */
@DatabaseTable
public class Settings {
	@DatabaseField(id = true)
	private static int id = 0;
	@DatabaseField
	private boolean teamsEditDB = false;
	@DatabaseField
	private boolean refereeEnabledDB = false;


	public Settings() {
	}

	public static int getId() {
		return id;
	}

	public boolean isTeamsEdit() {
		return teamsEditDB;
	}

	public void setTeamsEdit(boolean teamsEditDB) {
		this.teamsEditDB = teamsEditDB;
	}

	public boolean isRefereeEnabled() {
		return refereeEnabledDB;
	}

	public void setRefereeEnabled(boolean refereeEnabledDB) {
		this.refereeEnabledDB = refereeEnabledDB;
	}
}
