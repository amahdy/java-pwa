package org.vaadin.java_pwa;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.apache.commons.io.IOUtils;
import org.vaadin.leif.headertags.HeaderTagHandler;
import org.vaadin.leif.headertags.Link;
import org.vaadin.leif.headertags.Meta;
import org.vaadin.leif.headertags.MetaTags;

import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.RequestHandler;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

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
	@Meta(name="theme-color", content="#00b4f0")
})
@Link(rel="manifest", href="VAADIN/manifest.json")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        
        final TextField name = new TextField();
        name.setCaption("Type your name here:");

        Button button = new Button("Click Me");
        button.addClickListener( e -> {
            layout.addComponent(new Label("Thanks " + name.getValue() 
                    + ", it works!"));
        });
        
        layout.addComponents(name, button);
        layout.setMargin(true);
        layout.setSpacing(true);
        
        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = true)
    public static class MyUIServlet extends VaadinServlet {
    	
		@Override
		protected void servletInitialized() throws ServletException {
			super.servletInitialized();

            HeaderTagHandler.init(getService());
			
			getService().addSessionInitListener(new SessionInitListener() {

				@Override
				public void sessionInit(SessionInitEvent event) throws ServiceException {
					event.getSession().addRequestHandler(new RequestHandler() {

						@Override
						public boolean handleRequest(VaadinSession session, VaadinRequest request,
								VaadinResponse response) throws IOException {

							String pathInfo = request.getPathInfo();
							InputStream in = null;

							if (pathInfo.endsWith("sw.js")) {
								response.setContentType("application/javascript");
								in = getClass().getResourceAsStream("/sw.js");
							}

							if (in != null) {
								OutputStream out = response.getOutputStream();
								IOUtils.copy(in, out);
								in.close();
								out.close();

								return true;
							} else {

								return false;
							}
						}
					});
				}
			});
		}
    }
}
