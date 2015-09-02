package app.main;

import app.dao.model.Employee;
import app.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.stat.Statistics;

import java.util.Map;

/**
 * @author abogaichuk
 */
public class HibernateEHCacheMain {
    public static void main(String[] args) throws InterruptedException {

        System.out.println("Temp Dir:"+System.getProperty("java.io.tmpdir"));

        //Initialize Sessions
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Statistics stats = sessionFactory.getStatistics();
        System.out.println("Stats enabled="+stats.isStatisticsEnabled());
        stats.setStatisticsEnabled(true);
        System.out.println("Stats enabled="+stats.isStatisticsEnabled());

        Session session = sessionFactory.openSession();
        Session otherSession = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Transaction otherTransaction = otherSession.beginTransaction();

        printStats(stats, 0);

        Employee emp = (Employee) session.load(Employee.class, 1L);
        printData(emp, stats, 1);

        emp = (Employee) session.load(Employee.class, 1L);
        printData(emp, stats, 2);

        //clear first level cache, so that second level cache is used
        session.evict(emp);
        emp = (Employee) session.load(Employee.class, 1L);
        printData(emp, stats, 3);

        emp = (Employee) session.load(Employee.class, 3L);
        printData(emp, stats, 4);

        emp = (Employee) otherSession.load(Employee.class, 1L);
        printData(emp, stats, 5);

        for (long i = 1; i <= 8; i++) {
            int count = (int) i;
            emp = (Employee) otherSession.load(Employee.class, i);
            printData(emp, stats, 5+count);
        }

        //Release resources
        transaction.commit();
        otherTransaction.commit();

        Map cacheEntries = sessionFactory.getStatistics()
                .getSecondLevelCacheStatistics("employee")
                .getEntries();
        for (Object entry : cacheEntries.entrySet())
        {
            //System.out.println( entry.getKey() + "/" + entry.getValue());
            System.out.println( entry.toString());
        }

        //Thread.sleep(100000);

        sessionFactory.close();
    }

    private static void printStats(Statistics stats, int i) {
        System.out.println("***** " + i + " *****");
        System.out.println("Fetch Count="
                + stats.getEntityFetchCount());
        System.out.println("Second Level Hit Count="
                + stats.getSecondLevelCacheHitCount());
        System.out
                .println("Second Level Miss Count="
                        + stats
                        .getSecondLevelCacheMissCount());
        System.out.println("Second Level Put Count="
                + stats.getSecondLevelCachePutCount());
    }

    private static void printData(Employee emp, Statistics stats, int count) {
        System.out.println(count+":: Name="+emp.getName()+", Zipcode="+emp.getAddress().getZipcode());
        printStats(stats, count);
    }
}
