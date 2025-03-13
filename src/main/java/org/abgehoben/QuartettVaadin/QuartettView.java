package org.abgehoben.QuartettVaadin;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("quartett")
public class QuartettView extends VerticalLayout implements BeforeEnterObserver {

    public QuartettView() {
        VaadinSession session = VaadinSession.getCurrent();
        QuartettSession quartettSession = QuartettService.getQuartettSessionForPlayer(session);

        Span helloSpan = new Span("Hello World!");
        add(helloSpan);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        VaadinSession session = VaadinSession.getCurrent();
        if (!LoginService.usersInGame.containsKey(session)) {
            event.forwardTo("");
        }
    }
}