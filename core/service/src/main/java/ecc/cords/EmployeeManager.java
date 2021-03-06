package ecc.cords;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EmployeeManager{

	private static DaoService daoService = new DaoService();
	private static DTO_EntityMapper mapper = new DTO_EntityMapper();
	private static String logMsg = "";

	public static String addEmployee(EmployeeDTO employeeDTO) {
		Employee employee = mapper.mapToEmployee(employeeDTO, true);
		try {
			daoService.saveElement(employee);
		} catch(Exception exception) {
			exception.printStackTrace();
			return "Employee Creation Failed!";
		}
		return "Employee Creation Successful!";
	}

	public static Name createName(String lname, String fname, String mname, String suffix, String title) {
		return new Name(lname, fname, mname, suffix, title);
	}

	public static EmployeeDTO addContact(EmployeeDTO employee, Set<ContactDTO> contacts) {
		contacts.forEach(contact -> { 
			contact.setEmployee(employee);
			employee.getContacts().add(contact);
		});
		return employee;
	}

	public static void updateContact(EmployeeDTO employee, Set<ContactDTO> contacts) {
		employee.getContacts().clear();
		employee.setContacts(contacts);
	}

	public static void deleteContact(EmployeeDTO employee, ContactDTO contact) throws Exception {
		Set<ContactDTO> contacts = employee.getContacts();
		contacts.remove(contact);
		employee.setContacts(contacts);
	}
													
	public static Employee getEmployee(int id) throws Exception {
		Employee employee = new Employee();
		try {
			employee = daoService.getElement(Long.valueOf(id), Employee.class);
			employee.getName().getLastName();
			return employee;
		} catch(Exception exception) {
			exception.printStackTrace();
			logMsg = "Employee not found!";
			throw exception;
		}
	}

	public static EmployeeDTO addEmployeeRole(EmployeeDTO employee, int role_id) throws Exception{
		try {
			Set<RoleDTO> roles = employee.getRoles();
			Role role = daoService.getElement(Long.valueOf(role_id), Role.class);
			roles.add(mapper.mapToRoleDTO(role));
			employee.setRoles(roles);
			return employee;
		} catch(Exception exception) {
			logMsg = "Invalid role!";
			throw new Exception("",exception);
		}
	}

	public static EmployeeDTO deleteEmployeeRole(EmployeeDTO employee, int role_id) throws Exception {
		Set<RoleDTO> roles = employee.getRoles();
		Role role = daoService.getElement(Long.valueOf(role_id), Role.class);
		roles.remove(mapper.mapToRoleDTO(role));
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

	public static String deleteRole(RoleDTO roleDTO) throws Exception {
		Role role = mapper.mapToRole(roleDTO);
		try {
			daoService.deleteElement(role);
			return "Successfully deleted role " + role.getRoleName() + "!";
		} catch(Exception e) {
			logMsg = "Role " + role.getRoleName() + " cannot be deleted!";
			throw new Exception();
		}
	}

	public static String updateRole(RoleDTO roleDTO, String role_name) throws Exception {
		Role role = mapper.mapToRole(roleDTO);
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

	public static RoleDTO getRole(int role_id) throws Exception {
		Role role = new Role();
		try {
			role = daoService.getElement(Long.valueOf(role_id), Role.class);
			role.getRoleName();
		} catch(Exception exception) {
			logMsg = "Role does not exist!";
			throw exception;
		}
		return mapper.mapToRoleDTO(role);
	}

	public static String getLogMsg() {
		return logMsg;
	}

	private static Role addRole(Long roleId) {
		return daoService.getElement(roleId, Role.class);
	}
}