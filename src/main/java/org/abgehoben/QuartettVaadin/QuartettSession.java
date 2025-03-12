package org.abgehoben.QuartettVaadin;

import java.util.ArrayList;

public class QuartettSession {

    private static ArrayList<QuartettSession> AktiveSessions = new ArrayList<QuartettSession>();
    private static int QuartettSessionIdCounter = 0; //using a static means that it is shared between all instances of the class
    private int id;



    public QuartettSession(int id) {
        this.id = id;
        AktiveSessions.add(id,this);
        QuartettSessionIdCounter++;
    }


}



/*        // Create a new QuartettSession, assigning a new session ID
        QuartettSession newSession = new QuartettSession();
        activeSessions.add(newSession); // Add to the list of active sessions*/

//okay, so an object is like a copy of a class, i can access everything in it and set variables independently
