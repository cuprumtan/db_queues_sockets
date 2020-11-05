package edu.psu.receiver.service;

import edu.psu.receiver.db.PostgresDB;
import edu.psu.generatePostgreSQL.Sequences;
import edu.psu.generatePostgreSQL.tables.daos.PatientsDao;
import edu.psu.generatePostgreSQL.tables.pojos.Patients;
import edu.psu.generatePostgreSQL.tables.records.PatientsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;

import java.sql.Connection;
import java.util.List;

import static edu.psu.generatePostgreSQL.tables.Patients.PATIENTS;
import static org.jooq.SQLDialect.POSTGRES;

public class PatientsService {

    DSLContext context;
    private PatientsDao dao;

    public PatientsService() {
        Connection connection = PostgresDB.getInstance().getConnection();
        context = DSL.using(connection, POSTGRES);
        dao = new PatientsDao();
        Configuration configuration = new DefaultConfiguration().set(connection).set(POSTGRES);
        dao.setConfiguration(configuration);
    }

    public Integer insert(Patients patients) {
        PatientsRecord patientsRecord = context.selectFrom(PATIENTS)
                .where(PATIENTS.CARD_NUMBER.eq(patients.getCardNumber())).fetchOne();
        if (patientsRecord != null) {
            return (Integer)patientsRecord.get(0);
        } else {
            Integer cardNumber = context.nextval(Sequences.PATIENTS_CARD_NUMBER_SEQ);
            patients.setCardNumber(cardNumber);
            dao.insert(patients);
            return cardNumber;
        }
    }

    public List<Patients> getAll() {
        return context.selectFrom(PATIENTS).fetchInto(Patients.class);
    }
}
