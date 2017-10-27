package ro.duclad.logparser.importer;

import lombok.Data;

/**
 * A line from the log file
 */
@Data
public class LogLine {
    private int lineNumber;
    private String content;

    public LogLine(int lineNumber, String content) {
        this.lineNumber = lineNumber;
        this.content = content;
    }
}
