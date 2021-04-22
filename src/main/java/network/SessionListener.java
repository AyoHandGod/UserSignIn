package network;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.concurrent.atomic.AtomicInteger;

@WebListener("/party/*")
public class SessionListener implements HttpSessionListener {

    private final AtomicInteger activeSessions;

    public SessionListener() {
        super();

        activeSessions = new AtomicInteger();
    }

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

    }
}
