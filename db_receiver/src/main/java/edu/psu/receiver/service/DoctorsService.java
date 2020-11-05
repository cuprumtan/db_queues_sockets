package edu.psu.receiver.service;

import edu.psu.receiver.db.PostgresDB;
import edu.psu.generatePostgreSQL.Sequences;
import edu.psu.generatePostgreSQL.tables.daos.DoctorsDao;
import edu.psu.generatePostgreSQL.tables.pojos.Doctors;
import edu.psu.generatePostgreSQL.tables.records.DoctorsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;

import java.sql.Connection;
import java.util.List;

import static edu.psu.generatePostgreSQL.tables.Doctors.DOCTORS;
import static org.jooq.SQLDialect.POSTGRES;

public class DoctorsService {

    DSLContext context;
    private DoctorsDao dao;

    public DoctorsService() {
        Connection connection = PostgresDB.getInstance().getConnection();
        context = DSL.using(connection, POSTGRES);
        dao = new DoctorsDao();
        Configuration configuration = new DefaultConfiguration().set(connection).set(POSTGRES);
        dao.setConfiguration(configuration);
    }

    public Integer insert(Doctors doctors) {
        DoctorsRecord doctorsRecord = context.selectFrom(DOCTORS)
                .where(DOCTORS.LAST_NAME.eq(doctors.getFirstName()))
                .and(DOCTORS.FIRST_NAME.eq(doctors.getFirstName()))
                .and(DOCTORS.PATRONYMIC.eq(doctors.getPatronymic()))
                .and(DOCTORS.BIRTH_DATE.eq(doctors.getBirthDate()))
                .and(DOCTORS.SEX.eq(doctors.getSex()))
                .and(DOCTORS.EDUCATION.eq(doctors.getEducation()))
                .and(DOCTORS.POSITION.eq(doctors.getPosition()))
                .and(DOCTORS.ID_QUALIFICATION_CATEGORY.eq(doctors.getIdQualificationCategory()))
                .and(DOCTORS.ID_SPECIAL_DEPARTMENT.eq(doctors.getIdSpecialDepartment())).fetchOne();
        if (doctorsRecord != null) {
            return (Integer)doctorsRecord.get(0);
        } else {
            Integer id = context.nextval(Sequences.DOCTORS_ID_SEQ);
            doctors.setId(id);
            dao.insert(doctors);
            return id;
        }
    }

    public List<Doctors> getAll() {
        return context.selectFrom(DOCTORS).fetchInto(Doctors.class);
    }
}
