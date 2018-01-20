package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import static javax.persistence.EnumType.STRING;

@Entity
public class Node {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String user;

    private Date creationDate;

    @ManyToOne
    @JoinTable(name = "TEST_AB",
            joinColumns = {@JoinColumn(name = "NODE_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "TEST_AB", referencedColumnName = "ID")})
    private Node testABparent;

    @OneToMany(mappedBy = "testABparent")
    private Collection<Node> testAbCollection;

    @Enumerated(STRING)
    private NodeType nodeType;

    public void addTestAB(Node testAb) {
        testAb.setTestABparent(this);
        testAbCollection.add(testAb);
    }


    /*    GET / SET*/
    public Node() {
        testAbCollection = new ArrayList<Node>();
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

    public Collection<Node> getTestAbCollection() {
        return testAbCollection;
    }

    public void setTestAbCollection(Collection<Node> testAbCollection) {
        this.testAbCollection = testAbCollection;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public Node getTestABparent() {
        return testABparent;
    }

    public void setTestABparent(Node testABparent) {
        this.testABparent = testABparent;
    }

}
