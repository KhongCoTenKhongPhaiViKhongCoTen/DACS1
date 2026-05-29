package com.shopapp.repository.impl;

import com.shopapp.entity.DanhMuc;
import com.shopapp.entity.NhaCungCap;
import com.shopapp.entity.SanPham;
import com.shopapp.repository.SanPhamRepository;
import com.shopapp.util.HibernateUtil;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Hibernate implementation of SanPhamRepository.
 * Uses Hibernate for database persistence.
 */
public class SanPhamRepositoryImpl implements SanPhamRepository {

    @Override
    public Optional<SanPham> findById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            SanPham sanPham = session.get(SanPham.class, id);
            return Optional.ofNullable(sanPham);
        }
    }

    @Override
    public List<SanPham> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from SanPham", SanPham.class).list();
        }
    }

    @Override
    public List<SanPham> findByCategory(DanhMuc category) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "from SanPham where category = :category", SanPham.class)
                    .setParameter("category", category)
                    .list();
        }
    }

    @Override
    public List<SanPham> findBySupplier(NhaCungCap supplier) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "from SanPham where supplier = :supplier", SanPham.class)
                    .setParameter("supplier", supplier)
                    .list();
        }
    }

    @Override
    public Optional<SanPham> findBySku(String sku) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            SanPham sanPham = session.createQuery(
                    "from SanPham where sku = :sku", SanPham.class)
                    .setParameter("sku", sku)
                    .uniqueResult();
            return Optional.ofNullable(sanPham);
        }
    }

    @Override
    public SanPham save(SanPham sanPham) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            SanPham saved = session.merge(sanPham);
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
            SanPham sanPham = session.get(SanPham.class, id);
            if (sanPham != null) {
                session.remove(sanPham);
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
    public boolean existsBySku(String sku) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery(
                    "select count(s) from SanPham s where s.sku = :sku", Long.class)
                    .setParameter("sku", sku)
                    .uniqueResult();
            return count != null && count > 0;
        }
    }

    @Override
    public List<SanPham> findByProductNameContaining(String productName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "from SanPham where productName like :productName", SanPham.class)
                    .setParameter("productName", "%" + productName + "%")
                    .list();
        }
    }
}