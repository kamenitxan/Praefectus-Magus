package cz.kamenitxan.premag;

import cz.kamenitxan.premag.view.*;
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
		get("/tymy", Teams::teamListGet, new MyTemplateEngine());
		post("/tymy", Teams::teamListPost, new MyTemplateEngine());
		get("/tymy/tisk", Teams::teamPrintGet, new MyTemplateEngine());

		get("/vysledky/:year", Teams::resultListGet, new MyTemplateEngine());
		get("/vysledky", Teams::resultListGet, new MyTemplateEngine());

		get("/admin", Admin::dashboardGet, new MyTemplateEngine());
		get("/admin/uzivatele", Admin::usersGet, new MyTemplateEngine());
		get("/admin/uzivatele/smazat", Admin::userDeleteGet, new MyTemplateEngine());
		get("/admin/uzivatele/tymy", Admin::userTeamsGet, new MyTemplateEngine());
		get("/admin/tymy", Admin::teamsGet, new MyTemplateEngine());

		//get("/verify/:token", Register::verifyGet, new MyTemplateEngine());

		// https://gist.github.com/Wilfred/715ae4e22642cfff1dbd templaty

    }
}
