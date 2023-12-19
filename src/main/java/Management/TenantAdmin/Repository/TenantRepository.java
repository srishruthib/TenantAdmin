// package Management.TenantAdmin.Repository;

// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.mongodb.repository.MongoRepository;
// import org.springframework.data.mongodb.repository.Query;
// import Management.TenantAdmin.Entity.Tenant;

// public interface TenantRepository extends MongoRepository<Tenant, Integer> {
	
	
// 	Page<Tenant> findAll(Pageable pageable);

// 	@Query("{ ?0 : ?1 }")
// 	Page<Tenant> findByFilter(String key, Object value, Pageable pageable);
	
	//  @Query("{'customerName': ?0, 'customerEmail': ?1, 'customerPhone': ?2, 'adminName': ?3, 'adminEmail': ?4, 'customerNumber': ?5, 'parentCompany': ?6, 'country': ?7, 'street': ?8, 'city': ?9, 'state': ?10, 'zip': ?11, 'taxId': ?12, 'website': ?13, 'createDate': ?14, 'updateDate': ?15, 'createdBy': ?16, 'updatedBy': ?17}")
	//     Tenant createTenant(String customerName, String customerEmail, String customerPhone, String adminName, String adminEmail, String customerNumber, String parentCompany, String country, String street, String city, String state, String zip, String taxId, String website, String createDate, String updateDate, String createdBy, String updatedBy);
	 
	//   @Query(value = "{'id': ?0}", fields = "{'$set': {'customerName': ?1, 'customerEmail': ?2, 'customerPhone': ?3, 'adminName': ?4, 'adminEmail': ?5, 'customerNumber': ?6, 'parentCompany': ?7, 'country': ?8, 'street': ?9, 'city': ?10, 'state': ?11, 'zip': ?12, 'taxId': ?13, 'website': ?14, 'createDate': ?15, 'updateDate': ?16, 'createdBy': ?17, 'updatedBy': ?18}}")
	//     void updateTenantById(int id, String customerName, String customerEmail, String customerPhone, String adminName, String adminEmail, String customerNumber, String parentCompany, String country, String street, String city, String state, String zip, String taxId, String website, String createDate, String updateDate, String createdBy, String updatedBy);
	
	//   @Query("{'_id': ?0}")
	//     void deleteTenantById(int id);

	//     // Native query for retrieving all fields of a tenant by ID
	//     @Query("{'_id': ?0}")
	//     Tenant findTenantById(int id);
	    
	// }
	
	

