import configuration.RootConfig;

import model.PrintJob;
import model.PrintQueue;
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

  /*
  @OrderColumn pozwala na zachowanie kolejności zgodnej z kolejnością zapisywania do bazy danych.
  Wymaga dodatkowej kolumny która będzie zawierała dane o kolejności rekordu.

  Jest to rozwiązanie ciężkie dla bazy danych ponieważ każde wyjęcie z bazy danych i wstawienie powoduje update w bazie.


  Uwaga: To rozwiązanie nie działa na typie Collection, musiałem zmienić typ na List. Zapewne chodzi o to że interfejs kolekcji musi implementować jakąś kolejność.

"OK I finally found out what the problem is - I was using java.util.Collection. If I change it to java.util.List - it works. If I change it to java.util.SortedSet I get this exception:
Caused by: org.hibernate.AnnotationException: A sorted collection must define and ordering or sorting : xxxx.ProductModel.galleryImages
    at org.hibernate.cfg.annotations.CollectionBinder.applySortingAndOrdering(CollectionBinder.java:597)
    at org.hibernate.cfg.annotations.CollectionBinder.bind(CollectionBinder.java:434)
    at org.hibernate.cfg.AnnotationBinder.processElementAnnotations(AnnotationBinder.java:2123)
The reason I'm using java.util.Collection is that I remember when having two or more properties of type java.util.List I was getting the org.hibernate.loader.MultipleBagFetchException: cannot simultaneously fetch multiple bags."

*/

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class CollectionOrderPersistenceTest {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    @Transactional
    @Rollback(false)
    public void collection_order_persistence_test() {

        PrintQueue printQueue = new PrintQueue();
        printQueue.setName("Queue persisting ordered list");

        PrintJob printJobOne = new PrintJob();
        PrintJob printJobTwo = new PrintJob();
        PrintJob printJobThree = new PrintJob();
        PrintJob printJobFour = new PrintJob();

        printQueue.addJob(printJobOne);
        printQueue.addJob(printJobTwo);
        printQueue.addJob(printJobThree);
        printQueue.addJob(printJobFour);

        entityManager.persist(printQueue);
    }


}