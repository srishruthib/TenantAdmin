package Management.TenantAdmin.Controller;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import Management.TenantAdmin.Entity.Tenant;
import Management.TenantAdmin.Generics.GenericResult;
import Management.TenantAdmin.Service.TenantService;

@RequestMapping("/tenants")
@RestController
public class TenantController {
	private final Logger logger = LoggerFactory.getLogger(TenantController.class);

	@Autowired
	private TenantService tenantService;

	@GetMapping
	public ResponseEntity<?> getTenants(@RequestParam(required = false) Map<String, Object> tenants,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id") String sortField, @RequestParam(defaultValue = "asc") String sortOrder) {
				long startTime = System.currentTimeMillis();
		try {
			logger.info("Received request to get tenants.");
			GenericResult<Page<Tenant>> result = tenantService.getTenants(tenants, page, size, sortField, sortOrder);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			logger.error("Error getting tenants. Error: {}", e.getMessage(), e);
			long endTime = System.currentTimeMillis();
            logger.info("GET API Execution Time: {} ms", endTime - startTime);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error getting tenants: " + e.getMessage());
		}
	}

	
	@PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<?> createTenant(@RequestBody List<Tenant> newTenants) {
    long startTime = System.currentTimeMillis();
    try {
        logger.info("Received request to create users.");
        GenericResult<?> result = tenantService.createTenant(newTenants);
        long endTime = System.currentTimeMillis();
        logger.info("DB Create Execution Time: {} ms", endTime - startTime);
        return ResponseEntity.ok(result);
    } catch (IllegalArgumentException e) {
        return handleBadRequest(e);
    } catch (Exception e) {
        long endTime = System.currentTimeMillis();
        logger.error("DB Create Execution Time: {} ms", endTime - startTime);
        return handleInternalServerError(e);
	}
}

    @PutMapping
    public ResponseEntity<?> updateTenant(@RequestBody List<Tenant> updates) {
        long startTime = System.currentTimeMillis();
        try {
            logger.info("Received request to update tenants");
            GenericResult<String> result = tenantService.updateTenant(updates);

            if (result.isSuccess()) {
                logger.info("Tenants updated successfully.");
                return ResponseEntity.ok(result);
            } else {
                logger.error("Failed to update tenants: {}", result.getMessage());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            }

        } catch (IllegalArgumentException e) {
            return handleBadRequest(e);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            logger.info("DB Update Execution Time: {} ms", endTime - startTime);
            return handleInternalServerError(e);
        }
    }
    
    
    
	@DeleteMapping
    public ResponseEntity<?> deleteTenant(@RequestBody List<Integer> delIds) {

    long startTime = System.currentTimeMillis();
    try {
        logger.info("Received request to delete tenants with IDs: {}", delIds);
        GenericResult<String> result = tenantService.deleteTenant(delIds);
        long endTime = System.currentTimeMillis();
        logger.info("DB Delete Execution Time: {} ms", endTime - startTime);
        return ResponseEntity.ok(result);
    } catch (IllegalArgumentException e) {
        return handleBadRequest(e);
    } catch (Exception e) {
        return handleInternalServerError(e);
	}
    }
	
	private ResponseEntity<String> handleBadRequest(Exception e) {
		logger.error("Bad request: {}", e.getMessage(), e);
		return ResponseEntity.badRequest().body("Bad request: " + e.getMessage());
	}

	private ResponseEntity<String> handleInternalServerError(Exception e) {
		logger.error("Internal server error: {}", e.getMessage(), e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error: " + e.getMessage());
	}
}