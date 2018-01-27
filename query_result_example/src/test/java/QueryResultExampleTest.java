import configuration.RootConfig;
import model.Department;
import model.Employee;
import model.Project;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.*;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/*
Typy transakcji
Za pomocą adnotacji TransactionAttributeType można zdefiniować rodzaj transakcji, jaka ma obowiązywać w metodach. Występują następujące typy transakcji:

	* MANDATORY
Konieczność wykonania metody w ramach istniejącej już transakcji. Jeżeli dla danego klienta istnieje już działająca transakcji,
 dana metoda zostanie w jej ramach wykonana. Jeżeli natomiast taka transakcji nie istnieje, to zostanie wyrzucony wyjątek TransactionRequiredException.
 Należy użyć tego atrybutu używać wtedy, gdy dana metoda musi zostać wykonana w ramach transakcji klienta.

	* REQUIRED
Jeżeli klient działa w ramach transakcji i zostanie wywołana metoda oznaczona tym typem, to zostanie ona wykonana w ramach tej transakcji.
 Jeżeli natomiast klient nie działa w ramach transakcji, to zostanie rozpoczęta nowa, zanim metoda zostanie uruchomiona.
 Jest to także domyślne zachowanie, jeżeli nie zostanie ustawiony typ transakcji.

	* REQUIRES_NEW
Jeżeli klient nie działa w ramach transakcji, to po prostu jest rozpoczynana nowa przed uruchomieniem metody.
Natomiast jeżeli jest istnieje już działająca transakcja, to podejmowane są następujące kroki:

		1. Zawieszenie aktualnie działającej transakcji.
		2. Rozpoczęcie nowej transakcji do wykonania wybranej metody.
		3. Wywołanie i wykonanie metody.
		4. Po zakończeniu działania metody ponowne uruchomienie wcześniej zawieszonej transakcji.
Typu tego należy używać w sytuacji, gdy istnieje potrzeba wykonywania danej metody zawsze w nowej transakcji.

	* SUPPORTS
Jeżeli klient działa w ramach transakcji i wywoła daną metodę, to zostanie ona wykonana w ramach tej transakcji.
Jeżeli natomiast transakcja nie istnieje, to nie zostanie ona także rozpoczęta przed wykonaniem wybranej metody.

	* NOT_SUPPORTED
Jeżeli klient działa w ramach transakcji, to zostanie ona zatrzymana na czas wywołania wybranej metody, a po jej wykonaniu ponownie przywrócona.
 Jeżeli nie ma takiej transakcji, metoda zostanie po prostu wykonana, bez rozpoczynania nowej.
Należy używać tego typu wtedy, gdy wybrana metoda nie potrzebuje transakcji do działania,
 a zależy nam na większej wydajności jej wykonywania (transakcje mają negatywny wpływ na szybkość wykonywania metod).

	* NEVER
Jeżeli klient działa w ramach transakcji i wywoła metodą oznaczoną tym typem, zostanie wyrzucony wyjątek RemoteException. Natomiast jeżeli transakcja nie istnieje, to metoda zostanie wykonana bez rozpoczynania nowej transakcji.



 * */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class QueryResultExampleTest {


    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Test
    @Transactional
    @Rollback(false)
    public void query_result_example_test() {

        prepare_data();
        List<Department> allDepartmentsDetached = findAllDepartmentsDetached();

        for (Department department : allDepartmentsDetached) {
            System.out.println("1. Department detached: " + department);
            //System.out.println("1. Department detached is not detached ?: " + entityManager.contains(department));
        }

        List projectEmployees = findProjectEmployees("Hospital project");
        int count = 0;
        for (Iterator i = projectEmployees.iterator(); i.hasNext(); ) {
            Object[] values = (Object[]) i.next();
            System.out.println("2. Project employee :" + ++count + ": " + values[0] + ", " + values[1]);
        }

        List<EmpMenu> projectEmployeesWithConstructor = findProjectEmployeesWithConstructor("Hospital project");
        for (EmpMenu empMenu : projectEmployeesWithConstructor) {
            System.out.println("3. Project empMenu with constructor: empMenu.departmentName: " + empMenu.getDepartmentName()
                    + "empMenu.employeeName" + empMenu.getEmployeeName());
        }
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Department> findAllDepartmentsDetached() {
        return entityManager.createQuery("SELECT d FROM Department d", Department.class)
                .getResultList();
    }

    public List findProjectEmployees(String projectName) {
        return entityManager.createQuery("SELECT e.name, e.department.name " +
                "FROM Project p JOIN p.employees e " +
                "WHERE p.name = :project " +
                "ORDER BY e.name")
                .setParameter("project", projectName)
                .getResultList();
    }

    public List<EmpMenu> findProjectEmployeesWithConstructor(String projectName) {
        return entityManager.createQuery("SELECT NEW EmpMenu(e.name, e.department.name) " +
                "FROM Project p JOIN p.employees e " +
                "WHERE p.name = :project " +
                "ORDER BY e.name", EmpMenu.class)
                .setParameter("project", projectName)
                .getResultList();
    }

    public List<Employee> findAllEmployees() {
        return entityManager.createQuery("SELECT e FROM Employee e", Employee.class)
                .getResultList();
    }

    public List<Project> findAllProjects() {
        return entityManager.createQuery("SELECT p FROM Project p", Project.class)
                .getResultList();
    }


    private void prepare_data() {
        Department department = new Department();
        department.setName("Humar Resources");

        Employee john = new Employee();
        john.setName("John");
        john.setSalary(2800);
        john.setStartDate(new Date());

        Employee mark = new Employee();
        mark.setName("Mark");
        mark.setSalary(4200);
        mark.setStartDate(new Date());

        Collection<Employee> employess = new ArrayList<Employee>();
        employess.add(john);
        employess.add(mark);

        john.setDirects(employess);
        mark.setManager(john);
        john.setManager(john);

        Project hrDepartmentProject = new Project();
        hrDepartmentProject.setName("HR Headquarter development");

        hrDepartmentProject.setEmployees(employess);


        Project hospitalProject = new Project();
        hospitalProject.setName("Hospital project");

        hospitalProject.setEmployees(employess);

        Project schoolProject = new Project();
        schoolProject.setName("School project");

        schoolProject.setEmployees(employess);


        Collection<Project> projects = new ArrayList<Project>();
        projects.add(hrDepartmentProject);
        projects.add(hospitalProject);
        projects.add(schoolProject);

        john.setProjects(projects);
        mark.setProjects(projects);

        department.addEmployee(john);
        department.addEmployee(mark);

        entityManager.persist(department);

        entityManager.persist(hrDepartmentProject);
        entityManager.persist(hospitalProject);
        entityManager.persist(schoolProject);
    }

}