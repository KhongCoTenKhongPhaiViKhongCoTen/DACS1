package com.shopapp.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Utility class for Hibernate SessionFactory management.
 * Provides a single SessionFactory instance for the application.
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    /**
     * Builds the SessionFactory from hibernate.cfg.xml configuration.
     *
     * @return SessionFactory instance
     */
    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Log the exception
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Gets the SessionFactory instance.
     *
     * @return SessionFactory instance
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Shuts down the SessionFactory, releasing all resources.
     */
    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}