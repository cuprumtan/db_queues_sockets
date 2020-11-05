package edu.psu.receiver.service;

import edu.psu.receiver.db.PostgresDB;
import edu.psu.generatePostgreSQL.Sequences;
import edu.psu.generatePostgreSQL.tables.daos.DepartmentsDao;
import edu.psu.generatePostgreSQL.tables.pojos.Departments;
import edu.psu.generatePostgreSQL.tables.records.DepartmentsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;

import java.sql.Connection;
import java.util.List;

import static edu.psu.generatePostgreSQL.tables.Departments.DEPARTMENTS;
import static org.jooq.SQLDialect.POSTGRES;

public class DepartmentsService {

    DSLContext context;
    private DepartmentsDao dao;

    public DepartmentsService() {
        Connection connection = PostgresDB.getInstance().getConnection();
        context = DSL.using(connection, POSTGRES);
        dao = new DepartmentsDao();
        Configuration configuration = new DefaultConfiguration().set(connection).set(POSTGRES);
        dao.setConfiguration(configuration);
    }

    public Integer insert(Departments departments) {
        DepartmentsRecord departmentsRecord = context.selectFrom(DEPARTMENTS)
                .where(DEPARTMENTS.DEPARTMENT.eq(departments.getDepartment())).fetchOne();
        if (departmentsRecord != null) {
            return (Integer)departmentsRecord.get(0);
        } else {
            Integer id = context.nextval(Sequences.DEPARTMENTS_ID_SEQ);
            departments.setId(id);
            dao.insert(departments);
            return id;
        }
    }

    public List<Departments> getAll() {
        return context.selectFrom(DEPARTMENTS).fetchInto(Departments.class);
    }
}
