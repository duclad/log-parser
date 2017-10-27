package ro.duclad.httplog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ro.duclad.logparser.importer.BatchInsert;
import ro.duclad.logparser.importer.Importer;
import ro.duclad.logparser.importer.LogLine;
import ro.duclad.logparser.importer.ValidationException;
import ro.duclad.logparser.importer.ValidationsChain;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

@Component
public class HttpRequestsImporter implements Importer<HttpRequestsApplicationParameters>{


    private final BatchInsert<HttpRequest> requestsBatchInsert;

    private final BatchInsert<ValidationException> exceptionsBatchInsert;

    private final StringToHttpRequestConverter converter;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public HttpRequestsImporter(BatchInsert<HttpRequest> requestsBatchInsert, BatchInsert<ValidationException> exceptionsBatchInsert, StringToHttpRequestConverter converter, JdbcTemplate jdbcTemplate) {
        this.requestsBatchInsert = requestsBatchInsert;
        this.exceptionsBatchInsert = exceptionsBatchInsert;
        this.converter = converter;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void importFile(HttpRequestsApplicationParameters parameters) {
        jdbcTemplate.execute("TRUNCATE TABLE log_parse_errors");
        ValidationsChain validationsChain = new ValidationsChain().//
                withValidator(new RequestDateValidator()).//
                withValidator(new IpValidator()).//
                withErrorHandler(e -> exceptionsBatchInsert.add((ValidationException) e));
        try (LineNumberReader lnr = new LineNumberReader(new FileReader(parameters.getFile()))) {
            String logLine;
            while ((logLine = lnr.readLine()) != null) {
                LogLine log = new LogLine(lnr.getLineNumber(), logLine);
                if (validationsChain.validate(log)) {
                    requestsBatchInsert.add(converter.convert(logLine.replaceAll("\"", "")));
                }
            }
            requestsBatchInsert.flush();
            exceptionsBatchInsert.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clean() {
        jdbcTemplate.execute("TRUNCATE TABLE http_requests");
    }
}
