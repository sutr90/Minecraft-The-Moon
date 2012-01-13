package net.minecraft.src;

//Exported java file
//Keep in mind that you still need to fill in some blanks
// - ZeuX

public class ModelBuilderBot extends ModelBase {
	public ModelBuilderBot() {
		body = new ModelRenderer(0, 0);
		body.addBox(-5F, -5F, -5F, 10, 10, 10, 0F);
		body.setRotationPoint(0F, 17F, 0F);
		body.rotateAngleX = 0F;
		body.rotateAngleY = -1.5708F;
		body.rotateAngleZ = 0F;
		body.mirror = false;
		head = new ModelRenderer(0, 22);
		head.addBox(-3.5F, -3F, -3.5F, 7, 3, 7, 0F);
		head.setRotationPoint(0F, 12F, 0F);
		head.rotateAngleX = 0F;
		head.rotateAngleY = 0F;
		head.rotateAngleZ = 0F;
		head.mirror = false;
		leftArm = new ModelRenderer(41, 16);
		leftArm.addBox(5F, -4.5F, -4F, 2, 8, 8, 0F);
		leftArm.setRotationPoint(0F, 17F, 0F);
		leftArm.rotateAngleX = 0F;
		leftArm.rotateAngleY = 0F;
		leftArm.rotateAngleZ = 0F;
		leftArm.mirror = true;
		rightArm = new ModelRenderer(41, 0);
		rightArm.addBox(-7F, -4.5F, -4F, 2, 8, 8, 0F);
		rightArm.setRotationPoint(0F, 17F, 0F);
		rightArm.rotateAngleX = 0F;
		rightArm.rotateAngleY = 1.57F;
		rightArm.rotateAngleZ = 0F;
		rightArm.mirror = true;
	}

	public void render(float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5);
		body.render(f5);
		head.render(f5);
		leftArm.render(f5);
		rightArm.render(f5);
	}

	public void setRotationAngles(float f, float f1, float f2, float f3,
			float f4, float f5) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5);
		head.rotateAngleY = f3 / 57.295776F;
		leftArm.rotateAngleY = f3 / 57.295776F;
		rightArm.rotateAngleY = f3 / 57.295776F;
		body.rotateAngleY = f3 / 57.295776F;

		body.rotateAngleX = f4 / 57.295776F;
		// body.rotateAngleZ = f4 / 57.295776F;
	}

	// fields
	public ModelRenderer body;
	public ModelRenderer head;
	public ModelRenderer leftArm;
	public ModelRenderer rightArm;

}
