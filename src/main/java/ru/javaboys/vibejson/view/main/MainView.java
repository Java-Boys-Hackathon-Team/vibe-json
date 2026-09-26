package ru.javaboys.vibejson.view.main;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.flowui.app.main.StandardMainView;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.kit.theme.ThemeUtils;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import ru.javaboys.vibejson.entity.User;

@Route("")
@ViewController(id = "MainView")
@ViewDescriptor(path = "main-view.xml")
public class MainView extends StandardMainView {

    @Autowired
    private CurrentAuthentication currentAuthentication;

    @ViewComponent
    private JmixButton logoutButton;
    @ViewComponent
    private Div userBox;

    @Subscribe
    public void onInit(final InitEvent event) {
        // В узкой панели меню помещаются только иконки: текст действия выхода
        // убирается, подсказка остаётся в title.
        logoutButton.setText(null);

        UserDetails user = currentAuthentication.getUser();
        Avatar avatar = new Avatar(displayName(user));
        avatar.setTooltipEnabled(true);
        userBox.add(avatar);
    }

    @Subscribe(id = "themeToggle", subject = "clickListener")
    public void onThemeToggleClick(final ClickEvent<JmixButton> event) {
        UI.getCurrent().getPage()
                .executeJs("return document.documentElement.getAttribute('theme') || ''")
                .then(String.class, theme -> {
                    if (theme.contains("dark")) {
                        ThemeUtils.applyLightTheme();
                    } else {
                        ThemeUtils.applyDarkTheme();
                    }
                });
    }

    private static String displayName(UserDetails user) {
        if (user instanceof User u) {
            String fullName = StringUtils.trimToEmpty(
                    StringUtils.defaultString(u.getFirstName()) + " " + StringUtils.defaultString(u.getLastName()));
            if (!fullName.isEmpty()) {
                return fullName;
            }
        }
        return user.getUsername();
    }
}
