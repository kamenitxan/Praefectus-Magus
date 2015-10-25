package cz.kamenitxan.premag.view;

import cz.kamenitxan.premag.model.Dao.DaoManager;
import cz.kamenitxan.premag.model.School;
import cz.kamenitxan.premag.model.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kamenitxan (kamenitxan@me.com) on 25.10.15.
 */
public class Admin {
	public static ModelAndView dashboardGet(Request request, Response response) {
		Map<String, Object> data = new HashMap<>();
		data.put("menu", User.getMenuItems(request));

		return new ModelAndView(data, "admin/dashboard");
	}

	public static ModelAndView usersGet(Request request, Response response) {
		Map<String, Object> data = new HashMap<>();
		data.put("menu", User.getMenuItems(request));

		List<School> schools = new ArrayList<>();
		try {
			schools =  DaoManager.getSchoolDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		data.put("schools", schools);

		return new ModelAndView(data, "admin/users");
	}

	public static ModelAndView userDeleteGet(Request request, Response response) {
		int idS = Integer.valueOf(request.queryParams("idS"));
		int idU = Integer.valueOf(request.queryParams("idU"));

		if (User.getMenuItems(request).admin) {
			try {
				DaoManager.getSchoolDao().deleteById(idS);
				DaoManager.getUserDao().deleteById(idU);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return usersGet(request, response);
	}
}
