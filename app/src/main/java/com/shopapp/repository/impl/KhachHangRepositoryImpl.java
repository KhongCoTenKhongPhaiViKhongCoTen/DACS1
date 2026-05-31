package com.shopapp.repository.impl;

import com.shopapp.entity.KhachHang;
import com.shopapp.repository.KhachHangRepository;
import com.shopapp.util.HibernateUtil;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class KhachHangRepositoryImpl implements KhachHangRepository {

    @Override
    public Optional<KhachHang> findById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            KhachHang khachHang = session.find(KhachHang.class, id);
            return Optional.ofNullable(khachHang);
        }
    }

    @Override
    public List<KhachHang> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from KhachHang", KhachHang.class).list();
        }
    }

    @Override
    public Optional<KhachHang> findByPhone(String phone) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            KhachHang khachHang = session.createQuery(
                    "from KhachHang where phone = :phone", KhachHang.class)
                    .setParameter("phone", phone)
                    .uniqueResult();
            return Optional.ofNullable(khachHang);
        }
    }

    @Override
    public Optional<KhachHang> findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            KhachHang khachHang = session.createQuery(
                    "from KhachHang where email = :email", KhachHang.class)
                    .setParameter("email", email)
                    .uniqueResult();
            return Optional.ofNullable(khachHang);
        }
    }

    @Override
    public KhachHang save(KhachHang khachHang) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            KhachHang saved = session.merge(khachHang);
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
            KhachHang khachHang = session.find(KhachHang.class, id);
            if (khachHang != null) {
                session.remove(khachHang);
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
    public boolean existsByPhone(String phone) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery(
                    "select count(k) from KhachHang k where k.phone = :phone", Long.class)
                    .setParameter("phone", phone)
                    .uniqueResult();
            return count != null && count > 0;
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery(
                    "select count(k) from KhachHang k where k.email = :email", Long.class)
                    .setParameter("email", email)
                    .uniqueResult();
            return count != null && count > 0;
        }
    }
}