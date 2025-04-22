package ru.javaboys.vibejson.view.workflowconfiguration;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import ru.javaboys.vibejson.entity.WorkflowConfiguration;
import ru.javaboys.vibejson.view.main.MainView;

@Route(value = "workflow-configurations/:id", layout = MainView.class)
@ViewController(id = "WorkflowConfiguration.detail")
@ViewDescriptor(path = "workflow-configuration-detail-view.xml")
@EditedEntityContainer("workflowConfigurationDc")
public class WorkflowConfigurationDetailView extends StandardDetailView<WorkflowConfiguration> {
}