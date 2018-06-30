package daomephsta.buildersdrawers.client;

import java.util.ArrayList;
import java.util.List;

import com.jaquadro.minecraft.chameleon.resources.register.DefaultRegister;
import com.jaquadro.minecraft.storagedrawers.block.BlockDrawers;

import daomephsta.buildersdrawers.common.blocks.BlockBuildersDrawers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class ModelRegisterBuildersDrawer extends DefaultRegister<Block>
{
	public ModelRegisterBuildersDrawer(BlockBuildersDrawers block)
	{
		super(block);
	}

	@Override
	public List<IBlockState> getBlockStates()
	{
		List<IBlockState> validStates = new ArrayList<>();
		for(EnumFacing facing : BlockDrawers.FACING.getAllowedValues())
		{
			validStates.add(getBlock().getDefaultState().withProperty(BlockDrawers.FACING, facing));
		}
		return validStates;
	}

	@Override
	public IBakedModel getModel(IBlockState state, IBakedModel existingModel)
	{
		return new ModelBuildersDrawer(existingModel);
	}

	@Override
	public IBakedModel getModel(ItemStack stack, IBakedModel existingModel)
	{
		return new ModelBuildersDrawer(existingModel);
	}
}