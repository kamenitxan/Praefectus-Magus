package cz.kamenitxan.premag.view;

import cz.kamenitxan.premag.model.Dao.DaoManager;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.Objects;

/**
 * Created by Kamenitxan (kamenitxan@me.com) on 08.09.15.
 */
public class Login {
	public static ModelAndView indexViewGet(Request request, Response response) {
		return new ModelAndView(null, "index.html");
	}

	public static ModelAndView indexViewPost(Request request, Response response) {
		String userName = request.queryParams("username");
		String password = request.queryParams("password");
		String email = request.queryParams("email");
		
		return new ModelAndView(null, "index.html");
	}

	public static ModelAndView userRegisterViewGet(Request request, Response response) {
		return new ModelAndView(null, "register.html");
	}

	public static ModelAndView userRegisterViewPost(Request request, Response response) {
		return new ModelAndView(null, "index.html");
	}

	public static boolean isAuthenticated(Request request, Response response) {
		if (!Objects.equals(request.pathInfo(), "/") || !Objects.equals(request.pathInfo(), "/registrace")) {
			request.session().attribute("auth");
			if (request.session().attribute("auth") == null) {
				return false;
			} else return request.session().attribute("auth").equals(true);
		} else {
			return true;
		}
	}
}
