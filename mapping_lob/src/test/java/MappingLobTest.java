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

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    private byte[] getPicture() throws IOException {
        File fi = new File("myPicture.jpg");
        BufferedImage bufferedImage = ImageIO.read(fi);

        WritableRaster raster = bufferedImage.getRaster();
        DataBufferByte data = (DataBufferByte) raster.getDataBuffer();

        return (data.getData());
    }

    private void savePicture(byte[] byteArrayPicture) throws IOException {
        FileUtils.writeByteArrayToFile(new File("pictureFromDb.jpg"), byteArrayPicture);
    }


}