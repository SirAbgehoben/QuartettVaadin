// ParticleBackground.java
package org.abgehoben.QuartettVaadin;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


@Tag("div")
@JsModule("./particles.js")
public class ParticleBackground extends VerticalLayout {

    String particlesConfigJson;

    public ParticleBackground() {
        setId("particles-js"); // Set the ID directly on the VerticalLayout

        try {
            // Read the JSON file as a String.  This assumes particles-config.json is in frontend/.
            particlesConfigJson = new String(Files.readAllBytes(Paths.get("./src/main/frontend/particles-config.json")));
        } catch (IOException e) {
            e.printStackTrace(); // Or handle the exception more gracefully
            particlesConfigJson = "{}"; // Provide an empty config as a fallback
        } //I dunno, this doesn't work, just set the variables in the particles.js themselves.


    }
    @Override
    protected void onAttach(AttachEvent attachEvent) {
        attachEvent.getUI();
        UI.getCurrent().getPage().executeJs("particlesJS('particles-js', $0)", particlesConfigJson); //needs an UI, but UI is null after forwarding from mainView.
    }
}