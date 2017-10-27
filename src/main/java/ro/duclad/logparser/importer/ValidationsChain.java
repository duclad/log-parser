package ro.duclad.logparser.importer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Provides a chain of validator which are executed sequentially.
 * If a validator fails the validation exception consumer get a chance to handle it in some specific way.
 *
 * @param <V> Type of logParserValidators
 */
public class ValidationsChain<V extends LogParserValidator> {

    List<LogParserValidator> logParserValidators = new ArrayList<>();
    Consumer<ValidationException> validationExceptionConsumer = e -> {
        System.out.println(e.getParseError());
    };

    /**
     * Sets the validation exception consumer
     *
     * @param validationExceptionConsumer
     * @return
     */
    public ValidationsChain withErrorHandler(Consumer<ValidationException> validationExceptionConsumer) {
        this.validationExceptionConsumer = validationExceptionConsumer;
        return this;
    }

    /**
     * Adds a validator to the chain
     *
     * @param validator
     * @return
     */
    public ValidationsChain withValidator(V validator) {
        if (validator != null) {
            logParserValidators.add(validator);
        }
        return this;
    }

    /**
     * Start the chain of validation for the passed object. If a validator fails the chain is stopped
     * and, if passed, the validation exeption consumer has the chance to handles the failure.
     *
     * @param validatedObject - The log line to be validated for parsing
     * @return - returns true if all validators succeeds of false if at least one of them fails
     */
    public boolean validate(LogLine validatedObject) {
        boolean validationReturn = true;
        for (LogParserValidator logParserValidator : logParserValidators) {
            try {
                logParserValidator.validate(validatedObject);
            } catch (ValidationException e) {
                validationReturn = false;
                validationExceptionConsumer.accept(e);
            }
        }
        return validationReturn;
    }
}
