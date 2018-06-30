package daomephsta.buildersdrawers.common.items;

import daomephsta.buildersdrawers.common.BuildersDrawers;
import daomephsta.buildersdrawers.common.blocks.BlockRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = BuildersDrawers.MODID)
public class ItemRegistry
{
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().register(new ItemBuildersDrawers(BlockRegistry.BUILDERS_DRAWERS_FULL1));
		event.getRegistry().register(new ItemBuildersDrawers(BlockRegistry.BUILDERS_DRAWERS_FULL2));
		event.getRegistry().register(new ItemBuildersDrawers(BlockRegistry.BUILDERS_DRAWERS_FULL4));
		event.getRegistry().register(new ItemBuildersDrawers(BlockRegistry.BUILDERS_DRAWERS_HALF2));
		event.getRegistry().register(new ItemBuildersDrawers(BlockRegistry.BUILDERS_DRAWERS_HALF4));
	}
}
