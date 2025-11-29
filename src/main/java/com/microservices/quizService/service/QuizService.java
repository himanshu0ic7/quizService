package com.microservices.quizService.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.microservices.quizService.feign.QuizInterface;
import com.microservices.quizService.model.QuestionWrapper;
import com.microservices.quizService.model.Quiz;
import com.microservices.quizService.model.Response;
import com.microservices.quizService.repository.QuizRepo;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;


@Service
public class QuizService {
	@Autowired
	QuizRepo quizRepo;
	
	@Autowired
	QuizInterface questionInterface;

	
	public ResponseEntity<Quiz> createQuiz(String category, int numQ, String title) {
		
		List<String> questions=questionInterface.getQuestionForQuiz(category,numQ).getBody();
	
		Quiz quiz= new Quiz();
		quiz.setTitle(title);
//		List<Question> questions= questionRepo.findRandomQuestionByCategory(category,numQ);
		quiz.setQuestionIds(questions);
		return new ResponseEntity<>(quizRepo.save(quiz),HttpStatus.CREATED);
	}
	
	
//	@CircuitBreaker(name="questionInterface",fallbackMethod = "getQuizFallback")
	public ResponseEntity<List<QuestionWrapper>> getQuiz(String quizId) {

        Optional<Quiz> quizOpt = quizRepo.findById(quizId);

        if (quizOpt.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404 if quiz doesn't exist
        }

        Quiz quiz = quizOpt.get();

        return questionInterface.getQuestions(quiz.getQuestionIds());
    }
	
	public ResponseEntity<List<QuestionWrapper>> getQuizFallback(String quizId, Throwable t) {
		List<QuestionWrapper> dummyQuestion= new ArrayList<>();
		QuestionWrapper question = new QuestionWrapper("1","dummy Question","option1","option2","option3","option4");
		dummyQuestion.add(question);
		return new ResponseEntity<>(dummyQuestion,HttpStatus.CREATED);
	}

	public ResponseEntity<Integer> submitQuiz(String quizId, List<Response> responses) {
		Integer score= questionInterface.getScore(responses).getBody();
	    return new ResponseEntity<>(score,HttpStatus.OK);
	}

}
