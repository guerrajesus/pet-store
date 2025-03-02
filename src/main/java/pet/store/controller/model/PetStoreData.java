package pet.store.controller.model; //DTO or java object

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Data //adds getters and setters 
@NoArgsConstructor //Constructor
public class PetStoreData { //DTO or java object
	
    private Integer petStoreId;	
	private String petStoreName;
	private String petStoreAddress;
	private String petStoreCity;
	private String petStoreState;
	private String petStoreZip;
	private String petStorePhone;
	private Set<PetStoreEmployee> employees = new HashSet<>(); //Many to one Store
	private Set<PetStoreCustomer> customers = new HashSet<>(); //Many to Many 
	
	public PetStoreData (PetStore petStore) { //Constructor
		this.petStoreId = petStore.getPetStoreId();
		this.petStoreName = petStore.getPetStoreName();
		this.petStoreAddress = petStore.getPetStoreAddress();
		this.petStoreCity = petStore.getPetStoreCity();
		this.petStoreState = petStore.getPetStoreState();
		this.petStoreZip = petStore.getPetStoreZip();
		this.petStorePhone = petStore.getPetStorePhone();
		
		if(petStore.getEmployees() != null) {
			for(Employee employee : petStore.getEmployees()) {
			this.employees.add(new PetStoreEmployee(employee));
			}
		}
		
		if(petStore.getCustomers() != null) {
			for(Customer customer : petStore.getCustomers()) {
			this.customers.add(new PetStoreCustomer(customer));
			}
		}
		
	}
	
	
	@Data
	@NoArgsConstructor
	public static class PetStoreCustomer {  // copying Customer Entity - Set<petStores> cause its above ^
		private Integer customerId;
		private String customerFirstName;
		private String customerLastName;
		private String customerEmail;
		
		PetStoreCustomer(Customer customer) { //Constructor
			this.customerId = customer.getCustomerId();
			this.customerFirstName = customer.getCustomerFirstName();
			this.customerLastName = customer.getCustomerLastName();
			this.customerEmail = customer.getCustomerEmail();
		}
	}
	
	@Data
	@NoArgsConstructor
	public static class PetStoreEmployee { // copying Customer Entity - petStores cause its above ^
		private Integer employeeId;       
		private String employeeFirstName;
		private String employeeLastName;
		private String employeePhone;
		private String employeeJobTitle;
		
		PetStoreEmployee(Employee employee) { //Constructor
			this.employeeId = employee.getEmployeeId();
			this.employeeFirstName = employee.getEmployeeFirstName();
			this.employeeLastName = employee.getEmployeeLastName();
			this.employeePhone = employee.getEmployeePhone();
			this.employeeJobTitle = employee.getEmployeeJobTitle();
		}
	}
	
	

}
