package org.example.Utility;



import org.example.Model.Entity.Film;
import org.example.Model.Entity.Seance;
import org.example.Model.Entity.Ticket;
import org.example.Model.Entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    private HibernateUtil() {
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                configuration.addAnnotatedClass(User.class);
                configuration.addAnnotatedClass(Film.class);
                configuration.addAnnotatedClass(Seance.class);
                configuration.addAnnotatedClass(Ticket.class);
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
            } catch (Exception e) {
                System.out.println("Исключение в HibernateUtil! " + e);
            }
        }
        return sessionFactory;
    }
}