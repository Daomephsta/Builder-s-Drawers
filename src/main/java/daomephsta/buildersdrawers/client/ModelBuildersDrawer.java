package daomephsta.buildersdrawers.client;

import com.jaquadro.minecraft.chameleon.model.PassLimitedModel;
import com.jaquadro.minecraft.chameleon.model.ProxyBuilderModel;
import com.jaquadro.minecraft.storagedrawers.api.storage.EnumBasicDrawer;
import com.jaquadro.minecraft.storagedrawers.block.BlockDrawers;
import com.jaquadro.minecraft.storagedrawers.block.modeldata.DrawerStateModelData;
import com.jaquadro.minecraft.storagedrawers.client.model.component.DrawerDecoratorModel;

import daomephsta.buildersdrawers.common.blocks.BlockBuildersDrawers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;

public class ModelBuildersDrawer extends ProxyBuilderModel
{
	public ModelBuildersDrawer(IBakedModel parent)
	{
		super(parent);
	}

	@Override
	protected IBakedModel buildModel(IBlockState state, IBakedModel parent)
	{
		EnumFacing facing = state.getValue(BlockDrawers.FACING);
		EnumBasicDrawer drawerType = ((BlockBuildersDrawers) state.getBlock()).getDrawerType();

		if (state instanceof IExtendedBlockState)
		{
			IExtendedBlockState xstate = (IExtendedBlockState) state;
			DrawerStateModelData stateModel = xstate
					.getValue(BlockDrawers.STATE_MODEL);

			if (!DrawerDecoratorModel.shouldHandleState(stateModel))
				return new PassLimitedModel(parent,
						BlockRenderLayer.CUTOUT_MIPPED);

			return new DrawerDecoratorModel(parent, xstate, drawerType, facing, stateModel);
		}
		return new PassLimitedModel(parent, BlockRenderLayer.CUTOUT_MIPPED);
	}
}
