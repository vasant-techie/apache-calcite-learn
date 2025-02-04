package org.learn.apache.calcite;
import jakarta.persistence.*;

@Entity
@Table(name = "table_column_mapping")
public class TableColumnMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_name", nullable = false)
    private String tableName;

    @Column(name = "column_name", nullable = false)
    private String columnName;

    public TableColumnMapping() {}

    public TableColumnMapping(String tableName, String columnName) {
        this.tableName = tableName;
        this.columnName = columnName;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }

    public String getColumnName() { return columnName; }
    public void setColumnName(String columnName) { this.columnName = columnName; }

    @Override
    public String toString() {
        return "TableColumnMapping{tableName='" + tableName + "', columnName='" + columnName + "'}";
    }
}