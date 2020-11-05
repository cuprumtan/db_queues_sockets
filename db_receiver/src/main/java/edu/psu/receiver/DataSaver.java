package edu.psu.receiver;

import edu.psu.generatePostgreSQL.tables.pojos.*;
import edu.psu.generateSQLite.tables.pojos.PermCityPolyclinic_7Registry;
import edu.psu.receiver.service.*;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class DataSaver {

    DepartmentsService departmentsService;
    SpecialDepartmentsService specialDepartmentsService;
    QualificationCategoriesService qualificationCategoriesService;
    DoctorsService doctorsService;
    PatientsService patientsService;
    VisitsService visitsService;

    public DataSaver() {
        departmentsService = new DepartmentsService();
        specialDepartmentsService = new SpecialDepartmentsService();
        qualificationCategoriesService = new QualificationCategoriesService();
        doctorsService = new DoctorsService();
        patientsService = new PatientsService();
        visitsService = new VisitsService();
    }

    public void convertAndInsertToDB(PermCityPolyclinic_7Registry row) {
        // Сохоранение Департамента
        Departments departments = new Departments(null, row.getDepartment());
        Integer departmentId = departmentsService.insert(departments);

        // Сохранение специального департамента
        SpecialDepartments specialDepartments = new SpecialDepartments(null, row.getSpecialDepartment(), departmentId);
        Integer specialDepartmentId = specialDepartmentsService.insert(specialDepartments);

        // Сохранение квалификационной категории
        QualificationCategories qualificationCategories = new QualificationCategories(null, row.getQualificationCategoryDoctor());
        Integer qualificationCategoriesId = qualificationCategoriesService.insert(qualificationCategories);

        // Сохранение доктора
        String[] fioDcotor = row.getNameDoctor().split(" ");
        LocalDate birthDateDoctor = getLocalDateFromString(row.getBirthDateDoctor());
        Doctors doctors = new Doctors(null, fioDcotor[0], fioDcotor[1], fioDcotor[2], birthDateDoctor, row.getSexDoctor(), row.getEducationDoctor(), row.getPositionDoctor(), qualificationCategoriesId, specialDepartmentId);
        Integer doctorsId = doctorsService.insert(doctors);

        // Сохранение пациента
        String[] fioPatient = row.getNamePatient().split(" ");
        LocalDate birthDatePatient = getLocalDateFromString(row.getBirthDatePatient());
        Patients patients = new Patients(row.getCardNumber(), fioPatient[0], fioPatient[1], fioPatient[2], birthDatePatient, row.getSexPatient());
        Integer patientsCardNumber = patientsService.insert(patients);

        // Сохранение посещения
        OffsetDateTime visitDateTime = getOffsetDateTimeFromString(row.getVisitDatetime());
        Boolean status = row.getVisitStatus() == 1 ? Boolean.TRUE : Boolean.FALSE;
        Visits visits = new Visits(null, visitDateTime, doctorsId, patientsCardNumber, status, row.getVisitComment());
        visitsService.insert(visits);
    }

    private OffsetDateTime getOffsetDateTimeFromString(String dateTimeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final ZoneId zone = ZoneId.of("Eire");
        ZoneOffset zoneOffSet = zone.getRules().getOffset(LocalDateTime.parse(dateTimeStr, formatter));
        return LocalDateTime.parse(dateTimeStr, formatter).atOffset(zoneOffSet);

    }

    private LocalDate getLocalDateFromString(String birthDateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(birthDateStr, formatter);
    }
}
