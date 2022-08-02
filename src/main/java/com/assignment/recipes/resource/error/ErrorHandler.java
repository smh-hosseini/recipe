package com.assignment.recipes.resource.error;

import com.assignment.recipes.service.exception.RecipeNotFoundException;
import java.util.Map;
import javax.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;

@ControllerAdvice
@Slf4j
public class ErrorHandler implements ProblemHandling {

    @ExceptionHandler(RecipeNotFoundException.class)
    public ResponseEntity<Problem> handleNotFound(RecipeNotFoundException ex,
        NativeWebRequest request) {
        log.debug(ex.getMessage());
        return create(ex, createNotFoundProblem(ex.getMessage()), request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Problem> handleDataIntegrity(DataIntegrityViolationException ex,
        NativeWebRequest request) {
        log.error(ex.getMessage());
        return create(ex, createBadRequestProblem("Recipe with the same name exist", ex.getMessage()), request);
    }

    private Problem createNotFoundProblem(String detail) {
        return createProblem(Status.NOT_FOUND, "Resource not found", detail, null);
    }

    private Problem createBadRequestProblem(String title, String detail) {
        return createProblem(Status.BAD_REQUEST, title, detail, null);
    }

    private Problem createProblem(Status status, String title, String detail, Map<String, Object> additionalInfo) {
        ProblemBuilder problemBuilder = Problem.builder()
            .withStatus(status)
            .withTitle(title)
            .withDetail(detail);
        if (!CollectionUtils.isEmpty(additionalInfo)) {
            additionalInfo.forEach(problemBuilder::with);
        }
        return problemBuilder.build();
    }

}
