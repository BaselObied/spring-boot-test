package com.fawry.quizarena.domain;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String text;

    @ElementCollection
    @CollectionTable(name = "question_options", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "option_text")
    private List<String> options;

    @Column(nullable = false)
    private String correctAnswer;

    @Column(nullable = false)
    private String category;

    protected Question() {
        // JPA
    }

    public Question(String text, List<String> options, String correctAnswer, String category) {
        this.text = text;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getCategory() {
        return category;
    }

    public boolean isCorrect(String submittedAnswer) {
        return correctAnswer.equalsIgnoreCase(submittedAnswer);
    }
}
