package org.example.DAO.DAOimpl;

import org.example.DAO.DAO;
import org.example.Model.Entity.Film;
import org.example.Model.Entity.Seance;
import org.example.Model.Entity.User;
import org.example.Utility.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

public class FilmDaoImpl implements DAO<Film> {
    @Override
    public List<Film> findAllEntity() {
        List<Film> films = (List<Film>) HibernateUtil.getSessionFactory().openSession().createQuery("FROM Film ORDER BY name ").list();
        return films;
    }

    public Film findEntity(Film entity) throws NullPointerException {
        Film film = null;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Query query = session.createQuery("from Film where id = :paramName1");
            query.setParameter("paramName1", entity.getId());
            film = (Film) query.getSingleResult();
            tx.commit();
            session.close();
        } catch (NoClassDefFoundError | NoResultException e) {
            System.out.println("Exception: " + e);
        }
        if (film != null)
            return film;
        else
            throw new NullPointerException();
    }
}
