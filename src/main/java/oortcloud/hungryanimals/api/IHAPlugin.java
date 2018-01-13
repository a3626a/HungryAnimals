package oortcloud.hungryanimals.api;

public interface IHAPlugin {
	
	public String getJsonInjectionPath();
	
	public boolean registerAIs(IAIRegistry registry);
	public boolean registerGrassGenerators(IGrassGeneratorRegistry registry);
	public boolean registerLootTables(ILootTableRegistry registry);
	public boolean registerAttributes(IAttributeRegistry registry);
	
}
