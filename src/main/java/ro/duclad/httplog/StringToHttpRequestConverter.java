package ro.duclad.httplog;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

@Component
public class StringToHttpRequestConverter implements Converter<String, HttpRequest> {
    public static final Pattern IP_PATTERN = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public HttpRequest convert(String s) {
        HttpRequest httpRequest = new HttpRequest();
        StringTokenizer stringTokenizer = new StringTokenizer(s, "|");

        if (stringTokenizer.countTokens() < 2) {
            throw new IllegalArgumentException("IP which sent the request and Date on was sent are required");
        }
        String requestDateAsString = stringTokenizer.nextToken();

        httpRequest.setRequestDate(LocalDateTime.parse(requestDateAsString, DATE_FORMAT));
        httpRequest.setRequestIp(stringTokenizer.nextToken());

        if (stringTokenizer.hasMoreTokens()) {
            String request = stringTokenizer.nextToken();
            int idxOfUrl = request.indexOf(' ');
            int idxOfProtocol = request.lastIndexOf(' ');
            httpRequest.setRequestMethod(request.substring(0, idxOfUrl).replace("\"", ""));
            httpRequest.setRequestURL(request.substring(idxOfUrl, idxOfProtocol));
            httpRequest.setRequestProtocol(request.substring(idxOfProtocol, request.length()).replace("\"", ""));
        }
        if (stringTokenizer.hasMoreTokens()) {
            httpRequest.setResponseCode(stringTokenizer.nextToken());
        }
        if (stringTokenizer.hasMoreTokens()) {
            httpRequest.setUserAgent(stringTokenizer.nextToken());
        }
        return httpRequest;
    }
}
