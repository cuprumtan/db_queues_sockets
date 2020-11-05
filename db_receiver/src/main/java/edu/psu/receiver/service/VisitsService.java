package edu.psu.receiver.service;

import edu.psu.receiver.db.PostgresDB;
import edu.psu.generatePostgreSQL.Sequences;
import edu.psu.generatePostgreSQL.tables.daos.VisitsDao;
import edu.psu.generatePostgreSQL.tables.pojos.Visits;
import edu.psu.generatePostgreSQL.tables.records.VisitsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;

import java.sql.Connection;
import java.util.List;

import static edu.psu.generatePostgreSQL.tables.Visits.VISITS;
import static org.jooq.SQLDialect.POSTGRES;

public class VisitsService {

    DSLContext context;
    private VisitsDao dao;

    public VisitsService() {
        Connection connection = PostgresDB.getInstance().getConnection();
        context = DSL.using(connection, POSTGRES);
        dao = new VisitsDao();
        Configuration configuration = new DefaultConfiguration().set(connection).set(POSTGRES);
        dao.setConfiguration(configuration);
    }

    public Integer insert(Visits visits) {
        VisitsRecord visitsRecord = context.selectFrom(VISITS)
                .where(VISITS.VISIT_DATETIME.eq(visits.getVisitDatetime()))
                .and(VISITS.ID_DOCTOR.eq(visits.getIdDoctor()))
                .and(VISITS.CARD_NUMBER.eq(visits.getCardNumber()))
                .and(VISITS.STATUS.eq(visits.getStatus()))
                .and(VISITS.VISIT_COMMENT.eq(visits.getVisitComment())).fetchOne();
        if (visitsRecord != null) {
            return (Integer)visitsRecord.get(0);
        } else {
            Integer id = context.nextval(Sequences.VISITS_ID_SEQ);
            visits.setId(id);
            dao.insert(visits);
            return id;
        }
    }

    public List<Visits> getAll() {
        return context.selectFrom(VISITS).fetchInto(Visits.class);
    }
}
