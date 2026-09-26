package ru.javaboys.vibejson.view.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;

/**
 * Диаграмма workflow: стартеры, шаги и переходы между ними.
 * <p>
 * Раскладка и отрисовка выполняются в браузере веб-компонентом
 * {@code vj-workflow-diagram}, серверу достаточно передать JSON схемы.
 */
@Tag("vj-workflow-diagram")
@JsModule("./components/vj-workflow-diagram.js")
public class WorkflowDiagram extends Component implements HasSize, HasStyle {

    public void setWorkflow(String workflowJson) {
        getElement().setProperty("workflow", workflowJson == null ? "" : workflowJson);
    }
}
