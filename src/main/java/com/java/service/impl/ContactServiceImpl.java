package com.java.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.java.entity.Contact;
import com.java.repository.ContactRepository;
import com.java.service.ContactService;

@Service
public class ContactServiceImpl implements ContactService{

	@Autowired
	ContactRepository contactRepository;
	
	@Override
	public Contact addContact(Contact contact) {
		return contactRepository.save(contact);
	}

	@Override
	public void updateContact(Contact contact) {
		contactRepository.save(contact);
	}

	@Override
	public List<Contact> getContacts() {
		return contactRepository.findAll(Sort.by("status").ascending());
	}

	@Override
	public Contact getContact(int id) {
		return contactRepository.findById(id).get();
	}

}
