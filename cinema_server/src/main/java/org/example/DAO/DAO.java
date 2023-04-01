package org.example.DAO;

import org.example.Utility.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import java.util.List;

public interface DAO<T> {
    default boolean saveEntity(T entity){
        boolean isUpdated = false;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            session.save(entity);
            tx.commit();
            session.close();
            isUpdated = true;
        } catch (NoClassDefFoundError | NoResultException e) {
            System.out.println("Exception: " + e);
        }
        return isUpdated;
    };

    default boolean updateEntity(T entity){
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            session.update(entity);
            tx.commit();
            session.close();
            return true;
        } catch (NoClassDefFoundError | NoResultException e) {
            System.out.println("Exception: " + e);
        }
        catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        return false;
    };

    default boolean deleteEntity(T entity){
        boolean isUpdated = false;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            session.delete(entity);
            tx.commit();
            session.close();
            isUpdated = true;
        } catch (NoClassDefFoundError | NoResultException e) {
            System.out.println("Exception: " + e);
        }
        return isUpdated;
    };



    public List<T> findAllEntity();
}
