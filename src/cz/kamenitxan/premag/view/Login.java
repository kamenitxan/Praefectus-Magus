package cz.kamenitxan.premag.view;

import cz.kamenitxan.premag.model.Dao.DaoManager;
import cz.kamenitxan.premag.model.User;
import org.mindrot.jbcrypt.BCrypt;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.*;
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

		/*if (user == null) {
			errors.add("Neexistující uživatel");
			data.put("errors", errors);
			return new ModelAndView(data, "index");
		}*/
		if (auth(userName, password)) {
			System.out.println("auth ok");
			request.session(true);
			User user = null;
			try {
				user = DaoManager.getUserDao().queryBuilder().where().eq("email", "a@a.cz").queryForFirst();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println(user);
			request.session().attribute("useremail", "a@a.cz");
			request.session().attribute("userid", 2);
			request.session().attribute("ir", false);
			request.session().attribute("ia", true);

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
		//System.out.println(request.pathInfo());
		if (path.equals("/") || path.equals("/registrace") || path.equals("/zapomenute") || path.contains("/static")) {
			return false;
		} else {
			return true;
		}
	}

	private static Hashtable<String, String> env = new Hashtable<>();
	public static boolean auth(String user, String password) {
		try {
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.PROVIDER_URL, "ldap://127.0.0.1:389");
			env.put(Context.SECURITY_PRINCIPAL, "uid=" + user + ",ou=peopee,dc=vse,dc=cz");
			env.put(Context.SECURITY_CREDENTIALS, password);
			//env.put(Context.SECURITY_PRINCIPAL, "cn=admin,dc=vse,dc=cz");
			//env.put(Context.SECURITY_CREDENTIALS, "3fmOxDN0I6*Fr5bg2");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {

			DirContext ctx = new InitialDirContext(env);
			String base = "dc=vse,dc=cz";

			SearchControls sc = new SearchControls();
			sc.setSearchScope(SearchControls.SUBTREE_SCOPE);

			String filter = "(objectclass=top)";
			System.out.println("hledam");
			NamingEnumeration results = ctx.search(base, filter, sc);

			System.out.println("hledani ukonceno");
			while (results.hasMore()) {

				SearchResult sr = (SearchResult) results.next();
				//System.out.println("vysledek " + sr.toString());
				Attributes attrs = sr.getAttributes();
/*
				Attribute attr = attrs.get("cn");
				if (attr != null) {
					System.out.println("nalezeno cn" + attr.toString());
					//logger.debug("record found "+attr.get());
				}
				attr = attrs.get("userPassword");
				if (attr != null) {
					//Byte[] byteValue = (Byte[]) attr.get();
					//String plainTextPassword = new String(byteValue);
					System.out.println("nalezeno" + attr.toString());
					System.out.println("nalezeno2" + attr.get().toString());
					//logger.debug("record found "+attr.get());
				}
				*/
			}
			ctx.close();
			return true;

		} catch (AuthenticationException e) {
			System.out.println("Spatne heslo");
			return false;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
