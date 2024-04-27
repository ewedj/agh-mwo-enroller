package com.company.enroller.persistence;

import java.util.Collection;
import java.util.stream.Collectors;

import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Participant;

@Component("participantService")
public class ParticipantService {

	DatabaseConnector connector;

	public ParticipantService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Participant> getAll() {
		String hql = "FROM Participant";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

	public Participant findByLogin(String login) {
		return connector.getSession().get(Participant.class, login);
	}

	public void add(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(participant);
		transaction.commit();
	}

	public void update(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(participant);
		transaction.commit();
	}

	public void remove(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().remove(participant);
		transaction.commit();
	}

	public Collection<Participant> getFilteredParticipants(String key, String sortBy, String sortOrder) {
		String hql = "FROM Participant";
		if (key != null) {
			hql += " WHERE login LIKE '%" + key + "%'";
		}
		if (sortBy != null) {
			hql += " ORDER BY " + sortBy;
			if (sortOrder == null) {
				hql += " ASC";
			} else {
				hql += " " + sortOrder;
			}
		}
		Query query = connector.getSession().createQuery(hql);

		return query.list();
	}


}

	//	------
//	public Collection<Participant> getAll(String key, String sortBy, String sortOrder) {
//		String hql = "FROM Participant WHERE login LIKE :login";
//		if (sortBy.equals("login")) {
//			hql += " ORDER by " + sortBy;
//			if (sortOrder.equals("ASC") || sortOrder.equals("DESC")) {
//				hql += " " + sortOrder;
//			}
//		}
//		Query query = connector.getSession().createQuery(hql);
//		query.setParameter("login", "%" + key + "%");
//		return query.list();
//	}

