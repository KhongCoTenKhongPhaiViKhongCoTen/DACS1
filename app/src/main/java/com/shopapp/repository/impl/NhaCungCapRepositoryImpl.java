package com.shopapp.repository.impl;

import com.shopapp.entity.NhaCungCap;
import com.shopapp.repository.NhaCungCapRepository;
import com.shopapp.util.HibernateUtil;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class NhaCungCapRepositoryImpl implements NhaCungCapRepository {

    @Override
    public Optional<NhaCungCap> findById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            NhaCungCap nhaCungCap = session.find(NhaCungCap.class, id);
            return Optional.ofNullable(nhaCungCap);
        }
    }

    @Override
    public List<NhaCungCap> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from NhaCungCap", NhaCungCap.class).list();
        }
    }

    @Override
    public NhaCungCap save(NhaCungCap nhaCungCap) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            NhaCungCap saved = session.merge(nhaCungCap);
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
            NhaCungCap nhaCungCap = session.find(NhaCungCap.class, id);
            if (nhaCungCap != null) {
                session.remove(nhaCungCap);
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
    public boolean existsByCompanyName(String companyName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery(
                    "select count(n) from NhaCungCap n where n.companyName = :companyName", Long.class)
                    .setParameter("companyName", companyName)
                    .uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}