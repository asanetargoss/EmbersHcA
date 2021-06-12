package teamroots.embers.compat.thaumcraft;

import java.awt.Color;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import teamroots.embers.Embers;
import teamroots.embers.compat.RegistryManagerCompat;

public class FluidMoltenQuicksilver extends Fluid {
	public FluidMoltenQuicksilver() {
		super("quicksilver",new ResourceLocation(Embers.MODID+":blocks/molten_quicksilver_still"),new ResourceLocation(Embers.MODID+":blocks/molten_quicksilver_flowing"));
		setViscosity(6000);
		setDensity(2000);
		setLuminosity(15);
		setTemperature(900);
		setBlock(RegistryManagerCompat.ThaumcraftCompat.block_molten_quicksilver);
		setUnlocalizedName("quicksilver");
	}
	
	@Override
	public int getColor(){
		return Color.WHITE.getRGB();
	}
}
