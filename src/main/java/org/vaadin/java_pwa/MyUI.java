package org.vaadin.java_pwa;

import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import com.vaadin.ui.renderers.ImageRenderer;
import org.apache.commons.io.IOUtils;
import org.vaadin.java_pwa.backend.Workout;
import org.vaadin.java_pwa.backend.WorkoutDataReader;
import org.vaadin.leif.headertags.HeaderTagHandler;
import org.vaadin.leif.headertags.Link;
import org.vaadin.leif.headertags.Meta;
import org.vaadin.leif.headertags.MetaTags;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
@JavaScript("vaadin://js/app.js")
@MetaTags({
	@Meta(name="viewport", content="width=device-width, initial-scale=1"),
	@Meta(name="theme-color", content="#404549")
})
@Link(rel="manifest", href="VAADIN/manifest.json")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {

    	BeanItemContainer<Workout> container = new BeanItemContainer<>(
    			Workout.class, new WorkoutDataReader().run());

    	Grid grid = new Grid();
    	grid.setContainerDataSource(container);
    	grid.getColumn("sport").setRenderer(new ImageRenderer());
    	grid.setSizeFull();

        setContent(grid);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = true)
    public static class MyUIServlet extends VaadinServlet {
    	
		@Override
		protected void servletInitialized() throws ServletException {
			super.servletInitialized();

            HeaderTagHandler.init(getService());
		}

		@Override
		protected void service(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			String pathInfo = request.getPathInfo();

			if (pathInfo.endsWith("sw.js")) {
				try (InputStream in = getClass().getResourceAsStream("/sw.js")) {
					if (in == null) {
						response.sendError(404);
						return;
					}
					response.setContentType("application/javascript");
					OutputStream out = response.getOutputStream();
					IOUtils.copy(in, out);
					in.close();
					out.close();
				}
			} else {
				super.service(request, response);
			}
		}
    }
}
