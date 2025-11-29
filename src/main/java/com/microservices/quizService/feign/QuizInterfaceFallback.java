package com.microservices.quizService.feign;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.microservices.quizService.model.QuestionWrapper;
import com.microservices.quizService.model.Response;

@Component
public class QuizInterfaceFallback implements QuizInterface {

    @Override
    public ResponseEntity<List<String>> getQuestionForQuiz(String category, Integer numQ) {
        return ResponseEntity.ok(new ArrayList<>());
    }

    @Override
    public ResponseEntity<List<QuestionWrapper>> getQuestions(List<String> questionIds) {
        List<QuestionWrapper> dummy = List.of(
            new QuestionWrapper("0", "Service Currently Unavailable", "N/A", "N/A", "N/A", "N/A")
        );
        return ResponseEntity.ok(dummy);
    }

    @Override
    public ResponseEntity<Integer> getScore(List<Response> responses) {
        return ResponseEntity.ok(0);
    }
}
