package org.abgehoben.QuartettVaadin;

import com.vaadin.flow.component.UI;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route
public class MainView extends VerticalLayout {
    public MainView(LoginService service) {

        this.getStyle().set("background-image", "url('https://images6.alphacoders.com/113/1137853.png')");
        this.getStyle().set("background-position", "center");
        this.getStyle().set("background-size", "cover");
        this.getStyle().set("background-repeat", "no-repeat").setHeight("100%");

        Div container = new Div();
        container.getStyle().set("background", "hsla(214, 10%, 6%, 0.8)");
        container.getStyle().set("padding", "20px");
        container.getStyle().set("border-radius", "10px");
        container.getStyle().set("box-shadow", "0 4px 6px rgba(0, 0, 0, 0.1)");
        container.getStyle().set("backdrop-filter", "blur(20px)");
        container.getStyle().set("color", "hsla(214, 96%, 96%, 0.9)"); //global text color for container

        VaadinSession sessionId = VaadinSession.getCurrent();
        UI CurrentUI = UI.getCurrent();
        LoginService.sessionUIMap.put(sessionId, CurrentUI);

        Span sessionInfo = new Span("Session ID: " + sessionId.getSession().getId() + ", UI ID: " + CurrentUI);
        sessionInfo.getStyle().set("position", "absolute");
        sessionInfo.getStyle().set("bottom", "10px");
        sessionInfo.getStyle().set("left", "10px");
        sessionInfo.getStyle().set("color", "hsla(214, 96%, 16%, 0.9)");
        sessionInfo.getStyle().set("font-size", "12px");
        this.add(sessionInfo);



        TextField textField = new TextField("Your Nickname");
        textField.getElement().getStyle().set("--vaadin-input-field-label-color", "hsla(214, 69%, 84%, 0.32)"); //kinda sounds like limbo IDK
        textField.getElement().getStyle().set("--vaadin-input-field-focused-label-color", "hsla(205, 90%, 48%, 0.80)");
        textField.getElement().getStyle().set("--vaadin-input-field-hovered-label-color", "hsla(214, 87%, 92%, 0.69)");
        textField.getElement().getStyle().set("--vaadin-input-field-disabled-background", "hsla(214, 60%, 55%, 0.05)");
        textField.getElement().getStyle().set("--vaadin-input-field-disabled-value-color", "hsla(214, 69%, 84%, 0.32)");
        textField.getElement().getStyle().set("--vaadin-input-field-background", "hsla(214, 60%, 55%, 0.10)");
        textField.getStyle().set("--lumo-disabled-text-color", "hsla(214, 0%, 100%, 0.32)"); //vaadin-input-field-disabled-label-color
        textField.getStyle().setPaddingRight("20px");
        textField.getStyle().set("color", "hsla(214, 96%, 96%, 0.9)");
        textField.setMaxLength(12);

        Button button = new Button("Join Queue");


        button.addClickListener((e) -> {
            String greeting = service.greet(textField.getValue(), sessionId);

            if (greeting.equals("name cannot be empty") || greeting.equals("name already taken")) {
                Notification notification = new Notification(greeting, 4000);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.open();
            }

            if (greeting.equals("Joined Queue") || greeting.equals("already in Queue") || greeting.equals("already in Game")) {

                Div spinner = new Div();
                spinner.addClassName("lds-ripple");
                spinner.getStyle().setHeight("34px");
                spinner.getStyle().setWidth("112px");
                spinner.add(new Div(), new Div());

                textField.setEnabled(false);
                container.replace(button, spinner);

                Notification notification = new Notification(greeting, 4000);
                if (greeting.equals("Joined Queue")) {
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                }

                if (greeting.equals("already in Queue") || greeting.equals("already in Game")) {
                    notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
                }

                notification.open();
            }

        });
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.addClickShortcut(Key.ENTER);
        button.getStyle().set("height", "34px");
        button.getStyle().set("background", "rgba(14, 16, 17, 0.2)");
        button.getStyle().set("backdrop-filter", "blur(10px)");
        button.getStyle().set("border", "1px solid rgba(40, 44, 46, 0.3)");
        button.getStyle().set("box-shadow", "0 4px 6px rgba(0, 0, 0, 0.1)");
        button.getStyle().set("transition", "all 0.3s ease");
        button.getStyle().set("cursor", "pointer");
        button.addClickListener((e) -> {
            button.getStyle().set("transform", "scale(1.1)");
            button.getStyle().set("box-shadow", "0 6px 8px rgba(0, 0, 0, 0.2)");
            this.getUI().ifPresent((ui) -> ui.access(() -> {
                button.getStyle().set("transform", "scale(1)");
                button.getStyle().set("box-shadow", "0 4px 6px rgba(0, 0, 0, 0.1)");
            }));
        });
        container.add(textField, button);
        this.add(container);
        this.addClassNames("p-m");
    }
}
