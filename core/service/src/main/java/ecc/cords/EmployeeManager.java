package ecc.cords;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EmployeeManager{

	private static DaoService daoService = new DaoService();
	private static String logMsg = "";

	public static String addEmployee(Name name, Date birthdate, float gwa, Address address, Set<Contact> contacts, boolean curHired, Date hiredate, Set<Role> roles){
		Employee employee;
		try {
			employee = new Employee(name, birthdate, hiredate, gwa, curHired, address, contacts, roles);
			contacts.forEach(contact -> contact.setEmployee(employee));
			daoService.saveElement(employee);
		} catch(Exception exception) {
			exception.printStackTrace();
			return "Employee Creation Failed!";
		}
		return "Employee Creation Successful!";
	}

	public static Address createAddress(int strNo,String street, String brgy, String city, String zipcode){
		return new Address(strNo, street, brgy, city, zipcode);
	}

	public static Contact createContact(String contactType, String contactValue) {
		return new Contact(contactType, contactValue);
	}

	public static Name createName(String lname, String fname, String mname, String suffix, String title) {
		return new Name(lname, fname, mname, suffix, title);
	}

	public static Employee addContact(Employee employee, Set<Contact> contacts) {
		contacts.forEach(contact -> { 
			contact.setEmployee(employee);
			employee.getContacts().add(contact);
		});
		return employee;
	}

	public static void updateContact(Employee employee, Contact contact, String contactValue) {
		employee.getContacts().remove(contact);
		contact.setContactValue(contactValue);
		employee.getContacts().add(contact);
		daoService.updateElement(contact);
	}

	public static void deleteContact(Employee employee, Contact contact) throws Exception {
		if(employee.getContacts().size()==1) {
			logMsg = "Employee must have atleast one contact!";
			throw new Exception();																				
		}
		employee.getContacts().remove(contact);
	}
														
	public static Contact getContact(Long emp_id, int contact_id) throws Exception {
		Contact contact = new Contact();
		try {
			contact = daoService.getElement(Long.valueOf(contact_id), Contact.class);
			contact.getContactType();
		} catch(Exception exception) {
			logMsg = "Contact does not exist!";
			throw exception;
		}
		if(contact.getEmployee().getEmpId()!= Long.valueOf(emp_id)) {
			logMsg = "Contact does not belong to Employee " + emp_id + "!";
			throw new Exception();
		} 
		return contact;
	}

	public static Employee getEmployee(int id) throws Exception {
		Employee employee = new Employee();
		try {
			employee = daoService.getElement(Long.valueOf(id), Employee.class);
			employee.getName().getLastName();
			return employee;
		} catch(Exception exception) {
			logMsg = "Employee not found!";
			throw exception;
		}
	}

	public static Employee addEmployeeRole(Employee employee, int role_id) throws Exception{
		try {
			Set<Role> roles = employee.getRoles();
			Role role = daoService.getElement(Long.valueOf(role_id), Role.class);
			roles.add(role);
			employee.setRoles(roles);
			return employee;
		} catch(Exception exception) {
			logMsg = "Invalid role!";
			throw new Exception("",exception);
		}
	}

	public static Employee deleteEmployeeRole(Employee employee, int role_id) {
		Set<Role> roles = employee.getRoles();
		Role role = daoService.getElement(Long.valueOf(role_id), Role.class);
		roles.remove(role);
		employee.setRoles(roles);
		return employee;
	}

	public static String createRole(String role_str) {
		Role role = new Role(role_str);
		try {
			daoService.getElement(role);
			return "Role " + role_str + " already exists!";
		} catch(Exception exception) {
			daoService.saveElement(role);
			return "Successfully created " + role_str + " Role!";
		}
	}

	public static String deleteRole(Role role) throws Exception {
		try {
			daoService.deleteElement(role);
			return "Successfully deleted role " + role.getRoleName() + "!";
		} catch(Exception e) {
			logMsg = "Role " + role.getRoleName() + " cannot be deleted!";
			throw new Exception();
		}
	}

	public static String updateRole(Role role, String role_name) throws Exception {
		String prev_name = role.getRoleName();
		if(role.getEmployees().size()==0) {
			role.setRoleName(role_name);
			try {
				Role newRole = daoService.getElement(role);
				return "Role " + role_name + " already exists!"; 
			} catch(Exception exception) {
				daoService.updateElement(role);
				return "Successfully updated role " + prev_name + " to " + role.getRoleName() + "!";
			}
		}
		logMsg = "Role " + role.getRoleName() + " cannot be updated!";
		throw new Exception();
	}

	public static Role getRole(int role_id) throws Exception {
		Role role = new Role();
		try {
			role = daoService.getElement(Long.valueOf(role_id), Role.class);
			role.getRoleName();
		} catch(Exception exception) {
			logMsg = "Role does not exist!";
			throw exception;
		}
		return role;
	}

	public static String getLogMsg() {
		return logMsg;
	}

	private static Role addRole(Long roleId) {
		return daoService.getElement(roleId, Role.class);
	}
}