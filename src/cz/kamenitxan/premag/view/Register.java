package cz.kamenitxan.premag.view;

import cz.kamenitxan.premag.Logins;
import cz.kamenitxan.premag.model.Dao.DaoManager;
import cz.kamenitxan.premag.model.Email;
import cz.kamenitxan.premag.model.School;
import cz.kamenitxan.premag.model.User;
import org.mindrot.jbcrypt.BCrypt;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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

	public static ModelAndView forgotGet(Request request, Response response) {
		Map<String, Object> data = new HashMap<>();
		return new ModelAndView(data, "registrationForgot");
	}

	public static ModelAndView forgotPost(Request request, Response response) {
		Map<String, Object> data = new HashMap<>();
		data.put("mgs", new ArrayList<String>(){{add("Nové heslo vytvořeno a zasláno na váš email. ");}});
		String userEmail = request.queryParams("email");
		User user = null;
		try {
			user = DaoManager.getUserDao().queryBuilder().where().eq("email", userEmail).queryForFirst();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (user == null) {
			return new ModelAndView(data, "index");
		}
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 18) {
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		System.out.println("Uživatel " + user.getEmail() + "zažádal o změnu hesla.");
		String hashed = BCrypt.hashpw(saltStr, BCrypt.gensalt());
		user.setPassword(hashed);

		try {
			DaoManager.getUserDao().update(user);
			sendMail(new Email(user.getEmail(), "Nové heslo", "Vaše heslo na pokuston.kamus.cz bylo zresetováno. Vaše nové heslo je \"" + saltStr + "\". Doporučujeme ho ihned po příhlášní změnit."));
			//sendMail(new Email("tomaspavel@me.com", "Nové heslo", "Vaše heslo na pokuston.kamus.cz bylo zresetováno. Vaše nové heslo je \"" + saltStr + "\". Doporučujeme ho ihned po příhlášní změnit."));

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

	public static void sendMail(Email email){
		InternetAddress[] recipient = null;
		try {

				recipient = InternetAddress.parse(email.recipient);
			} catch (AddressException ex) {
			ex.printStackTrace();
		}

		Properties props = new Properties();
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", "mail.kamus.cz");
		props.put("mail.smtp.port", "465");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(Logins.getMailUser(), Logins.getMainPass());
					}
				});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("pokuston@kamus.cz"));
			message.setRecipients(Message.RecipientType.TO, recipient);
			message.setSubject(email.subject);
			message.setText(email.text);

			Transport.send(message);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
