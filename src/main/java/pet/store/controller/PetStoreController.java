package pet.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pet.store.controller.model.PetStoreData;
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
	
	//@ResponseStatus(code = HttpStatus.)
	@PutMapping("update/{petStoreId}")
	public PetStoreData updatePetStoreById (@RequestBody PetStoreData petStoreData, 
			@PathVariable Integer petStoreId) {
		petStoreData.setPetStoreId(petStoreId);
		
		log.info("Updated Pet store with ID ={}", petStoreId); 
		return petStoreService.savePetStore(petStoreData);
		
	}
	
	//Starting week 15 
//	@GetMapping
//	public List<PetStoreData> retrieveAllPetParks () {
//		log.info("Retrieved all Pet Parks");
//		return petStoreService.retrieveAllPetParks();
//	}
	
		//Starting week 15 
//	@DeleteMapping("/delete/{petStoreId}")  //Parameter 
//	public Map<String, String> deletePetStoreById (@PathVariable Integer petStoreId) {
//		log.info("Deleted Pet Store with Id of {}", petStoreId);
//		petStoreService.deletePetStoreById(petStoreId);
//		
//		return Map.of("message", "Deleted Pet store with Id=" + petStoreId);
//	}
	
}
