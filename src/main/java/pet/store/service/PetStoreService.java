package pet.store.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreData;
import pet.store.dao.PetStoreDao;
import pet.store.entity.PetStore;

@Service
public class PetStoreService {
	
	@Autowired
	private PetStoreDao petStoreDao; //instance Variable of Dao
	
	@Transactional(readOnly = false)
	public PetStoreData savePetStore(PetStoreData petStoreData) {  //for INSERT and Update
		Integer petStoreId = petStoreData.getPetStoreId();
		PetStore petStore = findOrCreatePetStore(petStoreId);
		
		setPetStoreFields(petStore, petStoreData);
		return new PetStoreData(petStoreDao.save(petStore));
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
	
	
	private PetStore findPetStoreById(Integer petStoreId) {
																 //Lambda expression
		return petStoreDao.findById(petStoreId).orElseThrow(() -> 
				new NoSuchElementException("Pet Store with ID= " + petStoreId + " does not exist") );
	}			

	@Transactional(readOnly = false)  // false when Deleting, 
	public void deletePetStoreById(Integer petStoreId) {
		PetStore petStore = findPetStoreById(petStoreId);
		petStoreDao.delete(petStore); 
	}

	@Transactional(readOnly = true) 
	public List<PetStoreData> retrieveAllPetParks() {
		List<PetStore> petStores = petStoreDao.findAll();
		
		List<PetStoreData> response = new LinkedList<>();
		
		for(PetStore petStore : petStores) {
			response.add(new PetStoreData(petStore));
		}
		
		return response;
	}

	
	
}
