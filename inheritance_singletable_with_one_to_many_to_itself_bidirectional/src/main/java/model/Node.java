package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static javax.persistence.EnumType.STRING;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "NODE_TYPE")
@DiscriminatorValue(value = "PAGE")
public class Node {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    public Node() {
        testAbCollection = new ArrayList<TestAB>();
    }

    private String user;

    private Date creationDate;

    @ManyToOne
    @JoinTable(name = "TEST_AB",
            joinColumns = {@JoinColumn(name = "NODE_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "TEST_AB", referencedColumnName = "ID")})
    private Node parent;

    @OneToMany(mappedBy = "parent", targetEntity = Node.class)
    private Collection<TestAB> testAbCollection;

    @Enumerated(STRING)
    @Column(name = "NODE_TYPE", insertable = false, updatable = false, nullable = false)
    private NodeType nodeType;

    public void addTestAB(TestAB testAb) {
        testAb.setParent(this);
        testAbCollection.add(testAb);
    }

    /*    GET / SET*/

    public Collection<TestAB> getTestAbCollection() {
        return testAbCollection;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setTestAbCollection(Collection<TestAB> testAbCollection) {
        this.testAbCollection = testAbCollection;
    }
}
