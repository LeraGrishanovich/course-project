package org.example.DAO.DAOimpl;


import org.example.DAO.DAO;
import org.example.Model.Entity.User;
import org.example.Utility.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

public class UserDaoImpl implements DAO<User> {





    @Override
    public List<User> findAllEntity() {
        List<User> users = (List<User>) HibernateUtil.getSessionFactory().openSession().createQuery("FROM User ORDER BY login ").list();
        return users;
    }

    public boolean existsByLogin(User user){
        User buf = null;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Query query = session.createQuery("from User where login = :paramName");
            query.setParameter("paramName", user.getLogin());
            buf = (User) query.getSingleResult();
            tx.commit();
            session.close();
        } catch (NoClassDefFoundError | NoResultException e) {
            System.out.println("Exception: " + e);
        }
        if(buf != null)
            return true;
        return  false;
    }

    public User existsByLoginAndPassword(User user) {
        User buf = null;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Query query = session.createQuery("from User where login = :paramName1 and password = :paramName2");
            query.setParameter("paramName1", user.getLogin());
            query.setParameter("paramName2", user.getPassword());
            buf = (User) query.getSingleResult();
            tx.commit();
            session.close();
        } catch (NoClassDefFoundError | NoResultException e) {
            System.out.println("Exception: " + e);
        }
        return buf;
    }


    public User findEntity(User entity) throws NullPointerException {
        User user = null;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Query query = session.createQuery("from User where idUser = :paramName1");
            query.setParameter("paramName1", entity.getIdUser());
            user = (User) query.getSingleResult();
            tx.commit();
            session.close();
        } catch (NoClassDefFoundError | NoResultException e) {
            System.out.println("Exception: " + e);
        }
        if(user!= null)
            return user;
        else
            throw new NullPointerException();
    };


}
