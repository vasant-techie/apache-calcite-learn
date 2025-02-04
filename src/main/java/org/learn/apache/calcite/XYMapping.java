package org.learn.apache.calcite;

import jakarta.persistence.*;

@Entity
@Table(name = "XY")
public class XYMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "srcTableName", referencedColumnName = "srcTableName", nullable = false)
    private XEntity xEntity;

    @ManyToOne
    @JoinColumn(name = "tgtTableName", referencedColumnName = "tgtTableName", nullable = false)
    private YEntity yEntity;

    public XYMapping() {}

    public XYMapping(XEntity xEntity, YEntity yEntity) {
        this.xEntity = xEntity;
        this.yEntity = yEntity;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public XEntity getXEntity() { return xEntity; }
    public void setXEntity(XEntity xEntity) { this.xEntity = xEntity; }

    public YEntity getYEntity() { return yEntity; }
    public void setYEntity(YEntity yEntity) { this.yEntity = yEntity; }

    @Override
    public String toString() {
        return "XYMapping{id=" + id + ", srcTableName='" + xEntity.getA() + "', tgtTableName='" + yEntity.getB() + "'}";
    }
}