package ru.javaboys.vibejson;

import com.google.common.base.Strings;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Push
@Theme(value = "vibe-json")
@PWA(name = "Vibe JSON", shortName = "Vibe JSON")
@JsModule("./src/theme/color-scheme-switching-support.js")
@SpringBootApplication
public class VibeJsonApplication implements AppShellConfigurator {

    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(VibeJsonApplication.class, args);
    }

    @Override
    public void configurePage(AppShellSettings settings) {
        settings.addFavIcon("icon", "icons/logo.svg", "any");
        settings.addMetaTag("theme-color", "#6366f1");
        // Шрифты интерфейса. Если Google Fonts недоступен, тема откатывается на
        // системные шрифты - они перечислены в --lumo-font-family.
        settings.addLink("preconnect", "https://fonts.gstatic.com");
        settings.addLink("stylesheet", "https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800"
                + "&family=JetBrains+Mono:wght@400;500&display=swap");
    }

    @Bean
    @Primary
    @ConfigurationProperties("main.datasource")
    DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("main.datasource.hikari")
    DataSource dataSource(final DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    @EventListener
    public void printApplicationUrl(final ApplicationStartedEvent event) {
        LoggerFactory.getLogger(VibeJsonApplication.class).info("Application started at "
                + "http://localhost:"
                + environment.getProperty("local.server.port")
                + Strings.nullToEmpty(environment.getProperty("server.servlet.context-path")));
    }
}
