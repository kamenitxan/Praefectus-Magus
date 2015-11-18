package cz.kamenitxan.premag.view;

import cz.kamenitxan.premag.model.Dao.DaoManager;
import cz.kamenitxan.premag.model.School;
import cz.kamenitxan.premag.model.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kamenitxan (kamenitxan@me.com) on 20.10.15.
 */
public class Profile {
	public static ModelAndView profileViewGet(Request request, Response response) {
		return new ModelAndView(profileGetData(request), "profile");
	}

	public static Map<String, Object> profileGetData(Request request) {
		Map<String, Object> data = new HashMap<>();
		School school = null;
		try {
			school = DaoManager.getSchoolDao().queryBuilder().where().eq("email", request.session().attribute("useremail")).queryForFirst();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		data.put("school", school);
		data.put("menu", User.getMenuItems(request));
		return data;
	}
}
