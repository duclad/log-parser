package ro.duclad.logparser.importer;

/**
 * Handles validation for parsing a log line
 */
public interface LogParserValidator {

    void validate(LogLine validatedObject) throws ValidationException;


}
