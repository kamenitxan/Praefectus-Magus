package cz.kamenitxan.premag.view;

import cz.kamenitxan.premag.model.Dao.DaoManager;
import cz.kamenitxan.premag.model.Participant;
import cz.kamenitxan.premag.model.School;
import cz.kamenitxan.premag.model.Team;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kamenitxan (kamenitxan@me.com) on 22.10.15.
 */
public class Teams {
	public static ModelAndView teamListGet(Request request, Response response) {
		Map<String, Object> data = new HashMap<>();
		String yearS = request.params(":year");
		List<Team> teams = null;
		try {
			final School school =  DaoManager.getSchoolDao().queryBuilder().where().eq("user_id", request.session().attribute("userid")).queryForFirst();
			teams = DaoManager.getTeamDao().queryBuilder().where()
					.eq("year", Integer.valueOf(yearS)).and().eq("school_id", school.getId()).query();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		data.put("teams", teams);
		return new ModelAndView(data, "teams");
	}

	public static ModelAndView teamAddGet(Request request, Response response) {
		Map<String, Object> data = new HashMap<>();
		return new ModelAndView(data, "teamsAdd");
	}

	public static ModelAndView teamAddPost(Request request, Response response) {
		Map<String, Object> data = new HashMap<>();

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

		Team team = new Team() {{
			setParticipant1(participant1);
			setParticipant2(participant2);
			setExperiment(request.queryParams("expname"));
			setRequirments(request.queryParams("req"));
			setLunch("true".equals(request.queryParams("lunch")));
			setYear(Calendar.getInstance().get(Calendar.YEAR));
		}};
		team.setSchool(school);

		try {
			DaoManager.getTeamDao().create(team);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return new ModelAndView(data, "teamsAdd");
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
		return new ModelAndView(data, "results");
	}

}
