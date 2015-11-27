package cz.kamenitxan.premag.view;

import cz.kamenitxan.premag.model.Dao.DaoManager;
import cz.kamenitxan.premag.model.School;
import cz.kamenitxan.premag.model.SettingHelper;
import cz.kamenitxan.premag.model.Team;
import cz.kamenitxan.premag.model.User;
import cz.kamenitxan.premag.model.Settings;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import spark.Spark;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Kamenitxan (kamenitxan@me.com) on 25.10.15.
 */
public class Admin {
	public static ModelAndView dashboardGet(Request request, Response response) {
		Map<String, Object> data = new HashMap<>();
		data.put("menu", User.getMenuItems(request));
		data.put("settings", SettingHelper.getInstance());

		return new ModelAndView(data, "admin/dashboard");
	}

	public static ModelAndView dashboardPost(Request request, Response response) {
		Map<String, Object> data = new HashMap<>();
		data.put("menu", User.getMenuItems(request));

		boolean teams = "checked".equals(request.queryParams("teamEdit"));
		boolean ref = "checked".equals(request.queryParams("refereeEnabled"));

		Settings s = SettingHelper.getInstance();
		s.setTeamsEdit(teams);
		s.setRefereeEnabled(ref);

		try {
			DaoManager.getSettingsDao().update(s);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		data.put("settings", s);

		return new ModelAndView(data, "admin/dashboard");
	}

	public static ModelAndView usersGet(Request request, Response response) {
		Map<String, Object> data = new HashMap<>();
		data.put("menu", User.getMenuItems(request));

		List<School> schools = new ArrayList<>();
		try {
			schools =  DaoManager.getSchoolDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		data.put("schools", schools);

		return new ModelAndView(data, "admin/users");
	}

	public static ModelAndView userDeleteGet(Request request, Response response) {
		int idS = Integer.valueOf(request.queryParams("idS"));
		int idU = Integer.valueOf(request.queryParams("idU"));

		if (User.getMenuItems(request).admin) {
			try {
				DaoManager.getSchoolDao().deleteById(idS);
				DaoManager.getUserDao().deleteById(idU);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return usersGet(request, response);
	}

	public static ModelAndView userTeamsGet(Request request, Response response) {
		Map<String, Object> data = new HashMap<>();
		String schoolId = request.queryParams("schoolId");
		String yearS = request.queryParams("rok");
		if (yearS == null) {
			yearS = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		}
		List<Team> teams = null;
		List<Team> years = null;
		try {
			final School school =  DaoManager.getSchoolDao().queryForId(Integer.valueOf(schoolId));
			teams = DaoManager.getTeamDao().queryBuilder().where()
					.eq("year", Integer.valueOf(yearS)).and().eq("school_id", school.getId()).query();
			years = DaoManager.getTeamDao().queryBuilder().distinct().selectColumns("year").query();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		data.put("years", years);
		data.put("schoolId", schoolId);
		data.put("selected", yearS);
		data.put("teams", teams);
		data.put("menu", User.getMenuItems(request));
		return new ModelAndView(data, "admin/userTeams");
	}

	public static ModelAndView teamsGet(Request request, Response response) {
		Map<String, Object> data = new HashMap<>();
		String yearS = request.queryParams("rok");
		if (yearS == null) {
			yearS = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		}
		List<Team> teams = null;
		List<Team> years = null;
		try {
			teams = DaoManager.getTeamDao().queryBuilder().where()
					.eq("year", Integer.valueOf(yearS)).query();
			years = DaoManager.getTeamDao().queryBuilder().distinct().selectColumns("year").query();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		data.put("years", years);
		data.put("selected", yearS);
		data.put("teams", teams);
		data.put("menu", User.getMenuItems(request));
		return new ModelAndView(data, "admin/participants");
	}

	public static Object teamsDownloadGet(Request request, Response response) {
		HttpServletResponse raw = response.raw();
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			response.raw().setContentType("application/octet-stream");
			response.raw().setHeader("Content-Disposition", "attachment; filename=tymy.xlsx");

			Workbook wb = new XSSFWorkbook();
			Sheet s = wb.createSheet();
			int yearS =Calendar.getInstance().get(Calendar.YEAR);
			List<Team> teams = DaoManager.getTeamDao().queryBuilder().where().eq("year", yearS).query();
			Row r = s.createRow(0);
			r.createCell(0).setCellValue("Jméno 1");
			r.createCell(1).setCellValue("Příjmení 1");
			r.createCell(2).setCellValue("Jméno 2");
			r.createCell(3).setCellValue("Příjmení 2");
			r.createCell(4).setCellValue("Pokus");
			r.createCell(5).setCellValue("Škola");
			r.createCell(6).setCellValue("Oběd");
			for (int rownum = 0; rownum < teams.size(); rownum++) {
				Team team = teams.get(rownum);
				r = s.createRow(rownum+1);
				r.createCell(0).setCellValue(team.getParticipant1().getFirstName());
				r.createCell(1).setCellValue(team.getParticipant1().getLastName());
				r.createCell(2).setCellValue(team.getParticipant2().getFirstName());
				r.createCell(3).setCellValue(team.getParticipant2().getLastName());
				r.createCell(4).setCellValue(team.getExperiment());
				r.createCell(5).setCellValue(team.getSchool().getName());
				String lunch = team.isLunch() ? "Ano" : "Ne";
				r.createCell(6).setCellValue(lunch);
			}

			wb.write(out);
			out.close();

			byte[] bytes = out.toByteArray();



			raw.getOutputStream().write(bytes);
			raw.getOutputStream().flush();
			raw.getOutputStream().close();



		} catch (Exception ex) {
			ex.printStackTrace();
			Spark.halt(501, "Chyba serveru");
		}
		return response.raw();
	}

}
