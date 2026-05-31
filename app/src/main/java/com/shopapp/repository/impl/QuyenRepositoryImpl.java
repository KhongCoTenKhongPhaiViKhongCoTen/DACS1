package com.shopapp.repository.impl;

import com.shopapp.entity.Quyen;
import com.shopapp.repository.QuyenRepository;
import com.shopapp.util.HibernateUtil;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class QuyenRepositoryImpl implements QuyenRepository {

    @Override
    public Optional<Quyen> findById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.find(Quyen.class, id));
        }
    }

    @Override
    public List<Quyen> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Quyen", Quyen.class).list();
        }
    }

    @Override
    public Optional<Quyen> findByPermissionCode(String permissionCode) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Quyen quyen = session.createQuery(
                    "from Quyen where permissionCode = :code", Quyen.class)
                    .setParameter("code", permissionCode)
                    .uniqueResult();
            return Optional.ofNullable(quyen);
        }
    }

    @Override
    public List<Quyen> findByModule(String module) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "from Quyen where module = :module", Quyen.class)
                    .setParameter("module", module)
                    .list();
        }
    }

    @Override
    public Quyen save(Quyen quyen) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Quyen saved = session.merge(quyen);
            transaction.commit();
            return saved;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    @Override
    public void deleteById(Integer id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Quyen quyen = session.find(Quyen.class, id);
            if (quyen != null) {
                session.remove(quyen);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    @Override
    public boolean existsByPermissionCode(String permissionCode) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery(
                    "select count(q) from Quyen q where q.permissionCode = :code", Long.class)
                    .setParameter("code", permissionCode)
                    .uniqueResult();
            return count != null && count > 0;
        }
    }
}
