package cz.kamenitxan.premag.view;

import cz.kamenitxan.premag.model.*;
import cz.kamenitxan.premag.model.Dao.DaoManager;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.*;

/**
 * Created by Kamenitxan (kamenitxan@me.com) on 22.10.15.
 */
public class Teams {
	public static ModelAndView teamListGet(Request request, Response response) {
		Map<String, Object> data = new HashMap<>();
		String yearS = request.queryParams("rok");
		if (yearS == null) {
			yearS = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		}
		List<Team> teams = null;
		List<Team> years = null;
		try {
			final School school =  DaoManager.getSchoolDao().queryBuilder().where().eq("user_id", request.session().attribute("userid")).queryForFirst();
			teams = DaoManager.getTeamDao().queryBuilder().orderBy("entryOrder", true).where()
					.eq("year", yearS).and().eq("school_id", school.getId()).query();
			years = DaoManager.getTeamDao().queryBuilder().distinct().selectColumns("year").query();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		boolean canAdd = false;
		if (SettingHelper.isTeamsEdit() && Integer.valueOf(yearS) == Calendar.getInstance().get(Calendar.YEAR)) {
			canAdd = true;
		}

		data.put("teams", teams);
		data.put("years", years);
		data.put("selected", yearS);
		data.put("menu", User.getMenuItems(request));
		data.put("canAdd", canAdd);
		return new ModelAndView(data, "teams");
	}

	public static ModelAndView teamListPost(Request request, Response response) {
		Set<String> params = request.queryParams();
		for (String param : params) {
			if (param.contains("tm")) {
				Integer id = Integer.valueOf(param.replace("tm", ""));
				try {
					Team team = DaoManager.getTeamDao().queryForId(id);
					team.setEntryOrder(Integer.valueOf(request.queryParams(param)));
					DaoManager.getTeamDao().update(team);
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		}
		return teamListGet(request, response);
	}

	public static ModelAndView teamPrintGet(Request request, Response response) {
		Map<String, Object> data = new HashMap<>();
		String yearS = request.queryParams("rok");
		if (yearS == null) {
			yearS = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		}
		School school = null;
		List<Team> teams = null;
		try {
			school =  DaoManager.getSchoolDao().queryBuilder().where().eq("user_id", request.session().attribute("userid")).queryForFirst();
			teams = DaoManager.getTeamDao().queryBuilder().orderBy("entryOrder", true).where()
					.eq("year", yearS).and().eq("school_id", school.getId()).query();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		data.put("school", school);
		data.put("teams", teams);
		return new ModelAndView(data, "teamsPrint");
	}

	public static ModelAndView teamAddGet(Request request, Response response) {
		Map<String, Object> data = new HashMap<>();
		data.put("menu", User.getMenuItems(request));
		return new ModelAndView(data, "teamsAdd");
	}

	public static ModelAndView teamAddPost(Request request, Response response) {
		Map<String, Object> data = new HashMap<>();
		data.put("menu", User.getMenuItems(request));

		Participant participant1 = new Participant() {{
			setFirstName(request.queryParams("firstname1"));
			setLastName(request.queryParams("lastname1"));
			setBirthDate(request.queryParams("date1"));
			setClassName(request.queryParams("class1"));
		}};
		Participant participant2 = new Participant() {{
			setFirstName(request.queryParams("firstname2"));
			setLastName(request.queryParams("lastname2"));
			setBirthDate(request.queryParams("date2"));
			setClassName(request.queryParams("class2"));
		}};

		School school = null;
		try {
			DaoManager.getParticipantDao().create(participant1);
			DaoManager.getParticipantDao().create(participant2);
			school =  DaoManager.getSchoolDao().queryBuilder().where().eq("user_id", request.session().attribute("userid")).queryForFirst();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		int entryOrder = 0;
		try {
			entryOrder = Integer.parseInt(request.queryParams("order"));
		} catch (NumberFormatException ex) {
			List<String> errors = new ArrayList<>();
			errors.add("Pořadí není číslo");
			data.put("errors", errors);
			return new ModelAndView(data, "teamsAdd");
		}
 		Team team = new Team() {{
			setParticipant1(participant1);
			setParticipant2(participant2);
			setExperiment(request.queryParams("expname"));
			setRequirments(request.queryParams("req"));
			setLunch("true".equals(request.queryParams("lunch")));
			setYear(Calendar.getInstance().get(Calendar.YEAR));
		}};
		team.setSchool(school);
		team.setEntryOrder(entryOrder);

		try {
			DaoManager.getTeamDao().create(team);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return new ModelAndView(data, "teamsAdd");
	}

	public static ModelAndView teamDeleteGet(Request request, Response response) {
		String id = request.queryParams("id");
		int year = Calendar.getInstance().get(Calendar.YEAR);

		try {
			Team team = DaoManager.getTeamDao().queryForId(Integer.valueOf(id));
			if (team.getYear() == year) {
				DaoManager.getTeamDao().delete(team);
			}
		} catch (SQLException | NumberFormatException ignored) {

		}

		return teamListGet(request, response);
	}

	/** RESULTS *******************************************************************************************************/

	public static ModelAndView resultListGet(Request request, Response response) {
		Map<String, Object> data = new HashMap<>();
		String yearS = request.queryParams("rok");
		if (yearS == null) {
			yearS = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		}
		List<Team> teams = null;
		List<Team> years = null;
		try {
			final School school =  DaoManager.getSchoolDao().queryBuilder().where().eq("user_id", request.session().attribute("userid")).queryForFirst();
			teams = DaoManager.getTeamDao().queryBuilder().where()
					.eq("year", Integer.valueOf(yearS)).and().eq("school_id", school.getId()).query();
			years = DaoManager.getTeamDao().queryBuilder().distinct().selectColumns("year").query();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		data.put("years", years);
		data.put("selected", yearS);
		data.put("teams", teams);
		data.put("menu", User.getMenuItems(request));
		return new ModelAndView(data, "results");
	}

}
