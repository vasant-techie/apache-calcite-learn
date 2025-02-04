package org.learn.apache.calcite;

import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class MainApp {
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        // Insert Data into X
        XEntity x1 = new XEntity("ValueA1", "ValueB1");
        XEntity x2 = new XEntity("ValueA2", "ValueB2");
        session.persist(x1);
        session.persist(x2);

        // Insert Data into Y (Referencing X)
        YEntity y1 = new YEntity("ValueA3", "ValueB3", x1);
        YEntity y2 = new YEntity("ValueA4", "ValueB4", x2);
        session.persist(y1);
        session.persist(y2);

        transaction.commit();

        // Fetch Data from Y
        List<YEntity> yEntities = session.createQuery("from YEntity", YEntity.class).list();
        System.out.println("Data in Y table:");
        yEntities.forEach(System.out::println);

        // Fetch Columns A and B for a given X ID
        Long searchXId = x1.getId();
        List<Object[]> results = session.createQuery("SELECT y.a, y.b FROM YEntity y WHERE y.xEntity.id = :xId", Object[].class)
                .setParameter("xId", searchXId)
                .getResultList();

        System.out.println("Columns A and B for X ID " + searchXId + ":");
        results.forEach(row -> System.out.println("A=" + row[0] + ", B=" + row[1]));

        session.close();
        HibernateUtil.getSessionFactory().close();
    }
}