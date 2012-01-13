package net.minecraft.src;


public class ModelMoonUfo extends ModelBase {
	public ModelMoonUfo() {
		bipedHead = new ModelRenderer(0, 0);
		bipedHead.addBox(-4.5F, -4.5F, -4.5F, 9, 9, 9, 0F);
		bipedHead.setRotationPoint(0F, 8.5F, 0F);

		bipedHead.rotateAngleX = 0F;
		bipedHead.rotateAngleY = 0F;
		bipedHead.rotateAngleZ = 0F;
		bipedHead.mirror = false;

		bipedBody = new ModelRenderer(36, 0);
		bipedBody.addBox(-2F, -3F, -2F, 4, 6, 4, 0F);
		bipedBody.setRotationPoint(0F, 16F, 0F);

		bipedBody.rotateAngleX = 0F;
		bipedBody.rotateAngleY = 0F;
		bipedBody.rotateAngleZ = 0F;
		bipedBody.mirror = false;

		bipedLeftArm = new ModelRenderer(0, 18);
		bipedLeftArm.addBox(-1F, -1F, -1F, 2, 7, 2, 0F);
		bipedLeftArm.setRotationPoint(3F, 14.5F, 0F);

		bipedLeftArm.rotateAngleX = 0F;
		bipedLeftArm.rotateAngleY = 0F;
		bipedLeftArm.rotateAngleZ = 0F;
		bipedLeftArm.mirror = false;

		bipedRightArm = new ModelRenderer(0, 18);
		bipedRightArm.addBox(-1F, -1F, -1F, 2, 7, 2, 0F);
		bipedRightArm.setRotationPoint(-3F, 14.5F, 0F);

		bipedRightArm.rotateAngleX = 0F;
		bipedRightArm.rotateAngleY = 0F;
		bipedRightArm.rotateAngleZ = 0F;
		bipedRightArm.mirror = false;

		bipedRightLeg = new ModelRenderer(8, 20);
		bipedRightLeg.addBox(-1F, -1F, -1F, 2, 5, 2, 0F);
		bipedRightLeg.setRotationPoint(-1F, 20F, 0F);

		bipedRightLeg.rotateAngleX = 0F;
		bipedRightLeg.rotateAngleY = 0F;
		bipedRightLeg.rotateAngleZ = 0F;
		bipedRightLeg.mirror = false;

		bipedLeftLeg = new ModelRenderer(8, 20);
		bipedLeftLeg.addBox(-1F, -1F, -1F, 2, 5, 2, 0F);
		bipedLeftLeg.setRotationPoint(1F, 20F, 0F);

		bipedLeftLeg.rotateAngleX = 0F;
		bipedLeftLeg.rotateAngleY = 0F;
		bipedLeftLeg.rotateAngleZ = 0F;
		bipedLeftLeg.mirror = false;

	}

	public void render(float f, float f1, float f2, float f3, float f4, float f5) {
		setRotationAngles(f, f1, f2, f3, f4, f5);
		bipedHead.render(f5);
		bipedBody.render(f5);
		bipedRightArm.render(f5);
		bipedLeftArm.render(f5);
		bipedRightLeg.render(f5);
		bipedLeftLeg.render(f5);

	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		bipedHead.rotateAngleY = f3 / 57.29578F;
		bipedHead.rotateAngleX = f4 / 57.29578F;
		bipedRightArm.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 2.0F * f1 * 0.5F;
		bipedLeftArm.rotateAngleX = MathHelper.cos(f * 0.6662F) * 2.0F * f1 * 0.5F;
		bipedRightArm.rotateAngleZ = 0.0F;
		bipedLeftArm.rotateAngleZ = 0.0F;
		bipedRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
		bipedLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
		bipedRightLeg.rotateAngleY = 0.0F;
		bipedLeftLeg.rotateAngleY = 0.0F;
		bipedRightArm.rotateAngleY = 0.0F;
		bipedLeftArm.rotateAngleY = 0.0F;
		if (onGround > -9990F) {
			float f6 = onGround;
			bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(f6) * 3.141593F * 2.0F) * 0.2F;
			bipedRightArm.rotateAngleY += bipedBody.rotateAngleY;
			bipedLeftArm.rotateAngleY += bipedBody.rotateAngleY;
			bipedLeftArm.rotateAngleX += bipedBody.rotateAngleY;
			f6 = 1.0F - onGround;
			f6 *= f6;
			f6 *= f6;
			f6 = 1.0F - f6;
			float f7 = MathHelper.sin(f6 * 3.141593F);
			float f8 = MathHelper.sin(onGround * 3.141593F) * -(bipedHead.rotateAngleX - 0.7F)
					* 0.75F;
			bipedRightArm.rotateAngleX -= (double) f7 * 1.2D + (double) f8;
			bipedRightArm.rotateAngleY += bipedBody.rotateAngleY * 2.0F;
			bipedRightArm.rotateAngleZ = MathHelper.sin(onGround * 3.141593F) * -0.4F;
		}

		bipedBody.rotateAngleX = 0.0F;

		bipedRightArm.rotateAngleZ += MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
		bipedLeftArm.rotateAngleZ -= MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
		bipedRightArm.rotateAngleX += MathHelper.sin(f2 * 0.067F) * 0.05F;
		bipedLeftArm.rotateAngleX -= MathHelper.sin(f2 * 0.067F) * 0.05F;
	}

	// fields
	public ModelRenderer bipedHead;
	public ModelRenderer bipedBody;
	public ModelRenderer bipedRightArm;
	public ModelRenderer bipedLeftArm;
	public ModelRenderer bipedRightLeg;
	public ModelRenderer bipedLeftLeg;

}
