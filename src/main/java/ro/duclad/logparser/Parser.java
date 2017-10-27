package ro.duclad.logparser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import ro.duclad.httplog.HttpRequestsApplicationParameters;
import ro.duclad.httplog.HttpRequestsSelectByInterval;

import java.time.format.DateTimeFormatter;

@SpringBootApplication
@ComponentScan({"ro.duclad"})
public class Parser implements ApplicationRunner {

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    HttpRequestsSelectByInterval requestsSelectByInterval;

    public static void main(String[] args) {
        SpringApplication.run(Parser.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        ApplicationParameters parameters = createApplicationParameters(args);
        parameters.getApplication(applicationContext).execute(createApplicationParameters(args));
    }

    //TODO: move this on a factory class based on some argument named perhaps importType
    private ApplicationParameters createApplicationParameters(ApplicationArguments args) {
        HttpRequestsApplicationParameters.Builder parameters = HttpRequestsApplicationParameters.Builder();
        if (args.containsOption("file")) {
            parameters.withFile(args.getOptionValues("file").get(0));
        }
        if (args.containsOption("clean")) {
            parameters.withClean(true);
        }
        if (args.containsOption("startDate")) {
            parameters.withFromDate(args.getOptionValues("startDate").get(0));
        }
        if (args.containsOption("duration")) {
            parameters.withInterval(args.getOptionValues("duration").get(0).toUpperCase());
        }
        if (args.containsOption("threshold")) {
            parameters.withThreshold(args.getOptionValues("threshold").get(0));
        }
        return parameters.build();
    }
}
