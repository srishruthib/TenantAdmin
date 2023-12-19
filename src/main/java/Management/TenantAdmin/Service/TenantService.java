package Management.TenantAdmin.Service;

import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import Management.TenantAdmin.Entity.Tenant;
import Management.TenantAdmin.Generics.GenericResult;

public interface TenantService {
	GenericResult<Tenant> createTenant(List<Tenant> newTenants);

	GenericResult<String> updateTenant(List<Tenant> updates);

	GenericResult<Page<Tenant>> getTenants(Map<String, Object> tenants, int page, int size, String sortField,
			String sortOrder);

	GenericResult<String> deleteTenant(List<Integer> delIds);

}
