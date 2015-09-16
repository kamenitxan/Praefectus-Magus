package cz.kamenitxan.premag;

import cz.kamenitxan.premag.view.Login;
import spark.template.handlebars.HandlebarsTemplateEngine;

import static spark.Spark.*;

public class Main {
	public static void main(String[] args) {
        //staticFileLocation("/META-INF/static");

		before((request, response) -> {
			if (Login.isAuthenticated(request, response)) {
				halt(401, "Neplatné přihlášení");
			}
		});

		get("/", Login::indexViewGet, new HandlebarsTemplateEngine());
		post("/", Login::indexViewPost, new HandlebarsTemplateEngine());
		get("/registrace", Login::userRegisterViewGet, new HandlebarsTemplateEngine());
		post("/registrace", Login::userRegisterViewPost, new HandlebarsTemplateEngine());

		// https://gist.github.com/Wilfred/715ae4e22642cfff1dbd templaty

    }
}
