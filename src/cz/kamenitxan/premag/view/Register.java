package cz.kamenitxan.premag.view;

import cz.kamenitxan.premag.model.Dao.DaoManager;
import cz.kamenitxan.premag.model.School;
import cz.kamenitxan.premag.model.User;
import org.mindrot.jbcrypt.BCrypt;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kamenitxan (kamenitxan@me.com) on 08.09.15.
 */
public class Register {
	public static ModelAndView userRegisterViewGet(Request request, Response response) {
		return new ModelAndView(new HashMap(), "registration");
	}

	public static ModelAndView userRegisterViewPost(Request request, Response response) {
		Map<String, Object> data = new HashMap<>();



		String userEmail = request.queryParams("email");

		try {
			long rows = DaoManager.getUserDao().queryBuilder().where().eq("email", userEmail).countOf();
			if (rows > 0) {
				return new ModelAndView(data, "registration");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String hashed = BCrypt.hashpw(request.queryParams("pass"), BCrypt.gensalt());
		User user = new User(userEmail, hashed);

		try {
			DaoManager.getUserDao().create(user);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		School school = new School() {{
			setName(request.queryParams("schoolname"));
			setAddress(request.queryParams("schooladdress"));
			setEmail(userEmail);
			setContactName(request.queryParams("contactname"));
			setContactAddress(request.queryParams("contactaddress"));
			setPhone(request.queryParams("tel"));
			setFax(request.queryParams("fax"));
			setUser(user);
		}};

		try {
			DaoManager.getSchoolDao().create(school);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return new ModelAndView(data, "index");
	}
}
