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
        setId("particles-js");
    } //particlesConfig is now directly configured In particles.js

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        UI.getCurrent().access(() -> UI.getCurrent().getPage().executeJs("setTimeout(function() { particlesJS('particles-js', $0); }, 1)", particlesConfigJson));
    }
}