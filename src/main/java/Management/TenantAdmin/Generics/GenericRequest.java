package Management.TenantAdmin.Generics;

import java.util.Map;

public class GenericRequest<T> {
	private Map<String, T> filters;
	private int page;
	private int size;
	private String sortField;
	private String sortOrder;

}
