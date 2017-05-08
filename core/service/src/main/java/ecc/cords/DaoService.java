package ecc.cords;

import java.util.List;

public class DaoService{

	private Dao dao = new Dao();

	public <E> void deleteElement(E e) {
		dao.delete(e);
	}

	public <E> List<E> getAllElements(final Class<E> type) {
		return dao.getAll(type);
	}

	public List getOrderedEmployees(String order) {
		return dao.getByCriteria(order, Employee.class);
	}

  	public <E> List<E> getByQuery(String query, final Class<E> type) {
    	return dao.getByQuery(query, type);
    }

	public <E> E getElement(E e) { 
      	return dao.get(e);
    }

    public <E> E getElement(final long id, final Class<E> type) {
      	return dao.get(id,type);
    }

	public <E> void saveElement(E e) {
		dao.save(e);
	}

	public <E> void saveOrUpdateElement(E e) {
		dao.saveOrUpdate(e);
	}

	public <E> void updateElement(E e) {
		dao.saveOrUpdate(e);
	}
}