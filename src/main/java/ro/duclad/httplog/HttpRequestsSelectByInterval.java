package ro.duclad.httplog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class HttpRequestsSelectByInterval {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public HttpRequestsSelectByInterval(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    List<String> selectHttpRequests(LocalDateTime fromDate, HttpRequestsApplicationParameters.INTERVAL interval, int threshold) {
        jdbcTemplate.execute("TRUNCATE TABLE selected_http_requests");
        jdbcTemplate.update("INSERT INTO selected_http_requests(REQUEST_IP, SELECTION_REASON)" +
                " SELECT t.REQUEST_IP, concat(count(*) , ' request were made from this IP in ',?, ' starting on ',?)" +
                " FROM log_parser.http_requests t" +
                " WHERE REQUEST_DATE > ? AND REQUEST_DATE < ?" +
                " GROUP BY REQUEST_IP HAVING count(*)>?", interval.toString(), fromDate, fromDate, fromDate.plusSeconds(interval.inSeconds()), threshold);
        return jdbcTemplate.queryForList("SELECT REQUEST_IP FROM selected_http_requests", String.class);
    }


}
