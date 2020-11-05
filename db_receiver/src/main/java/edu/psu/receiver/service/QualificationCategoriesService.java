package edu.psu.receiver.service;

import edu.psu.receiver.db.PostgresDB;
import edu.psu.generatePostgreSQL.Sequences;
import edu.psu.generatePostgreSQL.tables.daos.QualificationCategoriesDao;
import edu.psu.generatePostgreSQL.tables.pojos.QualificationCategories;
import edu.psu.generatePostgreSQL.tables.records.QualificationCategoriesRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;

import java.sql.Connection;
import java.util.List;

import static edu.psu.generatePostgreSQL.tables.QualificationCategories.QUALIFICATION_CATEGORIES;
import static org.jooq.SQLDialect.POSTGRES;

public class QualificationCategoriesService {

    DSLContext context;
    private QualificationCategoriesDao dao;

    public QualificationCategoriesService() {
        Connection connection = PostgresDB.getInstance().getConnection();
        context = DSL.using(connection, POSTGRES);
        dao = new QualificationCategoriesDao();
        Configuration configuration = new DefaultConfiguration().set(connection).set(POSTGRES);
        dao.setConfiguration(configuration);
    }

    public Integer insert(QualificationCategories qualificationCategories) {
        QualificationCategoriesRecord qualificationCategoriesRecord = context.selectFrom(QUALIFICATION_CATEGORIES)
                .where(QUALIFICATION_CATEGORIES.QUALIFICATION_CATEGORY.eq(qualificationCategories.getQualificationCategory())).fetchOne();
        if (qualificationCategoriesRecord != null) {
            return (Integer)qualificationCategoriesRecord.get(0);
        } else {
            Integer id = context.nextval(Sequences.QUALIFICATION_CATEGORIES_ID_SEQ);
            qualificationCategories.setId(id);
            dao.insert(qualificationCategories);
            return id;
        }
    }

    public List<QualificationCategories> getAll() {
        return context.selectFrom(QUALIFICATION_CATEGORIES).fetchInto(QualificationCategories.class);
    }
}
