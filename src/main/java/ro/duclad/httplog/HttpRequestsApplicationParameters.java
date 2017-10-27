package ro.duclad.httplog;

import lombok.Data;
import org.springframework.context.ApplicationContext;
import static ro.duclad.httplog.StringToHttpRequestConverter.DATE_FORMAT;
import ro.duclad.logparser.ApplicationParameters;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class HttpRequestsApplicationParameters implements ApplicationParameters {
    private String file;
    private LocalDateTime from;
    private Integer threshold;
    private INTERVAL interval;
    private boolean clean;
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");

    public static Builder Builder() {
        return new Builder();
    }

    @Override
    public HttpApplication getApplication(ApplicationContext applicationContext) {
        return applicationContext.getBean(HttpApplication.class);
    }

    public enum INTERVAL {
        DAILY, HOURLY;

        @Override
        public String toString() {
            switch (this) {
                case DAILY:
                    return "one day";
                case HOURLY:
                    return "one hour";
                default:
                    throw new IllegalArgumentException();
            }
        }

        public long inSeconds() {
            switch (this) {
                case HOURLY:
                    return 3600;
                case DAILY:
                    return 86399;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    public static final class Builder {
        String file;
        LocalDateTime from;
        Integer threshold;
        INTERVAL interval = INTERVAL.HOURLY;
        boolean clean = false;

        private Builder() {
        }

        public Builder withFile(String file) {
            this.file = file;
            return this;
        }

        public Builder withFromDate(String fromDate) {
            try {
                this.from = LocalDateTime.parse(fromDate, DATE_FORMAT);
            } catch (Exception e) {
                throw new IllegalArgumentException("Date format is " + DATE_FORMAT.toString());
            }
            return this;
        }

        public Builder withThreshold(String threshold) {
            this.threshold = Integer.valueOf(threshold);
            return this;
        }

        public Builder withInterval(String interval) {
            this.interval = INTERVAL.valueOf(interval);
            return this;
        }

        public Builder withClean(boolean clean) {
            this.clean = clean;
            return this;
        }

        public ApplicationParameters build() {
            HttpRequestsApplicationParameters httpRequestsApplicationParameters = new HttpRequestsApplicationParameters();
            httpRequestsApplicationParameters.setFile(file);
            httpRequestsApplicationParameters.setFrom(from);
            httpRequestsApplicationParameters.setThreshold(threshold);
            httpRequestsApplicationParameters.setInterval(interval);
            httpRequestsApplicationParameters.setClean(clean);
            return httpRequestsApplicationParameters;
        }
    }
}
