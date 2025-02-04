package org.learn.apache.calcite;

import jakarta.persistence.*;

@Entity
@Table(name = "Y")
public class YEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "A", nullable = false)
    private String a;

    @Column(name = "B", nullable = false)
    private String b;

    @ManyToOne
    @JoinColumn(name = "C", referencedColumnName = "ID", nullable = false)
    private XEntity xEntity; // Foreign key reference

    public YEntity() {}

    public YEntity(String a, String b, XEntity xEntity) {
        this.a = a;
        this.b = b;
        this.xEntity = xEntity;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getA() { return a; }
    public void setA(String a) { this.a = a; }

    public String getB() { return b; }
    public void setB(String b) { this.b = b; }

    public XEntity getXEntity() { return xEntity; }
    public void setXEntity(XEntity xEntity) { this.xEntity = xEntity; }

    @Override
    public String toString() {
        return "YEntity{id=" + id + ", A='" + a + "', B='" + b + "', C=" + xEntity.getId() + "}";
    }
}