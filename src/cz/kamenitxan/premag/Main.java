package cz.kamenitxan.premag;

import cz.kamenitxan.premag.view.Login;
import cz.kamenitxan.premag.view.Profile;
import cz.kamenitxan.premag.view.Register;
import cz.kamenitxan.premag.view.Teams;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import templates.MyTemplateEngine;

import static spark.Spark.*;

public class Main {
	public static void main(String[] args) {
		port(4568);
        staticFileLocation("/templates/resources");

		before((request, response) -> {
			if (Login.isProtected(request)) {
				if (!Login.isAuthenticated(request, response)) {
					response.redirect("/", 302);
					halt();
				}
			}
		});

		get("/", Login::indexViewGet, new MyTemplateEngine());
		post("/", Login::indexViewPost, new MyTemplateEngine());
		get("/registrace", Register::userRegisterViewGet, new MyTemplateEngine());
		post("/registrace", Register::userRegisterViewPost, new MyTemplateEngine());
		get("/logout", Login::logOutViewGet, new MyTemplateEngine());
		get("/profil", Profile::profileViewGet, new MyTemplateEngine());
		get("/tymy/pridat", Teams::teamAddGet, new MyTemplateEngine());
		post("/tymy/pridat", Teams::teamAddPost, new MyTemplateEngine());
		get("/tymy/:year", Teams::teamListGet, new MyTemplateEngine());

		// https://gist.github.com/Wilfred/715ae4e22642cfff1dbd templaty

    }
}
