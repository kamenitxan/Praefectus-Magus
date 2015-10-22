package templates;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.thymeleaf.context.AbstractContext;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateEngine;

import javax.servlet.ServletContext;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Kamenitxan (kamenitxan@me.com) on 20.10.15.
 */
public class MyTemplateEngine extends TemplateEngine {
	private final org.thymeleaf.TemplateEngine thymeleaf;

	public MyTemplateEngine() {
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setTemplateMode("HTML5");
		templateResolver.setPrefix("templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setCacheTTLMs(Long.valueOf(3600000L));
		this.thymeleaf = new org.thymeleaf.TemplateEngine();
		this.thymeleaf.setTemplateResolver(templateResolver);
		this.thymeleaf.addDialect(new LayoutDialect());
	}

	public MyTemplateEngine(org.thymeleaf.TemplateEngine thymeleaf) {
		if(thymeleaf == null) {
			throw new IllegalArgumentException("Thymeleaf must not be null");
		} else {
			this.thymeleaf = thymeleaf;
		}
	}

	public String render(ModelAndView modelAndView) {
		if(!(modelAndView.getModel() instanceof Map)) {
			throw new IllegalArgumentException("modelAndView must be of type java.util.Map");
		} else {
			Iterator modelMap = ((Map)modelAndView.getModel()).keySet().iterator();

			Object ctx;
			while(modelMap.hasNext()) {
				ctx = modelMap.next();
				if(!(ctx instanceof String)) {
					throw new IllegalArgumentException("All keys of the model must be Strings");
				}
			}

			Map modelMap1 = (Map)modelAndView.getModel();
			if(modelMap1.containsKey("request") && modelMap1.containsKey("response") && modelMap1.containsKey("servletContext") && modelMap1.get("request") instanceof Request && modelMap1.get("response") instanceof Response && modelMap1.get("servletContext") instanceof ServletContext) {
				ctx = new WebContext(((Request)modelMap1.get("request")).raw(), ((Response)modelMap1.get("response")).raw(), (ServletContext)modelMap1.get("servletContext"));
			} else {
				ctx = new Context();
			}

			((AbstractContext)ctx).setVariables(modelMap1);
			return this.thymeleaf.process(modelAndView.getViewName(), (IContext)ctx);
		}
	}
}
