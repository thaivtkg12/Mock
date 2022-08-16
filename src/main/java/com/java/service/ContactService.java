package com.java.service;

import java.util.List;

import com.java.entity.Contact;

public interface ContactService {
	List<Contact> getContacts();
	Contact addContact(Contact contact);
	Contact getContact(int id);
	void updateContact(Contact contact);
}
