package com.shopapp.repository.impl;

import com.shopapp.entity.Vaitro;
import com.shopapp.repository.VaitroRepository;
import com.shopapp.util.HibernateUtil;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Hibernate implementation of VaitroRepository.
 * Uses Hibernate for database persistence.
 */
public class VaitroRepositoryImpl implements VaitroRepository {

    @Override
    public Optional<Vaitro> findById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Vaitro vaitro = session.find(Vaitro.class, id);
            return Optional.ofNullable(vaitro);
        }
    }

    @Override
    public List<Vaitro> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Vaitro", Vaitro.class).list();
        }
    }

    @Override
    public Optional<Vaitro> findByTenVaiTro(String tenVaiTro) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Vaitro vaitro = session.createQuery(
                    "from Vaitro where roleName = :tenVaiTro", Vaitro.class)
                    .setParameter("tenVaiTro", tenVaiTro)
                    .uniqueResult();
            return Optional.ofNullable(vaitro);
        }
    }

    @Override
    public Vaitro save(Vaitro vaitro) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Vaitro saved = session.merge(vaitro);
            transaction.commit();
            return saved;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public void deleteById(Integer id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Vaitro vaitro = session.find(Vaitro.class, id);
            if (vaitro != null) {
                session.remove(vaitro);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public boolean existsByTenVaiTro(String tenVaiTro) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery(
                    "select count(v) from Vaitro v where v.roleName = :tenVaiTro", Long.class)
                    .setParameter("tenVaiTro", tenVaiTro)
                    .uniqueResult();
            return count != null && count > 0;
        }
    }
}