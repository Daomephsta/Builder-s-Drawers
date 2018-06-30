package daomephsta.buildersdrawers.common.tileentities;

import java.util.function.Predicate;

import com.jaquadro.minecraft.storagedrawers.api.capabilities.IItemRepository;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class MaterialItemRepository implements IItemRepository
{
	@Override
	public NonNullList<ItemRecord> getAllItems()
	{
		return null;
	}

	@Override
	public ItemStack insertItem(ItemStack stack, boolean simulate, Predicate<ItemStack> predicate)
	{
		return null;
	}

	@Override
	public ItemStack extractItem(ItemStack stack, int amount, boolean simulate, Predicate<ItemStack> predicate)
	{
		return null;
	}
}
