package edu.psu.sender.service;

import edu.psu.sender.db.SQLiteDB;
import edu.psu.generateSQLite.tables.pojos.PermCityPolyclinic_7Registry;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.util.List;

import static edu.psu.generateSQLite.tables.PermCityPolyclinic_7Registry.PERM_CITY_POLYCLINIC_7_REGISTRY;
import static org.jooq.SQLDialect.SQLITE;

public class PermCityPolyclinic7Service {

    DSLContext context;

    public PermCityPolyclinic7Service() {
        context = DSL.using(SQLiteDB.getInstance().getConnection(), SQLITE);
    }

    public List<PermCityPolyclinic_7Registry> getAllCourses() {
        return context.selectFrom(PERM_CITY_POLYCLINIC_7_REGISTRY).fetchInto(PermCityPolyclinic_7Registry.class);
    }
}
