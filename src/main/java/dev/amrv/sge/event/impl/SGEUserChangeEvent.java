package dev.amrv.sge.event.impl;

import dev.amrv.sge.auth.UserCredentials;
import dev.amrv.sge.event.Event;

/**
 *
 * @author Adrian Martin Ruiz del Valle Aka. Ansuz
 */
public class SGEUserChangeEvent extends Event {

    private final UserCredentials newUser;
    private final UserCredentials oldUser;

    public SGEUserChangeEvent(UserCredentials oldUser, UserCredentials newUser) {
        this.oldUser = oldUser;
        this.newUser = newUser;
    }

    public UserCredentials getNewUser() {
        return newUser;
    }

    public UserCredentials getOldUser() {
        return oldUser;
    }
}
