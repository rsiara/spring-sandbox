import configuration.RootConfig;
import model.Employee;
import org.apache.commons.io.FileUtils;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/*
 * Domyslny poziom dostepu to AccessType
 * */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class MappingLobTest {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    @Transactional
    @Rollback(false)
    public void access_field_test() throws IOException {
        System.out.println(System.getProperty("user.dir"));
        Employee employee = new Employee();
        employee.setName("John");
        employee.setSalary(3400);
        employee.setPicture(getPicture());

        entityManager.persist(employee);
        entityManager.flush();
        entityManager.clear();
        entityManager.close();

        //Get object from database
        Employee employeeLoadedFromDb = entityManager.find(Employee.class, employee.getId());
        savePicture(employeeLoadedFromDb.getPicture());

    }

    private byte[] getPicture() {
        File file = new File("Untitled.bmp");
        byte[] bFile = new byte[(int) file.length()];

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bFile;
    }

    private void savePicture(byte[] byteArrayPicture) throws IOException {
        FileUtils.writeByteArrayToFile(new File("pictureFromDb.bmp"), byteArrayPicture);
    }


}