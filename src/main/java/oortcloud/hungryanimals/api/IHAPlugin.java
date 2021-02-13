package oortcloud.hungryanimals.api;

public interface IHAPlugin {
	
	public String getJsonInjectionPath();
	
	public void registerAIs(IAIRegistry registry);
	public void registerLootTables(ILootTableRegistry registry);
	public void registerAttributes(IAttributeRegistry registry);
	public void registerProductions(IProductionRegistry registry);
	
}
