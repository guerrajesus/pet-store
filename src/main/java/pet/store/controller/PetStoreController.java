package pet.store.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.controller.model.PetStoreData.PetStoreEmployee;
import pet.store.service.PetStoreService;

@RestController               //expects and returns JSON in request/response bodies | default 200 (OK)
@RequestMapping("/pet_store") //tell Spring URI for every HTTP request must start with 
@Slf4j 						  //Logger log.info for the console
public class PetStoreController { 
	
	@Autowired 
	private PetStoreService petStoreService;
	
	@PostMapping("/create") // POST - CREATE 
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreData insertOrModifyPetStore (@RequestBody PetStoreData petStoreData) {
		log.info("Creating Park Store: {}", petStoreData); // log.info for the console 
		return petStoreService.savePetStore(petStoreData);
	}
	
	@PutMapping("update/{petStoreId}")
	public PetStoreData updatePetStoreById (@RequestBody PetStoreData petStoreData, 
			@PathVariable Integer petStoreId) {
		petStoreData.setPetStoreId(petStoreId);
		
		log.info("Updated Pet store with ID ={}", petStoreId); 
		return petStoreService.savePetStore(petStoreData);
		
	}
	
	@GetMapping   //pet_store gets all petStores 
	public List<PetStoreData> retrieveAllPetParks () {
		log.info("Retrieved all Pet Parks");
		return petStoreService.retrieveAllPetParks();
	}
	
	@GetMapping("/{petStoreId}")
	public PetStoreData retrievePetStoreById (@PathVariable Integer petStoreId) {
		log.info("Retrieved all info from Pet Store ID = " + petStoreId);
		return petStoreService.retrievePetStoreById(petStoreId);
	}
	 
	@DeleteMapping("/delete/{petStoreId}")  //Parameter 
	public Map<String, String> deletePetStoreById (@PathVariable Integer petStoreId) {
		log.info("Deleted Pet Store with Id of {}", petStoreId);
		petStoreService.deletePetStoreById(petStoreId);
		
		return Map.of("message", "Deleted Pet store with Id=" + petStoreId);
	}
	
	// Employee 
	
	@PostMapping("/{petStoreId}/employee")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreEmployee createEmployeeWithPetStoreId (@PathVariable Integer petStoreId,
			@RequestBody PetStoreEmployee petStoreEmployee) {
	
		log.info("Employee {} Created to Pet Store ID = {}  ", petStoreEmployee, petStoreId);
		return petStoreService.saveEmployee(petStoreId, petStoreEmployee);
	}
	
	
	 @PostMapping("/{petStoreId}/customer")
	 @ResponseStatus(code = HttpStatus.CREATED)
	 public PetStoreCustomer createCustomerWithPetStoreId (@PathVariable Integer petStoreId,
			 @RequestBody PetStoreCustomer petStoreCustomer) {
		 
		 log.info("Customer {} Created on Pet Store ID = {}  ", petStoreCustomer, petStoreId);
		 return petStoreService.saveCustomer(petStoreId, petStoreCustomer);
		 
	 }
	
	
	
	
	
	
}
