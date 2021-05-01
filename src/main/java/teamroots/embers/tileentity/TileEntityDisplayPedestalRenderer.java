package teamroots.embers.tileentity;

import static teamroots.embers.util.ItemUtil.EMPTY_ITEM_STACK;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import teamroots.embers.util.ItemUtil;

public class TileEntityDisplayPedestalRenderer extends TileEntitySpecialRenderer {
	RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
	Random random = new Random();
	public TileEntityDisplayPedestalRenderer(){
		super();
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage){
		if (tile instanceof TileEntityDisplayPedestal){
			TileEntityDisplayPedestal table = (TileEntityDisplayPedestal)tile;
			if (table.inventory.getStackInSlot(0) != EMPTY_ITEM_STACK){
				if (Minecraft.getMinecraft().world != null){
					GlStateManager.pushAttrib();
					GL11.glPushMatrix();
					EntityItem item = new EntityItem(Minecraft.getMinecraft().world,x,y,z,new ItemStack(table.inventory.getStackInSlot(0).getItem(),1,table.inventory.getStackInSlot(0).getMetadata()));
					item.hoverStart = 0;
					item.isCollided = false;
					GL11.glTranslated(x+0.5, y+0.75, z+0.5);
					GL11.glRotated(table.angle+((table.turnRate))*partialTicks, 0, 1.0, 0);
					Minecraft.getMinecraft().getRenderManager().doRenderEntity(item, 0, 0, 0, 0, 0, false);
					GL11.glPopMatrix();
					GlStateManager.popAttrib();
				}
			}
		}
	}
}
