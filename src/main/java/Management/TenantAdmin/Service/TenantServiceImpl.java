
 package Management.TenantAdmin.Service;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import Management.TenantAdmin.Entity.Tenant;
import Management.TenantAdmin.Generics.GenericResult;
import org.springframework.stereotype.Service;


@Service
public class TenantServiceImpl implements TenantService {
    @Autowired
    private  MongoTemplate mongoTemplate;
    public TenantServiceImpl(MongoTemplate mongoTemplate ) {
        this.mongoTemplate = mongoTemplate;
    }
	private final Logger logger = LoggerFactory.getLogger(TenantServiceImpl.class);
    
  public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getName);
    }
	@Override
    public GenericResult<Page<Tenant>> getTenants(Map<String, Object> tenants, int page, int size, String sortField, String sortOrder) {
        GenericResult<Page<Tenant>> result = new GenericResult<>();
        Sort.Direction direction = sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortField);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Tenant> tenantPage;
        long startTime = System.currentTimeMillis();
        try {
            Query query = new Query();
            Criteria criteria = new Criteria();
 
            if (tenants != null && !tenants.isEmpty()) {
                if (tenants.containsKey("id")) {
                    Integer id = Integer.parseInt(tenants.get("id").toString());
                    criteria.and("id").is(id);
                } else {
                    Map.Entry<String, Object> entry = tenants.entrySet().iterator().next();
                    criteria = createCriteriaForFilter(entry.getKey(), entry.getValue());
                }
            }
 
            query.addCriteria(criteria);
            query.with(pageable);
 
            List<Tenant> tenantsList = mongoTemplate.find(query.with(pageable), Tenant.class);
            long totalCount = mongoTemplate.count(query, Tenant.class);
    
            tenantPage = new PageImpl<>(tenantsList, pageable, totalCount);
            result.setSuccess(true);
            result.setMessage("tenants found");
            result.setData(tenantPage);
 
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("tenants not found " + e.getMessage());
            logger.error("tenants not found", e);
        }
        long endTime = System.currentTimeMillis();
        logger.info("GET API Response Time: {} ms", endTime - startTime);
        return result;
    }private Criteria createCriteriaForFilter(String key, Object value) {
        Criteria criteria = new Criteria();
    
        String[] fieldsToMatch = {
            "customerName", "customerEmail", "customerPhone",
            "adminName", "adminEmail", "customerNumber",
            "parentCompany", "country", "street", "city",
            "state", "zip", "taxId", "website"
        };
    
        for (String field : fieldsToMatch) {
            if (field.equals(key)) {
                criteria.and(field).regex(value.toString());
                break;  // Break out of the loop once a match is found
            }
        }
    
        return criteria;
    }
  

    @Override
public GenericResult<Tenant> createTenant(List<Tenant> newTenants) {
    GenericResult<Tenant> genericResult = new GenericResult<>();
    long startTime = System.currentTimeMillis();
    try {
        for (Tenant tenant : newTenants) {
            validateTenant(tenant);
    
            tenant.setUpdateDate(LocalDateTime.now());
            tenant.setCreateDate(LocalDateTime.now());
            // tenant.setUpdatedBy(getCurrentAuditor().toString());// Replace with your actual logic to get the logged-in user
            // tenant.setCreatedBy(getCurrentAuditor().toString());
            String createdBy = getCurrentAuditor().orElse("Veena").toString();
            tenant.setCreatedBy(createdBy);
            
          
            
            tenant.setId(SequenceGeneratorService.getSeqNumber(tenant.getSEQUENCE_NAME()));
            mongoTemplate.save(tenant);
        }
        genericResult.setSuccess(true);
        genericResult.setMessage("Tenants Created");
        logger.info("Tenants Saved");
        return genericResult;
    } catch (Exception e) {
        handleException(genericResult, e, "Error in createTenant method");
        genericResult.setSuccess(false);
        genericResult.setMessage("Tenants failed to create: " + e.getMessage());
        logger.error("Tenants failed to create", e);
    } finally {
        long endTime = System.currentTimeMillis();
        logger.info("DB Create Execution Time: {} ms", endTime - startTime);
    }
    return genericResult;
}

private void validateTenant(Tenant tenant) {
    List<String> requiredFields = Arrays.asList(
            "customerName", "customerEmail", "customerPhone",
            "adminName", "adminEmail", "customerNumber",
            "parentCompany", "country", "street", "city",
            "state", "zip", "taxId", "website"
    );

    List<String> missingFields = new ArrayList<>();

    for (String field : requiredFields) {
        try {
            Field declaredField = Tenant.class.getDeclaredField(field);
            declaredField.setAccessible(true);
            Object value = declaredField.get(tenant);

            if (value == null || (value instanceof String && ((String) value).isEmpty())) {
                missingFields.add(field);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    if (!missingFields.isEmpty()) {
        throw new IllegalArgumentException("Missing required fields: " + String.join(", ", missingFields));
    }
}

@Override
public GenericResult<String> updateTenant(List<Tenant> updates) {
    GenericResult<String> result = new GenericResult<>();
    long startTime = System.currentTimeMillis();

    try {
        for (Tenant update : updates) {
            int id = update.getId();
            //  tenant.setUpdateDate(LocalDateTime.now());
            Query query = new Query(Criteria.where("id").is(id));

            if (mongoTemplate.exists(query, Tenant.class)) {
                Update updateQuery = new Update();

                String[] requiredFields = {
                        "customerName", "customerEmail", "customerPhone",
                        "adminName", "adminEmail", "customerNumber",
                        "parentCompany", "country", "street", "city",
                        "state", "zip", "taxId", "website"
                };

                List<String> missingFields = new ArrayList<>();

                for (String field : requiredFields) {
                    Object fieldValue = getFieldValue(update, field);

                    if (fieldValue == null || (fieldValue instanceof String && ((String) fieldValue).isEmpty())) {
                        missingFields.add(field);
                    } else {

                        updateQuery.set(field, fieldValue);
                        

                    }
                    LocalDateTime currentDateTime = LocalDateTime.now();
                     updateQuery.set("UpdateDate", currentDateTime);
                    //   updateQuery.set("UpdateBy", getCurrentAuditor().toString());
                    String updatedBy = getCurrentAuditor().orElse("Srii").toString();
                    updateQuery.set("UpdateBy", updatedBy);
                }

                if (!missingFields.isEmpty()) {
                    result.setSuccess(false);
                    result.setMessage("Missing required fields for updating tenants with ID " + id + ": " +
                            String.join(", ", missingFields));
                    return result;
                }

                mongoTemplate.updateFirst(query, updateQuery, Tenant.class);
                logger.info("Tenants with ID {} updated successfully.", id);
            } else {
                result.setSuccess(false);
                result.setMessage("Tenants with ID " + id + " not found for update.");
                return result;
            }
        }

        result.setSuccess(true);
        result.setMessage("Tenants updated successfully.");
    } catch (Exception e) {
        handleException(result, e, "Error updating tenants");
        result.setSuccess(false);
        result.setMessage("Error updating tenants: " + e.getMessage());
        result.setErrorDetails(e.getMessage());
        logger.error("Error updating tenants Error: {}", e.getMessage(), e);
    }

    long endTime = System.currentTimeMillis();
    logger.info("DB update Execution Time: {} ms", endTime - startTime);
    return result;
}
private Object getFieldValue(Tenant tenant, String fieldName) throws NoSuchFieldException, IllegalAccessException {
    Field field = Tenant.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(tenant);
}

	@Override
public GenericResult<String> deleteTenant(List<Integer> delIds) {
    GenericResult<String> genericResult = new GenericResult<>();
    long startTime = System.currentTimeMillis();

    try {
        if (delIds.isEmpty()) {
            genericResult.setSuccess(false);
            genericResult.setMessage("Empty list provided. Please provide valid IDs for deletion.");
            return genericResult;
        }

        for (Integer id : delIds) {
            Query query = new Query(Criteria.where("id").is(id));
            if (mongoTemplate.exists(query, Tenant.class)) {
                mongoTemplate.remove(query, Tenant.class);
                genericResult.setSuccess(true);
                logger.info("Tenant deleted: " + id);
            } else {
                genericResult.setSuccess(false);
                genericResult.setMessage("Tenant with ID " + id + " not found for deletion.");
                return genericResult;
            }
        }
        genericResult.setMessage("Tenant(s) deleted");
        return genericResult;
    } catch (Exception e) {
        handleException(genericResult, e, "Error deleting Tenant(s)");
        logger.error("Error deleting tenant(s): " + e.getMessage());
    } finally {
        long endTime = System.currentTimeMillis();
        logger.info("DB deletion Execution Time: {} ms", endTime - startTime);
    }

    return genericResult;
}

private <T> void handleException(GenericResult<T> result, Exception e, String errorMessage) {
    logger.error(errorMessage, e);
    result.setSuccess(false);
    result.setMessage(errorMessage + ": " + e.getMessage());
}
}

