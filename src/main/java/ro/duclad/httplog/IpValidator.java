package ro.duclad.httplog;

import ro.duclad.logparser.importer.LogLine;
import ro.duclad.logparser.importer.LogParserValidator;
import ro.duclad.logparser.importer.ValidationException;

import java.util.StringTokenizer;
import java.util.regex.Matcher;

public class IpValidator implements LogParserValidator {

    @Override
    public void validate(LogLine validatedObject) throws ValidationException {
        StringTokenizer stringTokenizer = new StringTokenizer(validatedObject.getContent(), "|");
        try {
            stringTokenizer.nextToken();
            Matcher ipPatternMatcher = StringToHttpRequestConverter.IP_PATTERN.matcher(stringTokenizer.nextToken());
            if (!ipPatternMatcher.find()) {
                throw new ValidationException(validatedObject.getLineNumber(),"Second element must be a valid IP", validatedObject.getContent());
            }
        } catch (Exception e) {
            throw new ValidationException(validatedObject.getLineNumber(),"Second element must be a valid IP", validatedObject.getContent());
        }
    }
}
