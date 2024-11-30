package dao;

import datasets.UData;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class UserSessions {
    private Session session;

    public UserSessions(Session session) {
        this.session = session;
    }

    public long insertUser(UData user)
            throws HibernateException {
        return (Long) session.save(user);
    }
    public UData getUserDataSetByUserId(long userId) {
        Criteria criteria = session.createCriteria(UData.class);
        return (UData) criteria.add(Restrictions.eq("id", userId)).uniqueResult();
    }
    public UData getUserDataSetByLogin(String login)
            throws HibernateException {
        Criteria criteria = session.createCriteria(UData.class);
        return (UData) criteria.add(Restrictions.eq("login", login)).uniqueResult();
    }
}
