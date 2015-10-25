package cz.kamenitxan.premag.view;

import cz.kamenitxan.premag.model.Dao.DaoManager;
import cz.kamenitxan.premag.model.User;
import org.mindrot.jbcrypt.BCrypt;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.*;

/**
 * Created by Kamenitxan (kamenitxan@me.com) on 08.09.15.
 */
public class Login {
	public static ModelAndView indexViewGet(Request request, Response response) {
		return new ModelAndView(new HashMap(), "index");
	}

	public static ModelAndView indexViewPost(Request request, Response response) {
		Map<String, Object> data = new HashMap<>();
		List<String> errors = new ArrayList<>();
		String userName = request.queryParams("email");
		String password = request.queryParams("pass");
		User user = null;
		try {
			user = DaoManager.getUserDao().queryBuilder().where().eq("email", userName).queryForFirst();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (user == null) {
			errors.add("Neexistující uživatel");
			data.put("errors", errors);
			return new ModelAndView(data, "index");
		}
		if (BCrypt.checkpw(password, user.getPassword())) {
			request.session(true);
			request.session().attribute("useremail", user.getEmail());
			request.session().attribute("userid", user.getId());
			request.session().attribute("ir", user.isReferee());
			request.session().attribute("ia", user.isAdmin());

			return Profile.profileViewGet(request, response);
		} else {
			errors.add("Špatné heslo");
			data.put("errors", errors);
			return new ModelAndView(data, "index");
		}
	}

	public static ModelAndView logOutViewGet(Request request, Response response) {
		request.session().removeAttribute("useremail");
		request.session().removeAttribute("userid");
		return new ModelAndView(new HashMap(), "index");
	}


	public static boolean isAuthenticated(Request request, Response response) {
		//request.session().attribute("auth");
		if (request.session().attribute("useremail") == null) {
			return false;
		} else return true;

	}

	public static boolean isProtected(Request request) {
		String path = request.pathInfo();
		System.out.println(request.pathInfo());
		if (path.equals("/") || path.equals("/registrace") || path.contains("/static")) {
			return false;
		} else {
			return true;
		}
	}
}
