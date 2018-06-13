package oortcloud.hungryanimals.entities.production;

import java.util.function.Function;

import net.minecraft.entity.EntityLiving;

abstract class ProductionFactory implements Function<EntityLiving, IProduction>, IProductionJEI {

}
