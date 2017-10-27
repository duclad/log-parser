package ro.duclad.logparser.importer;

import ro.duclad.httplog.HttpRequestsApplicationParameters;

public interface Importer<T extends HttpRequestsApplicationParameters> {

    void importFile(T parameters);

    void clean();
}
