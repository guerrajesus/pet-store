package pet.store.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.controller.model.PetStoreData.PetStoreEmployee;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Service
public class PetStoreService {
	
	@Autowired
	private PetStoreDao petStoreDao; //instance Variable of Dao
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private CustomerDao customerDao;
	
	@Transactional(readOnly = false)
	public PetStoreData savePetStore(PetStoreData petStoreData) {  //for INSERT and Update
		Integer petStoreId = petStoreData.getPetStoreId();
		PetStore petStore = findOrCreatePetStore(petStoreId);
		
		setPetStoreFields(petStore, petStoreData);
		return new PetStoreData(petStoreDao.save(petStore));
	}
	

	@Transactional(readOnly = false)  // false when Deleting,  
	public void deletePetStoreById(Integer petStoreId) {
		PetStore petStore = findPetStoreById(petStoreId);
		petStoreDao.delete(petStore); 
	}

	@Transactional(readOnly = true)
	public PetStoreData retrievePetStoreById(Integer petStoreId) {
		
		return new PetStoreData(findPetStoreById(petStoreId));
	}
	
	@Transactional(readOnly = true) 
	public List<PetStoreData> retrieveAllPetParks() {    //Get mapping 
		List<PetStore> petStores = petStoreDao.findAll();
		
		List<PetStoreData> results = new LinkedList<>();
		
		for(PetStore petStore : petStores) {
			PetStoreData psd = new PetStoreData(petStore);
			psd.getCustomers().clear();  
			psd.getEmployees().clear(); // clearing both before adding to list
			
			results.add(psd);
		}
			return results;
		
		//2nd way of doing things 
//		return contributorDao.findAll()
//		.stream()
//		.map(cont -> new ContributorData(cont)) //or map(ContributorData::new)
//		.toList();
//	
	}
	
	
	@Transactional(readOnly = false)
	public PetStoreEmployee saveEmployee(Integer petStoreId, PetStoreEmployee petStoreEmployee) { //INSERT and Update
		
		PetStore petStore = findPetStoreById(petStoreId);
		Integer employeeId = petStoreEmployee.getEmployeeId(); //from ARC data
		Employee employee = findOrCreateEmployee(petStoreId, employeeId);
		
		setEmployeeFields(employee, petStoreEmployee); //from Data add to employee
		
		employee.setPetStore(petStore);  
		petStore.getEmployees().add(employee); //Add to petStore employee Set
		
		//Employee dbEmployee = employeeDao.save(employee);
		
		return new PetStoreEmployee(employeeDao.save(employee));
	}
	
	@Transactional(readOnly = false)
	public PetStoreCustomer saveCustomer(Integer petStoreId, PetStoreCustomer petStoreCustomer) {
		PetStore petStore = findPetStoreById(petStoreId);
		Integer customerId = petStoreCustomer.getCustomerId();
		Customer customer = findOrCreateCustomer(petStoreId, customerId);
		
		setCustomerFields(customer,petStoreCustomer);
		
		customer.getPetStores().add(petStore);
		petStore.getCustomers().add(customer);
		
		return new PetStoreCustomer(customerDao.save(customer));
	}
	

	//Transactions above and methods to help them below ^
	
	
	private void setCustomerFields(Customer customer, PetStoreCustomer petStoreCustomer) {
		customer.setCustomerId(petStoreCustomer.getCustomerId());
		customer.setCustomerFirstName(petStoreCustomer.getCustomerFirstName());
		customer.setCustomerLastName(petStoreCustomer.getCustomerLastName());
		customer.setCustomerEmail(petStoreCustomer.getCustomerEmail());
	}


	private Customer findOrCreateCustomer(Integer petStoreId, Integer customerId) {
		
		Customer customer;
		
		if(Objects.isNull(customerId)) {
			customer = new Customer();
		}
		else {
			customer = findCustomerById(customerId, petStoreId);
		}
		return customer;
	}
	


	private Customer findCustomerById(Integer customerId, Integer petStoreId) {
		
		Customer customer = customerDao.findById(customerId).orElseThrow(() -> 
		new NoSuchElementException("Customer with ID= " + customerId + " does not exist"));
		
					    // Set into streams | Boolean Returning whether any elements of this stream match the provided	
		if (customer.getPetStores().stream().anyMatch(petStore -> petStore.getPetStoreId() == petStoreId)) {
			return customer; 					     //Lambda parameter -> separates action ^
		} else {
			throw new IllegalArgumentException("The customer with ID=" + customerId 
					+ " is not a member of the pet store with ID=" + petStoreId);
		}
//		for(PetStore petStore : customer.getPetStores()) {
//				if(petStore.getPetStoreId() == petStoreId) {
//					return customer; //Returns right after found
//				} 
//			}
//					//if no match then throw Exception outside loop
//			throw new IllegalArgumentException("The customer with ID=" + customerId 
//			        + " is not a member of the pet store with ID=" + petStoreId);
	}


	private void setEmployeeFields(Employee employee, PetStoreEmployee petStoreEmployee) {
		employee.setEmployeeId(petStoreEmployee.getEmployeeId());
		employee.setEmployeeFirstName(petStoreEmployee.getEmployeeFirstName());
		employee.setEmployeeLastName(petStoreEmployee.getEmployeeLastName());
		employee.setEmployeePhone(petStoreEmployee.getEmployeePhone());
		employee.setEmployeeJobTitle(petStoreEmployee.getEmployeeJobTitle());
	}


	private Employee findOrCreateEmployee(Integer petStoreId, Integer employeeId) {
		Employee employee;
		
		if(Objects.isNull(employeeId)) {
			employee = new Employee();
		}
		else {
			employee = findEmployeeById(employeeId, petStoreId);
		}
		return employee;
	}


	private Employee findEmployeeById(Integer employeeId, Integer petStoreId) {
		Employee employee = employeeDao.findById(employeeId).orElseThrow(() -> 
		new NoSuchElementException("Employee with ID= " + employeeId + " does not exist"));
		
		if(employee.getPetStore().getPetStoreId() == petStoreId) {
			return employee;
		} 
		else {
			throw new IllegalArgumentException("Employee with ID= " + employeeId 
					+ " is not employeed by Pet Store ID = " + petStoreId);
		}
		
		// fail fast approach 
//		if (employee.getPetStore().getPetStoreId() != petStoreId) {
//		    throw new IllegalArgumentException("The employee with ID=" + employeeId 
//		        + " is not employed by the pet store with ID=" + petStoreId + ".");
//		}
//		return employee;
		
	}


	private PetStore findPetStoreById(Integer petStoreId) {
															//Lambda expression
		return petStoreDao.findById(petStoreId).orElseThrow(() -> 
		new NoSuchElementException("Pet Store with ID= " + petStoreId + " does not exist") );
	}			
	
	
	private void setPetStoreFields(PetStore petStore, PetStoreData petStoreData) {
		petStore.setPetStoreId(petStoreData.getPetStoreId());
		petStore.setPetStoreName(petStoreData.getPetStoreName());
		petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
		petStore.setPetStoreCity(petStoreData.getPetStoreCity());
		petStore.setPetStoreState(petStoreData.getPetStoreState());
		petStore.setPetStoreZip(petStoreData.getPetStoreZip());
		petStore.setPetStorePhone(petStoreData.getPetStorePhone());
	}
	
	
	private PetStore findOrCreatePetStore(Integer petStoreId) {
		PetStore petStore;
		
		if(Objects.isNull(petStoreId)) {
			petStore = new PetStore();
		}
		else {
			petStore = findPetStoreById(petStoreId);
		}
		
		return petStore;
	}

	
	


	
	
}
