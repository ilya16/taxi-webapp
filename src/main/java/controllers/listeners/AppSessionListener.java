package controllers.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class AppSessionListener implements HttpSessionListener {
    private static final Logger LOGGER = LogManager.getLogger(AppSessionListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
//        LOGGER.debug(HttpSessionEvent.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {

    }
}
