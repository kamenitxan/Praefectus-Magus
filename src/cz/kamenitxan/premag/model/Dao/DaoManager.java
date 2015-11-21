package cz.kamenitxan.premag.model.Dao;


import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import cz.kamenitxan.premag.model.*;

import java.sql.SQLException;

/**
 * Created by Kamenitxan (kamenitxan@me.com) on 16.09.15.
 */
public class DaoManager {
	private static final String databaseUrl = "jdbc:sqlite:pokuston.db";
	private static Dao<User, Integer> userDao = null;
	private static Dao<School, Integer> schoolDao = null;
	private static Dao<Team, Integer> teamDao = null;
	private static Dao<Participant, Integer> participantDao = null;
	private static Dao<Settings, Integer> settingsDao = null;

	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			final ConnectionSource connectionSource = new JdbcConnectionSource(databaseUrl);
			userDao = com.j256.ormlite.dao.DaoManager.createDao(connectionSource, User.class);
			if (!userDao.isTableExists()) {
				TableUtils.createTable(connectionSource, User.class);
			}
			schoolDao = com.j256.ormlite.dao.DaoManager.createDao(connectionSource, School.class);
			if (!schoolDao.isTableExists()) {
				TableUtils.createTable(connectionSource, School.class);
			}
			teamDao = com.j256.ormlite.dao.DaoManager.createDao(connectionSource, Team.class);
			if (!teamDao.isTableExists()) {
				TableUtils.createTable(connectionSource, Team.class);
			}
			participantDao = com.j256.ormlite.dao.DaoManager.createDao(connectionSource, Participant.class);
			if (!participantDao.isTableExists()) {
				TableUtils.createTable(connectionSource, Participant.class);
			}
			settingsDao = com.j256.ormlite.dao.DaoManager.createDao(connectionSource, Settings.class);
			if (!settingsDao.isTableExists()) {
				TableUtils.createTable(connectionSource, Settings.class);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Dao<User, Integer> getUserDao() {
		return userDao;
	}

	public static Dao<School, Integer> getSchoolDao() {
		return schoolDao;
	}

	public static Dao<Team, Integer> getTeamDao() {
		return teamDao;
	}

	public static Dao<Participant, Integer> getParticipantDao() {
		return participantDao;
	}

	public static Dao<Settings, Integer> getSettingsDao() {
		return settingsDao;
	}
}
