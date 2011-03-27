package org.mobicents.servlet.sip.weld.examples.modules;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

// Keep the registered users 

@ApplicationScoped
public class RegisteredUsers implements Serializable {

	private static final long serialVersionUID = 1L;

	public RegisteredUsers() {
	}

	@Produces private HashMap<String, String> users = new HashMap<String, String>();

	public HashMap<String, String> getUsers() {
		return users;
	}

	public void setUsers(HashMap<String, String> users) {
		this.users = users;
	} 
	
	public void put(String uri, String address){
		users.put(uri, address);
	}
	
	public void remove(String uri){
		users.remove(uri);
	}
	
	public Set<String> keySet(){
		return users.keySet();
	}

}
