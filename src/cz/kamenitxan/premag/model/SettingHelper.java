package cz.kamenitxan.premag.model;

import cz.kamenitxan.premag.model.Dao.DaoManager;

import java.sql.SQLException;

/**
 * Created by Kamenitxan (kamenitxan@me.com) on 21.11.15.
 */
public class SettingHelper {
	private static Settings instance;

	static {
		try {
			Settings s = DaoManager.getSettingsDao().queryForId(0);
			if (s != null) {
				SettingHelper.instance = s;
			} else {
				s = new Settings();
				instance = s;
				DaoManager.getSettingsDao().createIfNotExists(s);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static boolean isTeamsEdit() {
		return instance.isTeamsEdit();
	}

	public static void setTeamsEdit(boolean teamsEditDB) {
		instance.setTeamsEdit(teamsEditDB);
	}

	public static boolean isRefereeEnabled() {
		return instance.isRefereeEnabled();
	}

	public static void setRefereeEnabled(boolean refereeEnabledDB) {
		instance.setRefereeEnabled(refereeEnabledDB);
	}

	public static Settings getInstance() {
		return instance;
	}
}
