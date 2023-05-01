package org.example.DAO.DAOimpl;

import org.example.DAO.DAO;
import org.example.Model.Entity.Seance;
import org.example.Model.Entity.User;
import org.example.Utility.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

public class SeanceDaoImpl implements DAO<Seance> {
    @Override
    public List<Seance> findAllEntity() {
        List<Seance> seances = (List<Seance>) HibernateUtil.getSessionFactory().openSession().createQuery("FROM Seance ORDER BY film.name ").list();
        return seances;
    }

    public Seance findEntity(Seance entity) throws NullPointerException {
        Seance seance = null;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Query query = session.createQuery("from Seance where id = :paramName1");
            query.setParameter("paramName1", entity.getId());
            seance = (Seance) query.getSingleResult();
            tx.commit();
            session.close();
        } catch (NoClassDefFoundError | NoResultException e) {
            System.out.println("Exception: " + e);
        }
        if (seance != null)
            return seance;
        else
            throw new NullPointerException();
    }

    ;
}
