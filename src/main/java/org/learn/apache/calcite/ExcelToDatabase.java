package org.learn.apache.calcite;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelToDatabase {

    public static void main(String[] args) {
        String excelFilePath = "tables.xlsx"; // Update the file path

        List<TableColumnMapping> mappings = readExcelFile(excelFilePath);
        insertDataIntoDatabase(mappings);
        fetchColumnsForTable("Employee"); // Example Query
    }

    public static List<TableColumnMapping> readExcelFile(String filePath) {
        List<TableColumnMapping> mappings = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // Assuming first sheet

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                Cell tableNameCell = row.getCell(0);
                Cell columnNameCell = row.getCell(1);

                if (tableNameCell != null && columnNameCell != null) {
                    String tableName = tableNameCell.getStringCellValue().trim();
                    String columnName = columnNameCell.getStringCellValue().trim();
                    mappings.add(new TableColumnMapping(tableName, columnName));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mappings;
    }

    public static void insertDataIntoDatabase(List<TableColumnMapping> mappings) {
        Configuration configuration = new Configuration().configure();
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try {
            for (TableColumnMapping mapping : mappings) {
                session.persist(mapping);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
            sessionFactory.close();
        }
    }

    public static void fetchColumnsForTable(String tableName) {
        Configuration configuration = new Configuration().configure();
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        Session session = sessionFactory.openSession();

        List<String> columnNames = session.createQuery(
                        "SELECT t.columnName FROM TableColumnMapping t WHERE t.tableName = :tableName", String.class)
                .setParameter("tableName", tableName)
                .getResultList();

        System.out.println("Columns for Table '" + tableName + "': " + columnNames);

        session.close();
        sessionFactory.close();
    }
}