package ro.duclad.httplog;

import lombok.Data;
import ro.duclad.logparser.importer.BatchInsertable;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Data
public class HttpRequest implements BatchInsertable {

    private String requestIp;
    private LocalDateTime requestDate;
    private String requestMethod;
    private String requestURL;
    private String requestProtocol;
    private String responseCode;
    private String userAgent;

    @Override
    public String generateInsert() {
        return "INSERT INTO http_requests (REQUEST_IP, REQUEST_DATE, REQUEST_METHOD, REQUEST_URL," +
                " REQUEST_PROTOCOL, RESPONSE_CODE, REQUEST_USER_AGENT) " +
                "VALUES (?,?,?,?,?,?,?)";
    }

    @Override
    public void fillPrepareStatement(PreparedStatement ps) throws SQLException {
        ps.setString(1, requestIp);
        ps.setObject(2, requestDate);
        ps.setString(3, requestMethod);
        ps.setString(4, requestURL);
        ps.setString(5, requestProtocol);
        ps.setString(6, responseCode);
        ps.setString(7, userAgent);
    }
}
