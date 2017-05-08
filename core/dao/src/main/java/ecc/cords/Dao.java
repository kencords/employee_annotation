package ecc.cords;

import java.util.Arrays;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

public class Dao{

 	public Session initSession() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		if(!session.getTransaction().isActive()) {
			session.beginTransaction();
		}
		return session;
	}

  	public <T> void delete(final T t) {
	   	Session session = initSession();
	   	session.delete(t);
		session.getTransaction().commit();
	    session.close();
 	}

	public <T> T get(final long id, final Class<T> type) {
		Session session = initSession();
		T t = (T) session.get(type, id);
		session.close();
		return t;
	}

	public <T> T get(T t) {
		Session session = initSession();
		List<T> list = session.createCriteria(t.getClass()).list();
		session.getTransaction().commit();
		session.close();
		return (T) list.get(list.indexOf((T)t));
	}

	public <T> List<T> getAll(final Class<T> type) {
	   	Session session = initSession();
	   	List<T> list = session.createCriteria(type).list();
	   	session.close();
	   	return list;
 	}

 	public <T> List getByCriteria(String order, final Class<T> type) {
  		Session session = initSession();
  		List list = session.createCriteria(type)
  						   .addOrder(Property.forName(order).asc())
  						   .list();
  		session.close();
  		return list;	
  	}

  	public <T> List<T> getByQuery(String query, final Class<T> type) {
	  	Session session = initSession();
	   	List<T> list = session.createQuery(query).list();
	   	session.close();
	   	return list;
  	}

  	public <T> void save(final T t) {
	   	Session session = initSession();
	   	session.save(t);
		session.getTransaction().commit();
	   	session.close();
  	}

	public <T> void saveOrUpdate(final T t) {
	  	Session session = initSession();
	   	session.saveOrUpdate(t);
	   	session.getTransaction().commit();
	   	session.close();
	}
}