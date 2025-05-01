package org.abgehoben.QuartettVaadin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class LoginService implements Serializable {

    public static Map<VaadinSession, String> usersInQueue = new ConcurrentHashMap<>(); //keep everything threadsafe
    public static Map<VaadinSession, String> usersInGame = new ConcurrentHashMap<>();
    public static Map<VaadinSession, UI> sessionUIMap = new ConcurrentHashMap<>();

    public static String greet(String name, VaadinSession sessionId) {
        if (usersInGame.containsKey(sessionId)) {
            QuartettService.joinGame(sessionId);
            return "already in game";
        }
        if (name == null || name.isEmpty()) {
            return "name cannot be empty";
        }
        if (usersInQueue.containsKey(sessionId)) {
            return "already in Queue";
        }
        if (usersInGame.containsValue(name) || usersInQueue.containsValue(name)) {
            return "name already taken";
        }


        usersInQueue.put(sessionId, name);
        if (usersInQueue.size() >= 2) {

            // Add every queued session to usersInGame
            for (VaadinSession session : usersInQueue.keySet()) {
                usersInGame.put(session, usersInQueue.get(session));
            }

            QuartettService.startNewGame(usersInQueue);
            usersInQueue.clear();
            return "Game Started";
        } else {
            return "Joined Queue";
        }
    }

    public static UI getUIForSession(VaadinSession session) {
        return sessionUIMap.get(session);
    }
}
