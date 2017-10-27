package ro.duclad.httplog;

import ro.duclad.logparser.importer.LogLine;
import ro.duclad.logparser.importer.LogParserValidator;
import ro.duclad.logparser.importer.ValidationException;

import java.time.LocalDateTime;
import java.util.StringTokenizer;

public class RequestDateValidator implements LogParserValidator {


    @Override
    public void validate(LogLine validatedObject) throws ValidationException {
        StringTokenizer stringTokenizer = new StringTokenizer(validatedObject.getContent(), "|");

        try {
            LocalDateTime.parse(stringTokenizer.nextToken(), StringToHttpRequestConverter.DATE_FORMAT);
        } catch (Exception e) {
            throw new ValidationException(validatedObject.getLineNumber(), "First element must be a requestDate in format yyyy-MM-dd.HH:mm:ss", validatedObject.getContent());
        }
    }
}
