package teamroots.embers.world;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import teamroots.embers.Embers;

public class WorldUpdater {
	protected final String SAVE_VERSION_KEY = "EmbersSaveVersion";
	protected final int SAVE_VERSION = 1;
	
	protected Map<String, String> mappings = new HashMap<>();
	{
		//NOTE: In 1.10.2, FMLMissingMappingsEvent only will fire for items and blocks. To handle tile entities in a somewhat more version-agnostic manner, we use WorldUpdater.registerTileEntityWithAlternatives lower down
		
		// Special item and block mappings for things that were removed, renamed in a special way, or replaced with something different
		// Some of these are compatible with Embers Rekindled, but in a few cases you will notice missing IDs, if you are migrating from this Embers version to Embers Rekindled
		mappings.put("golemsEye", "codex");
		mappings.put("knowledgeTable", "display_pedestal");
		mappings.put("glimmerLamp", "archaic_light");
		mappings.put("coreStone", "archaic_tile");
		mappings.put("itemGauge", "item_gauge");
		
		// Fluid block mappings
		mappings.put("moltenIron", "iron");
		mappings.put("moltenGold", "gold");
		mappings.put("moltenLead", "lead");
		mappings.put("moltenCopper", "copper");
		mappings.put("moltenSilver", "silver");
		mappings.put("moltenDawnstone", "dawnstone");
		
		// Relatively noncontroversial item and block mappings, in principle compatible with Embers Rekindled
		mappings.put("advancedEdge", "advanced_edge");
		mappings.put("alchemicWaste", "alchemic_waste");
		mappings.put("alchemyPedestal", "alchemy_pedestal");
		mappings.put("alchemyTablet", "alchemy_tablet");
		mappings.put("ashenBrick", "ashen_brick");
		mappings.put("ashenBrickSlab", "ashen_brick_slab");
		mappings.put("ashenBrickSlabDouble", "ashen_brick_slab_double");
		mappings.put("ashenCloakBoots", "ashen_cloak_boots");
		mappings.put("ashenCloakChest", "ashen_cloak_chest");
		mappings.put("ashenCloakHead", "ashen_cloak_head");
		mappings.put("ashenCloakLegs", "ashen_cloak_legs");
		mappings.put("ashenCloth", "ashen_cloth");
		mappings.put("ashenStone", "ashen_stone");
		mappings.put("ashenStoneSlab", "ashen_stone_slab");
		mappings.put("ashenStoneSlabDouble", "ashen_stone_slab_double");
		mappings.put("ashenTile", "ashen_tile");
		mappings.put("ashenTileSlab", "ashen_tile_slab");
		mappings.put("ashenTileSlabDouble", "ashen_tile_slab_double");
		mappings.put("aspectusCopper", "aspectus_copper");
		mappings.put("aspectusDawnstone", "aspectus_dawnstone");
		mappings.put("aspectusIron", "aspectus_iron");
		mappings.put("aspectusLead", "aspectus_lead");
		mappings.put("aspectusSilver", "aspectus_silver");
		mappings.put("axeClockwork", "axe_clockwork");
		mappings.put("axeCopper", "axe_copper");
		mappings.put("axeDawnstone", "axe_dawnstone");
		mappings.put("axeLead", "axe_lead");
		mappings.put("axeSilver", "axe_silver");
		mappings.put("beamCannon", "beam_cannon");
		mappings.put("beamSplitter", "beam_splitter");
		mappings.put("blendCaminite", "blend_caminite");
		mappings.put("blockCaminiteBrick", "block_caminite_brick");
		mappings.put("blockCaminiteBrickSlab", "block_caminite_brick_slab");
		mappings.put("blockCaminiteBrickSlabDouble", "block_caminite_brick_slab_double");
		mappings.put("blockCopper", "block_copper");
		mappings.put("blockDawnstone", "block_dawnstone");
		mappings.put("blockFurnace", "block_furnace");
		mappings.put("blockLantern", "block_lantern");
		mappings.put("blockLead", "block_lead");
		mappings.put("blockSilver", "block_silver");
		mappings.put("blockTank", "block_tank");
		mappings.put("brickCaminite", "brick_caminite");
		mappings.put("cinderPlinth", "cinder_plinth");
		mappings.put("copperCell", "copper_cell");
		mappings.put("crystalCell", "crystal_cell");
		mappings.put("crystalEmber", "crystal_ember");
		mappings.put("dustAsh", "dust_ash");
		mappings.put("emberActivator", "ember_activator");
		mappings.put("emberBore", "ember_bore");
		mappings.put("emberCartridge", "ember_cartridge");
		mappings.put("emberDetector", "ember_detector");
		mappings.put("emberEmitter", "ember_emitter");
		mappings.put("emberGauge", "ember_gauge");
		mappings.put("emberJar", "ember_jar");
		mappings.put("emberReceiver", "ember_receiver");
		mappings.put("emberRelay", "ember_relay");
		mappings.put("fluidGauge", "fluid_gauge");
		mappings.put("glimmerShard", "glimmer_shard");
		mappings.put("heatCoil", "heat_coil");
		mappings.put("hoeCopper", "hoe_copper");
		mappings.put("hoeDawnstone", "hoe_dawnstone");
		mappings.put("hoeLead", "hoe_lead");
		mappings.put("hoeSilver", "hoe_silver");
		mappings.put("ignitionCannon", "ignition_cannon");
		mappings.put("inflictorGem", "inflictor_gem");
		mappings.put("ingotCopper", "ingot_copper");
		mappings.put("ingotDawnstone", "ingot_dawnstone");
		mappings.put("ingotLead", "ingot_lead");
		mappings.put("ingotSilver", "ingot_silver");
		mappings.put("itemDropper", "item_dropper");
		mappings.put("itemPipe", "item_pipe");
		mappings.put("itemPump", "item_pump");
		mappings.put("itemTransfer", "item_transfer");
		mappings.put("largeTank", "large_tank");
		mappings.put("mechAccessor", "mech_accessor");
		mappings.put("mechCore", "mech_core");
		mappings.put("mechEdge", "mech_edge");
		mappings.put("nuggetCopper", "nugget_copper");
		mappings.put("nuggetDawnstone", "nugget_dawnstone");
		mappings.put("nuggetIron", "nugget_iron");
		mappings.put("nuggetLead", "nugget_lead");
		mappings.put("nuggetSilver", "nugget_silver");
		mappings.put("oreCopper", "ore_copper");
		mappings.put("oreCopper", "ore_copper");
		mappings.put("oreLead", "ore_lead");
		mappings.put("oreLead", "ore_lead");
		mappings.put("oreSilver", "ore_silver");
		mappings.put("oreSilver", "ore_silver");
		mappings.put("pickaxeClockwork", "pickaxe_clockwork");
		mappings.put("pickaxeCopper", "pickaxe_copper");
		mappings.put("pickaxeDawnstone", "pickaxe_dawnstone");
		mappings.put("pickaxeLead", "pickaxe_lead");
		mappings.put("pickaxeSilver", "pickaxe_silver");
		mappings.put("plateCaminite", "plate_caminite");
		mappings.put("plateCaminiteRaw", "plate_caminite_raw");
		mappings.put("plateCopper", "plate_copper");
		mappings.put("plateDawnstone", "plate_dawnstone");
		mappings.put("plateGold", "plate_gold");
		mappings.put("plateIron", "plate_iron");
		mappings.put("plateLead", "plate_lead");
		mappings.put("plateSilver", "plate_silver");
		mappings.put("shardEmber", "shard_ember");
		mappings.put("shovelCopper", "shovel_copper");
		mappings.put("shovelDawnstone", "shovel_dawnstone");
		mappings.put("shovelLead", "shovel_lead");
		mappings.put("shovelSilver", "shovel_silver");
		mappings.put("staffEmber", "staff_ember");
		mappings.put("stairsAshenBrick", "stairs_ashen_brick");
		mappings.put("stairsAshenStone", "stairs_ashen_stone");
		mappings.put("stairsAshenTile", "stairs_ashen_tile");
		mappings.put("stairsCaminiteBrick", "stairs_caminite_brick");
		mappings.put("stampBar", "stamp_bar");
		mappings.put("stampBarRaw", "stamp_bar_raw");
		mappings.put("stampFlat", "stamp_flat");
		mappings.put("stampFlatRaw", "stamp_flat_raw");
		mappings.put("stampPlate", "stamp_plate");
		mappings.put("stampPlateRaw", "stamp_plate_raw");
		mappings.put("stamperBase", "stamper_base");
		mappings.put("stoneEdge", "stone_edge");
		mappings.put("swordCopper", "sword_copper");
		mappings.put("swordDawnstone", "sword_dawnstone");
		mappings.put("swordLead", "sword_lead");
		mappings.put("swordSilver", "sword_silver");
		mappings.put("tinkerHammer", "tinker_hammer");
		mappings.put("wallAshenBrick", "wall_ashen_brick");
		mappings.put("wallAshenStone", "wall_ashen_stone");
		mappings.put("wallAshenTile", "wall_ashen_tile");
		mappings.put("wallCaminiteBrick", "wall_caminite_brick");
	}
	
	// Try id remapping within the current mod
	protected <T extends IForgeRegistryEntry<T>> T tryMapping(MissingMapping missing, GameRegistry.Type type, IForgeRegistry<T> forgeRegistry) {
		if (missing.type != type) {
			return null;
		}
		String oldName = missing.resourceLocation.getResourcePath();
		String newName = mappings.get(oldName);
		if (newName == null) {
			return null;
		}
		
		return forgeRegistry.getValue(new ResourceLocation(Embers.MODID, newName));
	}
	
	// Called by the Embers mod class
	public void onMissingMappings(FMLMissingMappingsEvent event) {
		for (MissingMapping missing : event.get()) {
			{
				Block newBlock = tryMapping(missing, GameRegistry.Type.BLOCK, ForgeRegistries.BLOCKS);
				if (newBlock != null) {
					missing.remap(newBlock);
					continue;
				}
			}
			{
				Item newItem = tryMapping(missing, GameRegistry.Type.ITEM, ForgeRegistries.ITEMS);
				if (newItem != null) {
					missing.remap(newItem);
					continue;
				}
			}
		}
	}

	// Called by RegistryManager
	// GameRegistry.registerTileEntityWithAlternatives allows to convert tile entities over from old worlds with old tile entity IDs in 1.10.2/1.11.2.
	// In 1.12.2, the FMLMissingMappingsEvent was generalized to also support tile entities, so registerTileEntityWithAlternatives should not be used in that version
	public static void registerTileEntityWithAlternatives(Class<? extends TileEntity> tileEntityClass, String id, String... alternatives) {
		GameRegistry.registerTileEntityWithAlternatives(tileEntityClass, id, alternatives);
	}

	// Called by RegistryManager
	// Presumably, EntityList was overhauled/removed some time post-1.10, so we're abstracting this out
	// Note that alternative names must include the mod ID. For example: embers.ancientGolem or embers:ancient_golem
	public static void registerModEntityWithAlternatives(Class<? extends Entity> entityClass, String entityName, int id, Object mod, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates, String...alternatives) {
		EntityRegistry.registerModEntity(entityClass, entityName, id, mod, trackingRange, updateFrequency, sendsVelocityUpdates);
		for (String fullAlternativeMobName : alternatives) {
			EntityList.field_75625_b.put(fullAlternativeMobName, entityClass);
		}
	}
}
