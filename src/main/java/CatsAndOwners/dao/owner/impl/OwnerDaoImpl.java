package CatsAndOwners.dao.owner.impl;

import CatsAndOwners.dao.owner.OwnerDao;
import CatsAndOwners.model.entity.Owner;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.UUID;

public class OwnerDaoImpl implements OwnerDao {
    private final SessionFactory sessionFactory;

    public OwnerDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Owner owner) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(owner);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public Owner findOne(UUID id) {
        try (Session session = sessionFactory.openSession()) {
            Owner owner = session.get(Owner.class, id);
            if (owner != null) {
                Hibernate.initialize(owner.getCats());
            }
            return owner;
        }
    }

    @Override
    public List<Owner> findMany() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("SELECT DISTINCT o FROM Owner o LEFT JOIN FETCH o.cats", Owner.class).list();
        }
    }

    @Override
    public void update(Owner owner) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(owner);
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
            Owner owner = session.get(Owner.class, id);
            if (owner != null) {
                session.remove(owner);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}