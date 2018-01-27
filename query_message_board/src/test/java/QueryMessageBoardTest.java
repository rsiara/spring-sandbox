import configuration.RootConfig;
import model.Conversation;
import model.Message;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/*


 * */

@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class QueryMessageBoardTest {


    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    Date today = new Date();
    Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));
    Date todayplustwo = new Date(today.getTime() + (1000 * 60 * 60 * 24) * 2);
    Date todayplustree = new Date(today.getTime() + (1000 * 60 * 60 * 24) * 3);
    Date todayplusfour = new Date(today.getTime() + (1000 * 60 * 60 * 24) * 4);
    Date todayplusfive = new Date(today.getTime() + (1000 * 60 * 60 * 24) * 5);

    @Before
    public void init() {
        System.out.println("BEFORE TESTS");
    }

    @Test
    @Transactional
    @Rollback(false)
    public void query_message_board_test() {

        prepare_data();

        List<Conversation> conversations = findAllConversations();

        System.out.println("1. Before archive conversations: ");
        for (Conversation conversation : conversations) {
            System.out.println(conversation);
        }

        archiveConversations(todayplustree);

        conversations = findAllConversations();

        System.out.println("2. After archive conversations: ");
        for (Conversation conversation : conversations) {
            System.out.println(conversation);
        }


    }

    public void archiveConversations(Date minAge) {
        List<Conversation> active =
                entityManager.createNamedQuery("findActiveConversations", Conversation.class)
                        .getResultList();
        TypedQuery<Date> maxAge = entityManager.createNamedQuery("findLastMessageDate", Date.class);
        maxAge.setFlushMode(FlushModeType.COMMIT);
        for (Conversation c : active) {
            maxAge.setParameter("conversation", c);
            Date lastMessageDate = maxAge.getSingleResult();
            if (lastMessageDate.before(minAge)) {
                c.setStatus("INACTIVE");
            }
        }
    }


    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Conversation> findAllConversations() {
        return entityManager.createQuery("SELECT c FROM Conversation c", Conversation.class)
                .getResultList();
    }

    private void prepare_data() {

        Conversation conversationOne = new Conversation();
        conversationOne.setStatus(Conversation.ACTIVE);


        Message messageOne = new Message();
        messageOne.setMessage("First Message");
        messageOne.setPostingDate(today);

        Message messageTwo = new Message();
        messageTwo.setMessage("Second Message");
        messageTwo.setPostingDate(tomorrow);

        Message messageTree = new Message();
        messageTree.setMessage("Third Message");
        messageTree.setPostingDate(todayplustwo);

        conversationOne.addMessage(messageOne);
        conversationOne.addMessage(messageTwo);
        conversationOne.addMessage(messageTree);


        Conversation conversationTwo = new Conversation();
        conversationTwo.setStatus(Conversation.ACTIVE);

        Message messageFour = new Message();
        messageFour.setMessage("Four message");
        messageFour.setPostingDate(todayplustree);

        Message messageFive = new Message();
        messageFive.setMessage("Five message");
        messageFive.setPostingDate(todayplusfour);

        Message messageSix = new Message();
        messageSix.setMessage("Six message");
        messageSix.setPostingDate(todayplusfive);

        conversationTwo.addMessage(messageFour);
        conversationTwo.addMessage(messageFive);
        conversationTwo.addMessage(messageSix);

        entityManager.persist(conversationOne);
        entityManager.persist(conversationTwo);
    }

}