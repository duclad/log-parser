package ro.duclad.logparser;

/**
 * Main application component responsible with executing the business based on the params
 *
 * @param <T>
 */
public interface Application<T extends ApplicationParameters> {
    void execute(T params);
}
