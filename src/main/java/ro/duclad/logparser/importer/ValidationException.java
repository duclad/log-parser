package ro.duclad.logparser.importer;

import lombok.Data;
import ro.duclad.logparser.importer.BatchInsertable;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A validation exception that could be thrown when parsing a log line.
 */
@Data
public class ValidationException extends Exception implements BatchInsertable {
    private int lineNumber;
    private String parseError;
    private String log;

    public ValidationException(int lineNumber, String parseError, String log) {
        this.lineNumber = lineNumber;
        this.parseError = parseError;
        this.log = log;
    }

    @Override
    public String generateInsert() {
        return "INSERT INTO log_parse_errors (LOG_LINE, PARSE_ERROR, LOG_CONTENT) VALUES (?,?,?)";
    }

    @Override
    public void fillPrepareStatement(PreparedStatement ps) throws SQLException {
        ps.setInt(1, lineNumber);
        ps.setString(2, parseError);
        ps.setString(3, log);
    }
}
