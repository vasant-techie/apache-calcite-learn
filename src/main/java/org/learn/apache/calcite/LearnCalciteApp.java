package org.learn.apache.calcite;

import org.apache.calcite.sql.*;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.util.SqlVisitor;
import java.util.*;

public class LearnCalciteApp {

    public static void main(String[] args) throws Exception {
        // Sample SQL query
        String sql = "SELECT u.id, u.name, o.amount FROM users u "
                + "JOIN orders o ON u.id = o.user_id WHERE u.age > 25 "
                + "GROUP BY u.name HAVING COUNT(u.id) > 1 ORDER BY o.amount DESC";

        // Parse the SQL query using Apache Calcite 1.38.0
        SqlParser parser = SqlParser.create(sql);
        SqlNode sqlNode = parser.parseQuery();

        // Extract tables and columns
        SqlTableColumnExtractor extractor = new SqlTableColumnExtractor();
        sqlNode.accept(extractor);

        // Print extracted tables and columns
        System.out.println("Tables: " + extractor.getTables());
        System.out.println("Table-wise Columns: " + extractor.getTableColumns());
    }
}

// Custom visitor class to extract tables and columns
class SqlTableColumnExtractor implements SqlVisitor<Void> {
    private final Set<String> tables = new HashSet<>();
    private final Map<String, Set<String>> tableColumns = new HashMap<>();

    public Set<String> getTables() {
        return tables;
    }

    public Map<String, Set<String>> getTableColumns() {
        return tableColumns;
    }

    @Override
    public Void visit(SqlIdentifier id) {
        if (id.names.size() == 2) {  // Format: table.column
            String table = id.names.get(0);
            String column = id.names.get(1);

            tables.add(table);
            tableColumns.computeIfAbsent(table, k -> new HashSet<>()).add(column);
        }
        return null;
    }

    @Override
    public Void visit(SqlCall call) {
        // Handle SELECT queries
        if (call instanceof SqlSelect) {
            SqlSelect select = (SqlSelect) call;
            if (select.getSelectList() != null) select.getSelectList().forEach(node -> node.accept(this));
            if (select.getFrom() != null) select.getFrom().accept(this);
            if (select.getWhere() != null) select.getWhere().accept(this);
            if (select.getGroup() != null) select.getGroup().accept(this);
            if (select.getHaving() != null) select.getHaving().accept(this);
            if (select.getOrderList() != null) select.getOrderList().accept(this);
        }
        // Handle JOIN queries
        else if (call instanceof SqlJoin) {
            SqlJoin join = (SqlJoin) call;
            join.getLeft().accept(this);
            join.getRight().accept(this);
            if (join.getCondition() != null) join.getCondition().accept(this);
        }
        // Handle INSERT queries
        else if (call instanceof SqlInsert) {
            SqlInsert insert = (SqlInsert) call;
            insert.getTargetTable().accept(this);
            if (insert.getSource() != null) insert.getSource().accept(this);
        }
        // Handle DELETE queries
        else if (call instanceof SqlDelete) {
            SqlDelete delete = (SqlDelete) call;
            delete.getTargetTable().accept(this);
            if (delete.getCondition() != null) delete.getCondition().accept(this);
        }
        // Handle UPDATE queries
        else if (call instanceof SqlUpdate) {
            SqlUpdate update = (SqlUpdate) call;
            update.getTargetTable().accept(this);
            if (update.getSourceExpressionList() != null) update.getSourceExpressionList().accept(this);
            if (update.getCondition() != null) update.getCondition().accept(this);
        }
        // Handle MERGE queries
        else if (call instanceof SqlMerge) {
            SqlMerge merge = (SqlMerge) call;
            merge.getTargetTable().accept(this);
            merge.getSourceTableRef().accept(this);
            merge.getCondition().accept(this);
        }
        // Default case: Process other SQL nodes
        else {
            for (SqlNode operand : call.getOperandList()) {
                if (operand != null) operand.accept(this);
            }
        }
        return null;
    }

    @Override
    public Void visit(SqlNodeList nodeList) {
        nodeList.getList().forEach(node -> node.accept(this));
        return null;
    }

    @Override
    public Void visit(SqlLiteral sqlLiteral) {
        return null;
    }

    @Override
    public Void visit(SqlIntervalQualifier sqlIntervalQualifier) {
        return null;
    }

    @Override
    public Void visit(SqlDataTypeSpec sqlDataTypeSpec) {
        return null;
    }

    @Override
    public Void visit(SqlDynamicParam sqlDynamicParam) {
        return null;
    }
}