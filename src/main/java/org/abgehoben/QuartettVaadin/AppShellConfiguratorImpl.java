package org.abgehoben.QuartettVaadin;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.context.annotation.Configuration;

@PWA(
        name = "QuartettVaadin",
        shortName = "QtVaadin"
)
@Push
@Theme("Loading-Circle")
@CssImport("./themes/my-theme/styles.css")
@Configuration
public class AppShellConfiguratorImpl implements AppShellConfigurator {
}
