package daomephsta.buildersdrawers.common.blocks;

import com.jaquadro.minecraft.storagedrawers.api.storage.EnumBasicDrawer;

import daomephsta.buildersdrawers.common.BuildersDrawers;
import daomephsta.buildersdrawers.common.tileentities.TileEntityBuildersDrawers;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(BuildersDrawers.MODID)
@Mod.EventBusSubscriber(modid = BuildersDrawers.MODID)
public class BlockRegistry
{
	public static final BlockBuildersDrawers BUILDERS_DRAWERS_FULL1 = null;
	public static final BlockBuildersDrawers BUILDERS_DRAWERS_FULL2 = null;
	public static final BlockBuildersDrawers BUILDERS_DRAWERS_FULL4 = null;
	public static final BlockBuildersDrawers BUILDERS_DRAWERS_HALF2 = null;
	public static final BlockBuildersDrawers BUILDERS_DRAWERS_HALF4 = null;

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		for(EnumBasicDrawer drawerType : EnumBasicDrawer.values())
		{
			event.getRegistry().register(new BlockBuildersDrawers(drawerType));
		}
		GameRegistry.registerTileEntity(TileEntityBuildersDrawers.class, new ResourceLocation(BuildersDrawers.MODID, "builders_drawer"));
	}
}
