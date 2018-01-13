package oortcloud.hungryanimals.api.ha;

import oortcloud.hungryanimals.api.HAPlugin;
import oortcloud.hungryanimals.api.IAIRegistry;
import oortcloud.hungryanimals.api.IAttributeRegistry;
import oortcloud.hungryanimals.api.IGrassGeneratorRegistry;
import oortcloud.hungryanimals.api.IHAPlugin;
import oortcloud.hungryanimals.api.ILootTableRegistry;

@HAPlugin
public class PluginHungryAnimals implements IHAPlugin {

	@Override
	public String getJsonInjectionPath() {
		return "/assets/hungryanimals/config";
	}

	@Override
	public boolean registerAIs(IAIRegistry registry) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean registerGrassGenerators(IGrassGeneratorRegistry registry) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean registerLootTables(ILootTableRegistry registry) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean registerAttributes(IAttributeRegistry registry) {
		// TODO Auto-generated method stub
		return false;
	}

}
