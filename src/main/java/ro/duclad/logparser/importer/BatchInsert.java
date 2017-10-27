package ro.duclad.logparser.importer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles batch inserts for objects of type T
 *
 * @param <T>
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BatchInsert<T extends BatchInsertable> {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    private List<T> records = new ArrayList<>();

    private int batchSize = 10000;

    private int counter;

    @Autowired
    public BatchInsert(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Add an object to this batch. When batch reach its limit will get inserted in DB
     *
     * @param record
     */
    public void add(T record) {
        if (records.size() < batchSize) {
            records.add(record);
        } else {
            insertBatch(records.get(0).generateInsert(), records);
            counter = counter + batchSize;
            records.clear();
            records.add(record);
        }
    }

    /**
     * Flush current content of the batch triggering an insert in DB
     */
    public void flush() {
        if (!records.isEmpty()) {
            insertBatch(records.get(0).generateInsert(), records);
            counter = +records.size();
            records.clear();
        }
    }

    /**
     * Actual insert of the batch in DB.
     *
     * @param insertSql - INSERT SQL used by the batch
     * @param records   - List of records to be inserted as a batch
     */
    private void insertBatch(final String insertSql, final List<T> records) {

        jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                records.get(i).fillPrepareStatement(ps);
            }

            @Override
            public int getBatchSize() {
                return records.size();
            }
        });
    }

    public int getNumberOfRowsInsertedUntilNow() {
        return counter;
    }
}
