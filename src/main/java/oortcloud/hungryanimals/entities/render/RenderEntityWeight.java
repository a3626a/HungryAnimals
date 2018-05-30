package oortcloud.hungryanimals.entities.render;

import javax.annotation.Nullable;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.ResourceLocation;
import oortcloud.hungryanimals.entities.attributes.ModAttributes;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;

public class RenderEntityWeight extends Render<EntityAnimal> {

	protected Render<EntityAnimal> originalRender;

	public RenderEntityWeight(Render<EntityAnimal> render, RenderManager renderManager) {
		super(renderManager);
		this.originalRender = render;
	}

	public void setRenderOutlines(boolean renderOutlinesIn) {
		originalRender.setRenderOutlines(renderOutlinesIn);
	}

	public boolean shouldRender(EntityAnimal livingEntity, ICamera camera, double camX, double camY, double camZ) {
		return originalRender.shouldRender(livingEntity, camera, camX, camY, camZ);
	}

	/**
	 * Renders the desired {@code T} type Entity.
	 */
	public void doRender(EntityAnimal entity, double x, double y, double z, float entityYaw, float partialTicks) {
		double ratio = getRatio(entity);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.scale(ratio, ratio, ratio);
		originalRender.doRender(entity, 0, 0, 0, entityYaw, partialTicks);
		GlStateManager.popMatrix();
	}

	public void bindTexture(ResourceLocation location) {
		originalRender.bindTexture(location);
	}

	/**
	 * Renders the entity's shadow and fire (if its on fire). Args: entity, x, y, z, yaw, partialTickTime
	 */
	public void doRenderShadowAndFire(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
		double ratio = getRatio(entity);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.scale(ratio, ratio, ratio);
		originalRender.doRenderShadowAndFire(entity, 0, 0, 0, yaw, partialTicks);
		GlStateManager.popMatrix();
	}

	/**
	 * Returns the font renderer from the set render manager
	 */
	public FontRenderer getFontRendererFromRenderManager() {
		return originalRender.getFontRendererFromRenderManager();
	}

	public RenderManager getRenderManager() {
		return originalRender.getRenderManager();
	}

	public boolean isMultipass() {
		return originalRender.isMultipass();
	}

	public void renderMultipass(EntityAnimal entity, double x, double y, double z, float yaw, float partialTicks) {
		double ratio = getRatio(entity);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.scale(ratio, ratio, ratio);
		originalRender.renderMultipass(entity, 0, 0, 0, yaw, partialTicks);
		GlStateManager.popMatrix();
	}

	@Override
	@Nullable
	protected ResourceLocation getEntityTexture(EntityAnimal entity) {
		return null;
	}

	public static double getRatio(Entity entity) {
		if (!(entity instanceof EntityAnimal)) {
			return 1;
		}
		EntityAnimal animal = (EntityAnimal)entity;
		
		ICapabilityHungryAnimal cap = animal.getCapability(ProviderHungryAnimal.CAP, null);

		if (cap != null) {
			int age;
			if (animal.getEntityWorld() == null) {
				age = 0;
			} else {
				age = animal.getGrowingAge();
			}
			
			double standardWeight;
			if (age < 0) {
				return 1;
			} else {
				standardWeight = animal.getEntityAttribute(ModAttributes.hunger_weight_normal).getAttributeValue();
			}
			return Math.pow(cap.getWeight() / standardWeight, 1 / 3.0);
		} else {
			return 1;
		}
	}

}
