package com.shopapp.repository.impl;

import com.shopapp.entity.DonHang;
import com.shopapp.entity.KhachHang;
import com.shopapp.entity.NguoiDung;
import com.shopapp.entity.Vaitro;
import com.shopapp.repository.DonHangRepository;
import com.shopapp.util.HibernateUtil;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Hibernate implementation of DonHangRepository.
 * Uses Hibernate for database persistence.
 */
public class DonHangRepositoryImpl implements DonHangRepository {

    @Override
    public Optional<DonHang> findById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            DonHang donHang = session.get(DonHang.class, id);
            return Optional.ofNullable(donHang);
        }
    }

    @Override
    public List<DonHang> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from DonHang", DonHang.class).list();
        }
    }

    @Override
    public List<DonHang> findByCustomer(KhachHang customer) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "from DonHang where customer = :customer", DonHang.class)
                    .setParameter("customer", customer)
                    .list();
        }
    }

    @Override
    public List<DonHang> findByUser(NguoiDung user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "from DonHang where nguoiDung = :user", DonHang.class)
                    .setParameter("user", user)
                    .list();
        }
    }

    @Override
    public List<DonHang> findByStatus(String status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "from DonHang where status = :status", DonHang.class)
                    .setParameter("status", status)
                    .list();
        }
    }

    @Override
    public Optional<DonHang> findByOrderNumber(String orderNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            DonHang donHang = session.createQuery(
                    "from DonHang where orderNumber = :orderNumber", DonHang.class)
                    .setParameter("orderNumber", orderNumber)
                    .uniqueResult();
            return Optional.ofNullable(donHang);
        }
    }

    @Override
    public DonHang save(DonHang donHang) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            DonHang saved = session.merge(donHang);
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
            DonHang donHang = session.get(DonHang.class, id);
            if (donHang != null) {
                session.remove(donHang);
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
    public boolean existsByOrderNumber(String orderNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery(
                    "select count(d) from DonHang d where d.orderNumber = :orderNumber", Long.class)
                    .setParameter("orderNumber", orderNumber)
                    .uniqueResult();
            return count != null && count > 0;
        }
    }

    @Override
    public List<DonHang> findByOrderDateBetween(java.time.LocalDateTime start, java.time.LocalDateTime end) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "from DonHang where orderDate between :start and :end", DonHang.class)
                    .setParameter("start", start)
                    .setParameter("end", end)
                    .list();
        }
    }
}