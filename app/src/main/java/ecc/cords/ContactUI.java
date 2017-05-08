package ecc.cords;

import java.util.HashSet;
import java.util.Set;

public class ContactUI {

	private static ContactUI contactUI = null;
	private static DTO_EntityMapper mapper = new DTO_EntityMapper();
	private static String logMsg = "";

	public void manageContact(EmployeeDTO employee, boolean toDelete) throws Exception {
		System.out.println("\nCONTACT DETAILS:");
		employee.getContacts().stream()
							  .sorted((c1,c2) -> Long.compare(c1.getContactId(),c2.getContactId()))
							  .forEach(System.out::println);
		ContactDTO contact = EmployeeManager.getContact(employee.getEmpId(), InputHelper.askPositiveNumber("What contact detail to update/delete? (Enter Number): ", false));
		String value = "";
		if(!toDelete) {
			if(contact.getContactType().equals("Landline"))
				value = InputHelper.askLandline("Enter Landline (xxx-xxxx): ");
			else if(contact.getContactType().equals("Mobile"))
				value = InputHelper.askMobile("Enter Mobile (xxxx-xxx-xxxx): ");
			else if(contact.getContactType().equals("Email"))
				value = InputHelper.askEmail("Enter Email: ");
			EmployeeManager.updateContact(employee,contact,value);
			return;
		}
		EmployeeManager.deleteContact(employee,contact);
	}

	public Set<ContactDTO> askContacts(boolean isNew) {
		Set<ContactDTO> contacts = new HashSet<>(); 
		while(true) {
			System.out.println("\nWHAT TYPE OF CONTACT?");
			System.out.println("1. LANDLINE");
			System.out.println("2. MOBILE");
			System.out.println("3. EMAIL");
			System.out.println("4. DONE");
			String choice = InputHelper.askChoice("What contact do you want to add? (Enter Choice Number): ");
			switch(choice){
				case "1":
					contacts.add(mapper.createContactDTO("Landline",InputHelper.askLandline("Enter Landline (xxx-xxxx): ")));
					break;
				case "2":
					contacts.add(mapper.createContactDTO("Mobile",InputHelper.askMobile("Enter Mobile (xxxx-xxx-xxxx): ")));
					break;
				case "3":
					contacts.add(mapper.createContactDTO("Email", InputHelper.askEmail("Enter Email: ")));
					break;
				case "4":
					if(contacts.size()==0 && isNew){
						System.out.println("Add at least one contact!");
						continue;
					}
					return contacts;
				default:
					System.out.println("Invalid Choice!");
			}
		}
	}

	public static ContactUI getInstance() {
		if(contactUI == null){
			contactUI = new ContactUI();
		}
		return contactUI;
	}
}