package uk.gov.hmcts.probate.services.businessdocuments.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionAndAnswerRow implements Serializable {

    @NotBlank
    @JsonProperty("question")
    private String question;

    @JsonProperty("answers")
    private List<String> answers = new ArrayList<>();

    private final String[] NOT_ANSWERED = {"not answered"};

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswers() {

        if (answers.isEmpty() || answers.contains("")) {
            return Arrays.asList(NOT_ANSWERED);
        };
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }



}
