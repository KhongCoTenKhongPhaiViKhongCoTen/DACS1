package com.shopapp.repository.impl;

import com.shopapp.entity.SanPham;
import com.shopapp.entity.TonKho;
import com.shopapp.repository.TonKhoRepository;
import com.shopapp.util.HibernateUtil;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Hibernate implementation of TonKhoRepository.
 * Uses Hibernate for database persistence.
 */
public class TonKhoRepositoryImpl implements TonKhoRepository {

    @Override
    public Optional<TonKho> findById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            TonKho tonKho = session.find(TonKho.class, id);
            return Optional.ofNullable(tonKho);
        }
    }

    @Override
    public List<TonKho> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from TonKho", TonKho.class).list();
        }
    }

    @Override
    public TonKho save(TonKho tonKho) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            TonKho saved = session.merge(tonKho);
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
            TonKho tonKho = session.find(TonKho.class, id);
            if (tonKho != null) {
                session.remove(tonKho);
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
    public Optional<TonKho> findByProduct(SanPham product) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            TonKho tonKho = session.createQuery(
                    "from TonKho where product = :product", TonKho.class)
                    .setParameter("product", product)
                    .uniqueResult();
            return Optional.ofNullable(tonKho);
        }
    }
}