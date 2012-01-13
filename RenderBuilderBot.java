package net.minecraft.src;

public class RenderBuilderBot extends RenderLiving {

	public RenderBuilderBot(ModelBase modelbase, float f) {
		super(modelbase, f);
	}

    public void renderCow(EntityMoonBuilder ebs, double d, double d1, double d2,
            float f, float f1)
    {
        super.doRenderLiving(ebs, d, d1, d2, f, f1);
    }

    public void doRenderLiving(EntityLiving entityliving, double d, double d1, double d2,
            float f, float f1)
    {
        renderCow((EntityMoonBuilder)entityliving, d, d1, d2, f, f1);
    }

    public void doRender(Entity entity, double d, double d1, double d2,
            float f, float f1)
    {
        renderCow((EntityMoonBuilder)entity, d, d1, d2, f, f1);
    }
}