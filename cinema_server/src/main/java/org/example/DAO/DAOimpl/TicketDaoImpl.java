package org.example.DAO.DAOimpl;

import org.example.DAO.DAO;
import org.example.Model.Entity.Ticket;
import org.example.Model.Entity.User;
import org.example.Utility.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.query.Query;


import java.util.List;

public class TicketDaoImpl implements DAO<Ticket> {
    @Override
    public List<Ticket> findAllEntity() {
        List<Ticket> tickets = (List<Ticket>) HibernateUtil.getSessionFactory().openSession().createQuery("FROM Ticket ORDER BY user.login ").list();
        return tickets;
    }


    public List<Ticket> findTicketsByUser(User user) {
        String hql = "FROM Ticket WHERE user.idUser = :paramName ORDER BY user.login ";
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery(hql);
        query.setParameter("paramName", user.getIdUser());
        List<Ticket> tickets = (List<Ticket>) query.list();
        session.close();
        return tickets;
    }
}
