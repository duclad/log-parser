package ro.duclad.logparser;

import org.springframework.context.ApplicationContext;

/**
 * This is passed further to different components
 */
public interface ApplicationParameters<T extends Application> {

    T getApplication(ApplicationContext applicationContext);
}
