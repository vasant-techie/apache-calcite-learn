package org.learn.apache.calcite;

import jakarta.persistence.*;

@Entity
@Table(name = "X")
public class XEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "A", nullable = false)
    private String a;

    @Column(name = "B", nullable = false)
    private String b;

    public XEntity() {}

    public XEntity(String a, String b) {
        this.a = a;
        this.b = b;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getA() { return a; }
    public void setA(String a) { this.a = a; }

    public String getB() { return b; }
    public void setB(String b) { this.b = b; }

    @Override
    public String toString() {
        return "XEntity{id=" + id + ", A='" + a + "', B='" + b + "'}";
    }
}