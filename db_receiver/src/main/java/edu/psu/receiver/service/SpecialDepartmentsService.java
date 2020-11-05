package edu.psu.receiver.service;

import edu.psu.receiver.db.PostgresDB;
import edu.psu.generatePostgreSQL.Sequences;
import edu.psu.generatePostgreSQL.tables.daos.SpecialDepartmentsDao;
import edu.psu.generatePostgreSQL.tables.pojos.SpecialDepartments;
import edu.psu.generatePostgreSQL.tables.records.SpecialDepartmentsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;

import java.sql.Connection;
import java.util.List;

import static edu.psu.generatePostgreSQL.tables.SpecialDepartments.SPECIAL_DEPARTMENTS;
import static org.jooq.SQLDialect.POSTGRES;

public class SpecialDepartmentsService {

    DSLContext context;
    private SpecialDepartmentsDao dao;

    public SpecialDepartmentsService() {
        Connection connection = PostgresDB.getInstance().getConnection();
        context = DSL.using(connection, POSTGRES);
        dao = new SpecialDepartmentsDao();
        Configuration configuration = new DefaultConfiguration().set(connection).set(POSTGRES);
        dao.setConfiguration(configuration);
    }

    public Integer insert(SpecialDepartments specialDepartments) {
        SpecialDepartmentsRecord specialDepartmentsRecord = context.selectFrom(SPECIAL_DEPARTMENTS)
                .where(SPECIAL_DEPARTMENTS.SPECIAL_DEPARTMENT.eq(specialDepartments.getSpecialDepartment()))
                .and(SPECIAL_DEPARTMENTS.ID_DEPARTMENT.eq(specialDepartments.getIdDepartment())).fetchOne();
        if (specialDepartmentsRecord != null) {
            return (Integer)specialDepartmentsRecord.get(0);
        } else {
            Integer id = context.nextval(Sequences.SPECIAL_DEPARTMENTS_ID_SEQ);
            specialDepartments.setId(id);
            dao.insert(specialDepartments);
            return id;
        }
    }

    public List<SpecialDepartments> getAll() {
        return context.selectFrom(SPECIAL_DEPARTMENTS).fetchInto(SpecialDepartments.class);
    }
}
