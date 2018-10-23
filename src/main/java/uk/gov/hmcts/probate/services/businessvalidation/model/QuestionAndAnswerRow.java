package uk.gov.hmcts.probate.services.businessvalidation.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

public class QuestionAndAnswerRow implements Serializable {

    @JsonProperty("question")
    private String question;

    @JsonProperty("answers")
    private List<String> answers;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }



}
