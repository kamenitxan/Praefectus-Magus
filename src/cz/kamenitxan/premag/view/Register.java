package cz.kamenitxan.premag.view;

import cz.kamenitxan.premag.model.Dao.DaoManager;
import cz.kamenitxan.premag.model.School;
import cz.kamenitxan.premag.model.User;
import org.mindrot.jbcrypt.BCrypt;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.ArrayList;
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
			data.put("mgs", new ArrayList<String>(){{add("Účet by úšpěšně vytvořen. ");}});
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return new ModelAndView(data, "index");
	}

	public static ModelAndView changePassPost(Request request, Response response) {
		// $2a$10$0rCUUET/RNsRt9ST7z93yeMKwjlCHXSXbvO8CbaQ60ZHq3J47VDxm
		Map<String, Object> data = Profile.profileGetData(request);

		User user = null;
		try {
			user = DaoManager.getUserDao().queryBuilder().where().eq("email", request.session().attribute("useremail")).queryForFirst();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String oldPass = request.queryParams("passOld");
		String new1 = request.queryParams("pass");
		String new2 = request.queryParams("pass2");
		if (new1.equals(new2) && new1.length() > 0) {
			if (BCrypt.checkpw(oldPass, user.getPassword())) {
				String hashed = BCrypt.hashpw(new1 , BCrypt.gensalt());
				user.setPassword(hashed);
				try {
					DaoManager.getUserDao().update(user);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				data.put("mgs", new ArrayList<String>(){{add("Heslo změněno");}});
				return new ModelAndView(data, "profile");
			} else {
				data.put("errors", new ArrayList<String>(){{add("Špatné aktuální heslo");}});
				return new ModelAndView(data, "profile");
			}

		} else {
			data.put("errors", new ArrayList<String>(){{add("Hesla se neschodují ");}});
		}
		return new ModelAndView(data, "profile");
	}

	/*public static ModelAndView verifyGet(Request request, Response response) {
		Map<String, Object> data = new HashMap<>();
		String token = request.params(":token");
		User user = null;
		try {
			user = DaoManager.getUserDao().queryBuilder().where().eq("verification", token).queryForFirst();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (user != null) {
			data.put("mgs", new ArrayList<String>(){{add("Účet by úšpěšně ověřen.");}});
			user.setEnabled(true);
			try {
				DaoManager.getUserDao().update(user);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			data.put("errors", new ArrayList<String>(){{add("Neplatný ověřovací kód.");}});
		}

		return new ModelAndView(data, "index");
	}*/
}
