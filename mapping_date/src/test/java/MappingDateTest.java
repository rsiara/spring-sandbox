import configuration.RootConfig;
import model.DateWithTemporalSandbox;
import model.DateWithoutTemporalSandbox;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class MappingDateTest {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


/*
    @Temporal - Powinno zawsze się stosować do podczas zapisywania czasu do bazy danych.
    Może być stosowana do typów tylko takich jak Calendar oraz Date.

    W bardzo dużym skrócie - TemporalType. np. TemporalType.TIMESTAMP oznacza
    do jakiego typu w bazie danych ma być zapisana data z obiektów javowych takich jak np. Calendar czy Date.
*/

    @Test
    @Transactional
    @Rollback(false)
    public void mapping_date_temportal_annotation_test() {

        Date currentDate = new Date();

        DateWithTemporalSandbox dateWithTemporalSandbox = new DateWithTemporalSandbox();

        dateWithTemporalSandbox.setDateColumn(currentDate);
        dateWithTemporalSandbox.setDatetimeColumn(currentDate);
        dateWithTemporalSandbox.setTimeColumn(currentDate);
        dateWithTemporalSandbox.setTimestampColumn(currentDate);

        DateWithoutTemporalSandbox dateWithoutTemporalSandbox = new DateWithoutTemporalSandbox();

        dateWithoutTemporalSandbox.setDateColumn(currentDate);
        dateWithoutTemporalSandbox.setDatetimeColumn(currentDate);
        dateWithoutTemporalSandbox.setTimeColumn(currentDate);
        dateWithoutTemporalSandbox.setTimestampColumn(currentDate);

        entityManager.persist(dateWithTemporalSandbox);
        entityManager.persist(dateWithoutTemporalSandbox);
    }
}