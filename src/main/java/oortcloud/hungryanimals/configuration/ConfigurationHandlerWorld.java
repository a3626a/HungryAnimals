package oortcloud.hungryanimals.configuration;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.blocks.BlockExcreta;
import oortcloud.hungryanimals.blocks.BlockNiterBed;
import oortcloud.hungryanimals.core.handler.WorldEventHandler;

public class ConfigurationHandlerWorld {
	public static Configuration config;

	public static final String CATEGORY_Block = "block";

	public static final String KEY_fermetationProbability = "Fermentation Probability";
	public static final String KEY_erosionProbability = "Erosion Probability";
	public static final String KEY_fertilizationProbability = "Fertilization Probability";
	public static final String KEY_erosionOnHayProbability = "Erosion On Hay Probability";
	public static final String KEY_ripeningProbability = "Ripening Probability";
	public static final String KEY_diseaseProbability = "Disease Probability";
	public static final String KEY_grassProbability = "Grass Probability";

	public static void init(File file) {
		config = new Configuration(file);
		config.load();
	}

	public static void sync() {
		HungryAnimals.logger.info("Configuration: World start");

		HungryAnimals.logger.info("Configuration: Read and Register properties of BlockExcreta");
		BlockExcreta.fermetationProbability = config.get(CATEGORY_Block, KEY_fermetationProbability, BlockExcreta.defualt_fermetationProbability).getDouble();
		BlockExcreta.erosionProbability = config.get(CATEGORY_Block, KEY_erosionProbability, BlockExcreta.defualt_erosionProbability).getDouble();
		BlockExcreta.erosionOnHayProbability = config.get(CATEGORY_Block, KEY_erosionOnHayProbability, BlockExcreta.defualt_erosionOnHayProbability)
				.getDouble();
		BlockExcreta.fertilizationProbability = config.get(CATEGORY_Block, KEY_fertilizationProbability, BlockExcreta.defualt_fertilizationProbability)
				.getDouble();
		BlockExcreta.diseaseProbability = config.get(CATEGORY_Block, KEY_diseaseProbability, BlockExcreta.defualt_diseaseProbability).getDouble();

		HungryAnimals.logger.info("Configuration: Read and Register properties of NiterBed");
		BlockNiterBed.ripeningProbability = config.get(CATEGORY_Block, KEY_ripeningProbability, BlockNiterBed.default_ripeningProbability).getDouble();

		HungryAnimals.logger.info("Configuration: Read and Register properties of Grass Growth");
		WorldEventHandler.grassProbability = config.get(CATEGORY_Block, KEY_grassProbability, WorldEventHandler.default_grassProbability).getDouble();
		config.save();

	}

}
