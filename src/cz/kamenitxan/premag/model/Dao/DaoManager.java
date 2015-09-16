package cz.kamenitxan.premag.model.Dao;


import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import cz.kamenitxan.premag.model.User;

import java.sql.SQLException;

/**
 * Created by Kamenitxan (kamenitxan@me.com) on 16.09.15.
 */
public class DaoManager {
	private static final String databaseUrl = "jdbc:sqlite:pokuston.db";
	private static Dao<User, Integer> userDao = null;

	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			final ConnectionSource connectionSource = new JdbcConnectionSource(databaseUrl);
			userDao = com.j256.ormlite.dao.DaoManager.createDao(connectionSource, User.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Dao<User, Integer> getUserDao() {
		return userDao;
	}
}
