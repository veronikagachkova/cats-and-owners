package CatsAndOwners.dao.cat.impl;

import CatsAndOwners.dao.cat.CatDao;
import CatsAndOwners.model.entity.Cat;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CatDaoImpl implements CatDao {
    private final SessionFactory sessionFactory;

    public CatDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Cat cat) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(cat);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public Cat findOne(UUID id) {
        try (Session session = sessionFactory.openSession()) {
            Cat cat = session.get(Cat.class, id);
            if (cat != null) {
                Hibernate.initialize(cat.getOwner());    // если тебе нужен владелец
                Hibernate.initialize(cat.getFriends());  // если нужны друзья
            }
            return cat;
        }
    }

    @Override
    public List<Cat> findFriends(UUID id) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT DISTINCT f FROM Cat c " +
                    "JOIN c.friends f " +
                    "LEFT JOIN FETCH f.owner " +
                    "LEFT JOIN FETCH f.friends " +  // ← ДОБАВЬТЕ ЭТО!
                    "WHERE c.id = :catId";
            return session.createQuery(hql, Cat.class)
                    .setParameter("catId", id)
                    .getResultList();
        }
    }

    @Override
    public List<Cat> findMany() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("SELECT DISTINCT c FROM Cat c LEFT JOIN FETCH c.friends", Cat.class).list();
        }
    }

    @Override
    public List<Cat> findAllByIds(List<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }

        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "SELECT DISTINCT c FROM Cat c LEFT JOIN FETCH c.friends WHERE c.id IN :ids", Cat.class
            ).setParameter("ids", ids).list();
        }
    }

    @Override
    public void update(Cat cat) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(cat);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void delete(UUID id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Cat cat = session.get(Cat.class, id);
            if (cat != null) {
                session.remove(cat);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}