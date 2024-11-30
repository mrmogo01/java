package service;

import dao.UserSessions;
import datasets.UData;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.HashMap;
import java.util.Map;

public class AccService {
    private static final String hibernate_show_sql = "true";
    private static final String hibernate_hbm2ddl_auto = "update";
    private static final String hibernate_username = "test";
    private static final String hibernate_password = "test";

    private final SessionFactory sessionFactory;
    private final Map<String, Long> sessionIdToProfile;

    public AccService() {
        Configuration configuration = getH2Configuration();
        sessionFactory = createSessionFactory(configuration);

        sessionIdToProfile = new HashMap<>();
    }

    public long addNewUser(UData user) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        UserSessions dao = new UserSessions(session);
        long id = dao.insertUser(user);
        transaction.commit();
        session.close();
        return id;
    }

    public UData getUserBySessionId(String sessionId) {
        final Long userId = sessionIdToProfile.get(sessionId);
        if(userId == null){
            return null;
        }
        Session session = sessionFactory.openSession();
        UserSessions dao = new UserSessions(session);
        UData ds = dao.getUserDataSetByUserId(userId);
        session.close();
        return ds;
    }

    public UData getUserByLogin(String login) {
        Session session = sessionFactory.openSession();
        UserSessions dao = new UserSessions(session);
        UData ds = dao.getUserDataSetByLogin(login);
        session.close();
        return ds;
    }

    public void addSession(long userId, String sessionId) {
        sessionIdToProfile.put(sessionId, userId);
    }

    private Configuration getH2Configuration() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(UData.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:./h2db");
        configuration.setProperty("hibernate.connection.username", hibernate_username);
        configuration.setProperty("hibernate.connection.password", hibernate_password);
        configuration.setProperty("hibernate.show_sql", hibernate_show_sql);
        configuration.setProperty("hibernate.hbm2ddl.auto", hibernate_hbm2ddl_auto);
        return configuration;
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }
}
