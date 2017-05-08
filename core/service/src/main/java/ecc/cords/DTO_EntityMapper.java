package ecc.cords;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class DTO_EntityMapper {

	private static DaoService daoService = new DaoService();

	public AddressDTO createAddressDTO(Address address) {
		return new AddressDTO(address.getStreetNo(), address.getStreet(), address.getBrgy(), address.getCity(), address.getZipcode());
	}

	public Set<Contact> createContactSet(Employee employee, Set<ContactDTO> contactsDTO) {
		Set<Contact> contacts = new HashSet<>();
		contactsDTO.forEach(contact -> {
			contacts.add(new Contact(contact.getContactType(), contact.getContactValue()));
		});
		contacts.forEach(contact -> contact.setEmployee(employee));
		return contacts;
	}

	public Set<ContactDTO> createContactSetDTO(Set<Contact> contacts) {
		Set<ContactDTO> contactsDTO = new HashSet<>();
		contacts.forEach(contact -> {
			ContactDTO contactDTO = new ContactDTO(contact.getContactType(), contact.getContactValue());
			contactDTO.setContactId(contact.getContactId());
			contactsDTO.add(contactDTO);
		});
		return contactsDTO;
	}

	public Set<Role> createRoleSet(Set<RoleDTO> rolesDTO) {
		Set<Role> roles = new HashSet<>();
		rolesDTO.forEach(role -> roles.add(mapToRole(role)));
		return roles;
	}

	public Set<RoleDTO> createRoleSetDTO(Set<Role> roles) {
		Set<RoleDTO> rolesDTO = new HashSet<>();
		roles.forEach(role -> {
			try { 
				rolesDTO.add(mapToRoleDTO(role));
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		});
		return rolesDTO;
	}

	public List<RoleDTO> getAllRoles() {
		List<Role> roles = daoService.getAllElements(Role.class);
		List<RoleDTO> rolesDTO = new ArrayList<>();
		roles.forEach(role -> {
			rolesDTO.add(new RoleDTO(role.getRoleId(), role.getRoleName()));
		});
		return rolesDTO;
	}

	public EmployeeDTO mapToEmployeeDTO(Employee employee) throws Exception {
		EmployeeDTO empDTO = new EmployeeDTO();
		empDTO.setEmpId(employee.getEmpId());
		empDTO.setLastName(employee.getName().getLastName());
		empDTO.setFirstName(employee.getName().getFirstName());
		empDTO.setMiddleName(employee.getName().getMiddleName());
		empDTO.setSuffix(employee.getName().getSuffix());
		empDTO.setTitle(employee.getName().getTitle());
		empDTO.setBirthDate(employee.getBirthDate());
		empDTO.setGwa(employee.getGwa());
		empDTO.setCurrentlyHired(employee.isCurrentlyHired());
		empDTO.setHireDate(employee.getHireDate());
		empDTO.setAddress(createAddressDTO(employee.getAddress()));
		empDTO.setContacts(createContactSetDTO(employee.getContacts()));
		empDTO.setRoles(createRoleSetDTO(employee.getRoles()));
		return empDTO;
	}

	public List<EmployeeDTO> mapEmployeeDTOList(String order) {
		List<Employee> employees = (!order.equals("gwa") ? daoService.getOrderedEmployees(order) : 
		daoService.getAllElements(Employee.class));
		List<EmployeeDTO> employeesDTO = new ArrayList<>();
		employees.forEach(e -> {
			try {
				employeesDTO.add(mapToEmployeeDTO(e));
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		});
		return employeesDTO;		
	}

	/*public Employee mapToEmployee(EmployeeDTO empDTO){

	}*/

	public RoleDTO mapToRoleDTO(Role role) throws Exception {
		return new RoleDTO(role.getRoleId(), role.getRoleName());
	}

	public Role mapToRole(RoleDTO roleDTO) {
		Role role = new Role();
		role.setRoleId(roleDTO.getRoleId());
		role.setRoleName(roleDTO.getRoleName());
		return role;
	}
}