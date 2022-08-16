package com.java.utils;

import com.java.dto.ContactDTO;
import com.java.entity.Contact;

public class Process {
	public static boolean checkPageParameter(String pageParameter) {
        boolean check = true;
       try {
            if (Integer.parseInt(pageParameter) <= 0) {
                check = false;
            }
        } catch (Exception e) {
            check = false;
        }
       return check;
    }
	
	public static Contact convertContactDTOToContact(ContactDTO contactDTO) {
		Contact contact = new Contact();
		
		contact.setCustomerName(contactDTO.getCustomerName());
		contact.setEmail(contactDTO.getEmail());
		contact.setMessage(contactDTO.getMessage());
		contact.setSubject(contactDTO.getSubject());
		
		return contact;
	}
	
	public static boolean checkInteger(String parameter) {
		
		boolean check = true;
		
		try {
			int i = Integer.parseInt(parameter);
			if (i < 1) {
				check = false;
			}
		} catch (Exception e) {
			check = false;
		}
		
		return check;
	}
}
