package com.shopapp.repository.impl;

import com.shopapp.entity.DanhMuc;
import com.shopapp.repository.DanhMucRepository;
import com.shopapp.util.HibernateUtil;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Hibernate implementation of DanhMucRepository.
 * Uses Hibernate for database persistence.
 */
public class DanhMucRepositoryImpl implements DanhMucRepository {

    @Override
    public Optional<DanhMuc> findById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            DanhMuc danhMuc = session.find(DanhMuc.class, id);
            return Optional.ofNullable(danhMuc);
        }
    }

    @Override
    public List<DanhMuc> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from DanhMuc", DanhMuc.class).list();
        }
    }

    @Override
    public DanhMuc save(DanhMuc danhMuc) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            DanhMuc saved = session.merge(danhMuc);
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
            DanhMuc danhMuc = session.find(DanhMuc.class, id);
            if (danhMuc != null) {
                session.remove(danhMuc);
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
    public boolean existsByCategoryName(String categoryName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery(
                    "select count(d) from DanhMuc d where d.categoryName = :categoryName", Long.class)
                    .setParameter("categoryName", categoryName)
                    .uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}