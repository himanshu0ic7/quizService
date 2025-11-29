package com.microservices.quizService.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.microservices.quizService.model.QuestionWrapper;
import com.microservices.quizService.model.Response;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;


@FeignClient(name= "QUESTIONSERVICE", fallback = QuizInterfaceFallback.class)
//@FeignClient("QUESTIONSERVICE")
public interface QuizInterface {
		//generate (return list of questionId
		@GetMapping("question/generate")
		public ResponseEntity<List<String>> getQuestionForQuiz(@RequestParam String category,@RequestParam Integer numQ);
		
		//getQuestion(questionId)
//		@CircuitBreaker(name="questionInterface", fallbackMethod="fallbackQuestions")
		@PostMapping("question/getQuestions")
		public ResponseEntity<List<QuestionWrapper>> getQuestions(@RequestBody List<String> questionIds);
		
		//calculateScore
		@PostMapping("question/score")
		public ResponseEntity<Integer> getScore(@RequestBody List<Response> responses);
		
//		default ResponseEntity<List<QuestionWrapper>> fallbackQuestions(List<String> ids, Throwable t) {
//	        List<QuestionWrapper> dummy = List.of(
//	            new QuestionWrapper("0", "Service unavailable", "A","B","C","D")
//	        );
//	        return ResponseEntity.ok(dummy);
//	    }
}
