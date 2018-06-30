package daomephsta.buildersdrawers.common.items;

import java.util.List;

import javax.annotation.Nonnull;

import com.jaquadro.minecraft.storagedrawers.StorageDrawers;
import com.jaquadro.minecraft.storagedrawers.api.storage.EnumBasicDrawer;
import com.jaquadro.minecraft.storagedrawers.item.ItemDrawers;
import com.mojang.realmsclient.gui.ChatFormatting;

import daomephsta.buildersdrawers.common.blocks.BlockBuildersDrawers;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBuildersDrawers extends ItemDrawers
{
	public ItemBuildersDrawers(Block block)
	{
		super(block);
		setRegistryName(block.getRegistryName());
		setHasSubtypes(true);
	}

	//Fixes incorrect drawer capacity tooltip
	@Override
	public void addInformation(ItemStack itemStack, World world, List<String> list, ITooltipFlag advanced)
	{
		if (itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("material"))
		{
			String key = itemStack.getTagCompound().getString("material");
			list.add(I18n.format("storagedrawers.material", I18n.format("storagedrawers.material." + key)));
		}

		list.add(I18n.format("storagedrawers.drawers.description", getCapacityForBlock(itemStack)));

		if (itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("tile"))
			list.add(ChatFormatting.YELLOW + I18n.format("storagedrawers.drawers.sealed"));
	}

	private int getCapacityForBlock(@Nonnull ItemStack itemStack)
	{
		EnumBasicDrawer drawerType = ((BlockBuildersDrawers) block).getDrawerType();
		switch (drawerType)
		{
		case FULL1 :
			return StorageDrawers.config.getBlockBaseStorage("fulldrawers1");
		case FULL2 :
			return StorageDrawers.config.getBlockBaseStorage("fulldrawers2");
		case FULL4 :
			return StorageDrawers.config.getBlockBaseStorage("fulldrawers4");
		case HALF2 :
			return StorageDrawers.config.getBlockBaseStorage("halfdrawers2");
		case HALF4 :
			return StorageDrawers.config.getBlockBaseStorage("halfdrawers4");
		default :
			return 0;
		}
	}
}
