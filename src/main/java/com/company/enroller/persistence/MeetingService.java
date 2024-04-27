package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;

@Component("meetingService")
public class MeetingService {

	DatabaseConnector connector;

	public MeetingService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Meeting> getAll() {
		String hql = "FROM Meeting";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

	public Meeting findByID(long id) {
		return connector.getSession().get(Meeting.class, id);
	}

	public void add(Meeting meeting) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(meeting);
		transaction.commit();
	}

	public void update(Meeting meeting) {
		Transaction transaction = connector.getSession().getTransaction();
		connector.getSession().save(meeting);
		transaction.commit();
	}

	public void remove(Meeting meeting) {
		Transaction transaction = connector.getSession().getTransaction();
		connector.getSession().remove(meeting);
		transaction.commit();
	}

//	public Collection<Meeting> getFilteredMeeting(String key, String sortBy, String sortOrder) {
//
//	}
}
