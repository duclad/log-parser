package ro.duclad.httplog;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.duclad.logparser.Application;
import ro.duclad.logparser.Parser;
import ro.duclad.logparser.importer.Importer;

import java.util.List;

@Component
public class HttpApplication implements Application<HttpRequestsApplicationParameters> {

    private static final Logger logger = LoggerFactory.getLogger(Parser.class);

    @Autowired
    Importer<HttpRequestsApplicationParameters> httpRequestsImporter;

    @Autowired
    HttpRequestsSelectByInterval requestsSelectByInterval;

    @Override
    public void execute(HttpRequestsApplicationParameters params) {
        if (params.isClean()) {
            httpRequestsImporter.clean();
        }
        if (StringUtils.isNoneBlank(params.getFile())) {
            httpRequestsImporter.importFile(params);
        }
        if (params.getFrom() != null) {
            List<String> result = requestsSelectByInterval.selectHttpRequests(params.getFrom(), params.getInterval(), params.getThreshold());
            result.forEach(logger::info);
        }

    }
}
