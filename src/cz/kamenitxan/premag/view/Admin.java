package cz.kamenitxan.premag.view;

import cz.kamenitxan.premag.model.Dao.DaoManager;
import cz.kamenitxan.premag.model.School;
import cz.kamenitxan.premag.model.Team;
import cz.kamenitxan.premag.model.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.*;

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

	public static ModelAndView userTeamsGet(Request request, Response response) {
		Map<String, Object> data = new HashMap<>();
		String schoolId = request.queryParams("schoolId");
		String yearS = request.queryParams("rok");
		if (yearS == null) {
			yearS = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		}
		List<Team> teams = null;
		List<Team> years = null;
		try {
			final School school =  DaoManager.getSchoolDao().queryForId(Integer.valueOf(schoolId));
			teams = DaoManager.getTeamDao().queryBuilder().where()
					.eq("year", Integer.valueOf(yearS)).and().eq("school_id", school.getId()).query();
			years = DaoManager.getTeamDao().queryBuilder().distinct().selectColumns("year").query();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		data.put("years", years);
		data.put("schoolId", schoolId);
		data.put("selected", yearS);
		data.put("teams", teams);
		data.put("menu", User.getMenuItems(request));
		return new ModelAndView(data, "admin/userTeams");
	}

	public static ModelAndView teamsGet(Request request, Response response) {
		Map<String, Object> data = new HashMap<>();
		String yearS = request.queryParams("rok");
		if (yearS == null) {
			yearS = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		}
		List<Team> teams = null;
		List<Team> years = null;
		try {
			teams = DaoManager.getTeamDao().queryBuilder().where()
					.eq("year", Integer.valueOf(yearS)).query();
			years = DaoManager.getTeamDao().queryBuilder().distinct().selectColumns("year").query();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		data.put("years", years);
		data.put("selected", yearS);
		data.put("teams", teams);
		data.put("menu", User.getMenuItems(request));
		return new ModelAndView(data, "admin/participants");
	}
}
