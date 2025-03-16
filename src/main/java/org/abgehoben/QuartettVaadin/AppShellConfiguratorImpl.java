package org.abgehoben.QuartettVaadin;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;

@PWA(
        name = "QuartettVaadin",
        shortName = "qtvaadin"
)
@Push //I think this actually fixed the issue(ui not forwarding) SO KEEEEEEEP IT.
@Theme("Loading-Circle")
public class AppShellConfiguratorImpl implements AppShellConfigurator {
    public AppShellConfiguratorImpl() {
    }
}
