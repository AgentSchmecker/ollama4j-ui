package agency.schmecker.dev.ollama4j.ui.view.generate;

import com.vaadin.cdi.annotation.CdiComponent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import agency.schmecker.dev.ollama4j.ui.service.GenerateService;
import agency.schmecker.dev.ollama4j.ui.template.MainLayout;
import jakarta.inject.Inject;

@Route(value = "generate/simple", layout = MainLayout.class)
@PageTitle("Ask single question")
@CdiComponent
public class SimpleGenerateView extends VerticalLayout {

    @Inject
    private GenerateService generateService;
    private TextField questionTextField = new TextField();
    private TextArea sentQuestionTextArea = new TextArea();
    private TextArea answerTextArea = new TextArea();

    public SimpleGenerateView() {
        questionTextField.setWidthFull();
        questionTextField.setLabel("Question");
        questionTextField.setPlaceholder("Please enter a question");

        sentQuestionTextArea.setWidthFull();
        sentQuestionTextArea.setVisible(false);
        sentQuestionTextArea.setLabel("Sent question");
        sentQuestionTextArea.setReadOnly(true);

        answerTextArea.setWidthFull();
        answerTextArea.setVisible(false);
        answerTextArea.setLabel("Answer");
        answerTextArea.setReadOnly(true);

        Button askButton = new Button("Ask model", e -> sendQuestion());

        add(questionTextField, askButton,sentQuestionTextArea, answerTextArea);
    }

    private void sendQuestion() {
        sentQuestionTextArea.setVisible(true);
        answerTextArea.setVisible(true);
        sentQuestionTextArea.setValue(questionTextField.getValue());
        questionTextField.clear();
        answerTextArea.setValue("Thinking...");
        Thread t = new Thread(() -> {
            generateService.sendPrompt(sentQuestionTextArea.getValue(),
                    (s) -> {
                        getUI().ifPresent(ui -> ui.access(
                                () -> {
                                    answerTextArea.setValue(s);
                                }));
                    });
        });
        t.start();
    }

}
