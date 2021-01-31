package oortcloud.hungryanimals.entities.production;

import java.util.function.Function;

import net.minecraft.entity.MobEntity;

abstract class ProductionFactory implements Function<MobEntity, IProduction>, IProductionJEI {

}
