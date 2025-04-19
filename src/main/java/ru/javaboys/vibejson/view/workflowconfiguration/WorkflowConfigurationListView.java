package ru.javaboys.vibejson.view.workflowconfiguration;

import com.vaadin.flow.router.Route;

import io.jmix.flowui.view.DialogMode;
import io.jmix.flowui.view.LookupComponent;
import io.jmix.flowui.view.StandardListView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import ru.javaboys.vibejson.entity.WorkflowConfiguration;
import ru.javaboys.vibejson.view.main.MainView;


@Route(value = "workflow-configurations", layout = MainView.class)
@ViewController(id = "WorkflowConfiguration.list")
@ViewDescriptor(path = "workflow-configuration-list-view.xml")
@LookupComponent("workflowConfigurationsDataGrid")
@DialogMode(width = "64em")
public class WorkflowConfigurationListView extends StandardListView<WorkflowConfiguration> {
}