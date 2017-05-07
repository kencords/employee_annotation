package ecc.cords;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class EmployeeUI {

	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_RESET = "\u001B[0m";

	private static EmployeeUI employeeUI = null;
	private static DaoService daoService = new DaoService();
	private static String logMsg = "";

	public String addEmployee() throws Exception {
		System.out.print("\033\143");
		System.out.println("ADD NEW EMPLOYEE...\n\n");
		System.out.println("BASIC INFORMATION");
		String lastname = InputHelper.askString("Enter Lastname: ", false);
		String firstname = InputHelper.askString("Enter Firstname: ", false);
		String middlename = InputHelper.askString("Enter Middlename: ", false);
		String suffix = InputHelper.askString("Enter Suffix: ", true);
		String title = InputHelper.askString("Enter Title: ", true);
		Date birthdate = InputHelper.askDate("Enter Birthdate (yyyy-mm-dd): ");
		float gwa = InputHelper.askPositiveFloat("Enter GWA (float): ", false);
		System.out.println("ADDRESS:");
		int streetNo = InputHelper.askPositiveNumber("Enter Street No: ", false);
		String street = InputHelper.askString("Enter Street: ", false);
		String brgy = InputHelper.askString("Enter Barangay: ", false);
		String city =InputHelper.askString("Enter City: ", false);
		String zipcode =InputHelper.askString("Enter Zipcode: ", false);
		System.out.println("CONTACT INFORMATION");
		Set<Contact> contacts = ContactUI.getInstance().askContacts(true);
		System.out.println("\nCAREER INFORMATION");
		boolean curHired = InputHelper.askBoolean("Is currently hired? (Y|N): ");
		Date hiredate = InputHelper.askDate("Enter Date Hired (yyyy-mm-dd): ");
		Set<Role> roles = RoleUI.getInstance().askRoles();
		return EmployeeManager.addEmployee(EmployeeManager.createName(lastname, firstname, middlename, suffix, title), birthdate, gwa,
		EmployeeManager.createAddress(streetNo, street, brgy, city, zipcode), 
		contacts, curHired, hiredate, roles);
	}

	public Employee editEmployeeDetails(Employee employee) throws Exception {
		System.out.println("\n1. EDIT NAME");
		System.out.println("2. EDIT BIRTHDATE");
		System.out.println("3. EDIT ADDRESS");
		System.out.println("4. EDIT GWA");
		System.out.println("5. EDIT CAREER INFORMATION");
		System.out.println("6. BACK");
		String choice = InputHelper.askChoice("\nWhat do you want to do? (Enter Choice Number): ");

		switch(choice) {
			case "1":
				editEmployeeName(employee);
				break;
			case "2":
				System.out.println("\nEDIT BIRTHDATE\n");
				employee.setBirthDate(InputHelper.askDate("Enter Birthdate (yyyy-mm-dd): "));
				logMsg = "Successfully edited Employee Birthdate!";
				break;
			case "3":
				editEmployeeAddress(employee);
				break;
			case "4":
				System.out.println("\nEDIT GWA\n");
				employee.setGwa(InputHelper.askPositiveFloat("Enter GWA (float): ", false));
				logMsg = "Successfully edited Employee GWA!";
				break;
			case "5":
				System.out.println("\nEDIT CAREER INFORMATION\n");
				employee.setCurrentlyHired(InputHelper.askBoolean("Is currently hired? (Y|N): "));
				employee.setHireDate(InputHelper.askDate("Enter Date Hired (yyyy-mm-dd): "));
				logMsg = "Successfully edited Employee CAREER INFORMATION!"; 
				break;
			case "6":
				return employee;
			default:
				System.out.println("Invalid Choice!");
		}

		return employee;
	}

	public String deleteEmployee() throws Exception {
		System.out.print("\033\143");
		System.out.println("DELETE EMPLOYEE...\n\n");
		System.out.println("EMPLOYEE LIST:");
		System.out.println(getEmployees());
		int id = InputHelper.askPositiveNumber("\nEnter Employee ID: ", false);		
		daoService.deleteElement(EmployeeManager.getEmployee(id));
		return "Deleted Employee " + id + "!";
	}

	public void editEmployee() throws Exception {
		System.out.print("\033\143");
		System.out.println("EDIT EMPLOYEE...\n\n");
		System.out.println("EMPLOYEE LIST:");
		System.out.println(getEmployees());
		int id = InputHelper.askPositiveNumber("\nEnter Employee ID: ", false);
		Employee employee = EmployeeManager.getEmployee(id);
		manageEmployee(employee);
	}

	public String getEmployeeDetail(Employee employee, String type) {
		StringBuilder sb = new StringBuilder();
		sb.append("\nEMPLOYEE ID: " + employee.getEmpId());
		sb.append("\nNAME: " + employee.getName().getTitle() + " " + employee.getName().getFirstName() + 
		" " + employee.getName().getMiddleName() + " " + 
		(type.equals("LN")? emphasizeText(employee.getName().getLastName()) : employee.getName().getLastName()) + 
		" " + employee.getName().getSuffix());
		sb.append("\nADDRESS: " + employee.getAddress());
		sb.append("\nBIRTHDATE: " + employee.getBirthDate());
		sb.append(type.equals("GWA")? emphasizeText("\nGWA: " + employee.getGwa()) : "\nGWA: " + employee.getGwa());
		String curHired = employee.isCurrentlyHired() ? "YES" : "NO";
		sb.append("\nCURRENTLY HIRED: " + curHired);
		sb.append(type.equals("DH")? emphasizeText("\nDATE HIRED: " + employee.getHireDate()) : "\nDATE HIRED: " + employee.getHireDate());
		sb.append("\nCONTACTS: "); 
		employee.getContacts().stream()
							  .sorted((c1,c2) -> Long.compare(c1.getContactId(),c2.getContactId()))
							  .forEach(contact -> sb.append("\n" + (type.equals("")? contact : "[" + contact.getContactType() + ": " + contact.getContactValue() + "]")));
		sb.append("\nROLES: " + employee.getRoles() + "\n");
		return sb.toString();
	}

	public String getEmployeeDetails(List<Employee> employees, String type) {
		StringBuilder sb = new StringBuilder();
		employees.forEach(employee -> sb.append(getEmployeeDetail(employee,type)));
		return sb.toString(); 
	}

	public void manageEmployee(Employee employee) {
		while(true) {
			System.out.println(getEmployeeDetail(daoService.getElement(employee.getEmpId(), Employee.class),""));
			System.out.println("1. EDIT EMPLOYEE DETAILS");
			System.out.println("2. ADD ROLE");
			System.out.println("3. DELETE ROLE");
			System.out.println("4. ADD CONTACT");
			System.out.println("5. UPDATE CONTACT");
			System.out.println("6. DELETE CONTACT");
			System.out.println("7. BACK");
			System.out.println(logMsg.equals("")? "" : "\n" + logMsg + "\n");
			logMsg = "";
			String choice = InputHelper.askChoice("What do you want to do? (Enter Choice Number): ");
			int id = 0;
			try {
				switch(choice) {
					case "1":
						employee = editEmployeeDetails(employee);
						break;
					case "2":
						System.out.println("\n" + RoleUI.getInstance().getFilteredRoles(employee));
						id = InputHelper.askPositiveNumber("What Role? (Enter Role ID): ", false);
						employee = EmployeeManager.addEmployeeRole(employee, id);
						break;
					case "3":
						id = InputHelper.askPositiveNumber("What Role? (Enter Role ID to delete): ", false);
						employee = EmployeeManager.deleteEmployeeRole(employee, id);
						break;
					case "4":
						employee = EmployeeManager.addContact(employee, ContactUI.getInstance().askContacts(false));
						break;
					case "5":
						ContactUI.getInstance().manageContact(employee, false);
						break;	
					case "6":
						ContactUI.getInstance().manageContact(employee, true);
						break;
					case "7":
						return;
					default:
						System.out.println("Invalid Choice!");
				}
				daoService.updateElement(employee);
			} catch(Exception exception) {
				logMsg = EmployeeManager.getLogMsg();
			}
		}
	}

	private Employee editEmployeeName(Employee employee) {
		System.out.println("\nEDIT EMPLOYEE NAME\n");
		employee.getName().setLastName(InputHelper.askString("Enter Lastname: ", false));
		employee.getName().setFirstName(InputHelper.askString("Enter Firstname: ", false));
		employee.getName().setMiddleName(InputHelper.askString("Enter Middlename: ", false));
		employee.getName().setSuffix(InputHelper.askString("Enter Suffix: ", true));
		employee.getName().setTitle(InputHelper.askString("Enter Title: ", true));
		logMsg = "Successfully edited Employee Name!";
		return employee;
	}

	private Employee editEmployeeAddress(Employee employee) {
		System.out.println("\nEDIT EMPLOYEE ADDRESS\n");
		employee.getAddress().setStreetNo(InputHelper.askPositiveNumber("Enter Street No: ", false));
		employee.getAddress().setStreet(InputHelper.askString("Enter Street: ", false));
		employee.getAddress().setBrgy(InputHelper.askString("Enter Barangay: ", false));
		employee.getAddress().setCity(InputHelper.askString("Enter City: ", false));
		employee.getAddress().setZipcode(InputHelper.askString("Enter Zipcode: ", false));
		logMsg = "Successfully edited Employee Address!";
		return employee;
	}

	private String emphasizeText(String msg) {
		return ANSI_GREEN + msg + ANSI_RESET;
	}

	private String getEmployees(){
		StringBuilder sb = new StringBuilder();
		List<Employee> employees = daoService.getAllElements(Employee.class);
		employees.stream()
				 .sorted((employee1,employee2) -> Long.compare(employee1.getEmpId(), employee2.getEmpId()))
				 .forEach(employee -> sb.append(employee + "\n"));
		return sb.toString();
	}

	public static EmployeeUI getInstance(){
		if(employeeUI == null) {
			employeeUI = new EmployeeUI();
		}
		return employeeUI;
	}
}