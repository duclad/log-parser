package ro.duclad.logparser.importer;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Marks an object as able to be used by a BatchInsert
 */
public interface BatchInsertable {

    void fillPrepareStatement(PreparedStatement ps) throws SQLException;

    String generateInsert();
}
