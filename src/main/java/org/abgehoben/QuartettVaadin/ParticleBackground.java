// ParticleBackground.java
package org.abgehoben.QuartettVaadin;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;


@Tag("div")
@JsModule("./particles.js")
public class ParticleBackground extends VerticalLayout {

    String particlesConfigJson;

    public ParticleBackground() {
        setId("particles-js"); // Set the ID directly on the VerticalLayout
    } //particlesConfig is now directly configured In particles.js

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        attachEvent.getUI();
        UI.getCurrent().getPage().executeJs("particlesJS('particles-js', $0)", particlesConfigJson); //needs an UI, but UI is null after forwarding from mainView.
    }
}