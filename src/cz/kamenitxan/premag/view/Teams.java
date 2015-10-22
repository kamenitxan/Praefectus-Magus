package cz.kamenitxan.premag.view;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kamenitxan (kamenitxan@me.com) on 22.10.15.
 */
public class Teams {
	public static ModelAndView teamListGet(Request request, Response response) {
		Map<String, Object> data = new HashMap<>();
		String yearS = request.params(":year");
		return new ModelAndView(data, "teams");
	}

}
