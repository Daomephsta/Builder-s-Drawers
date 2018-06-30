package daomephsta.buildersdrawers.common.blocks;

import com.jaquadro.minecraft.storagedrawers.api.storage.BlockType;
import com.jaquadro.minecraft.storagedrawers.api.storage.EnumBasicDrawer;
import com.jaquadro.minecraft.storagedrawers.block.BlockDrawers;
import com.jaquadro.minecraft.storagedrawers.block.dynamic.StatusModelData;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;

import daomephsta.buildersdrawers.common.BuildersDrawers;
import daomephsta.buildersdrawers.common.tileentities.TileEntityBuildersDrawers;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBuildersDrawers extends BlockDrawers
{	
	private final StatusModelData statusModel;
	private final EnumBasicDrawer drawerType;

	public BlockBuildersDrawers(EnumBasicDrawer drawerType)
	{
		super(Material.ROCK, "builders_drawers_" + drawerType.getName(),
				BuildersDrawers.MODID + ".buildersDrawers." + drawerType.getName());
		this.drawerType = drawerType;
		this.statusModel = new StatusModelData(3,
				new ResourceLocation(BuildersDrawers.MODID,
				"models/block/dynamic/builders_drawers_" + drawerType.getName() + ".json"));
		setSoundType(SoundType.STONE);
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack itemStack)
	{
		super.onBlockPlacedBy(world, pos, state, entity, itemStack);
		TileEntityBuildersDrawers drawersTE = (TileEntityBuildersDrawers) world.getTileEntity(pos);
		drawersTE.onBlockPlaced(world, pos, state, entity, itemStack);
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		TileEntityBuildersDrawers drawersTE = (TileEntityBuildersDrawers) world.getTileEntity(pos);
		drawersTE.onBlockBroken(world, pos, state);
		super.breakBlock(world, pos, state);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		boolean superResult = super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
		if (!world.isRemote)
		{
			TileEntityBuildersDrawers drawersTE = (TileEntityBuildersDrawers) world.getTileEntity(pos);
			player.sendStatusMessage(new TextComponentString("Connected to " + drawersTE.getMaterialNetwork().getNetworkID()), true);
			return false;
		}
		return superResult; 
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityBuildersDrawers(drawerType.getDrawerCount());
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState();
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, FACING);
	}
	
	@Override
	protected int getDrawerSlot(int drawerCount, int side, float hitX, float hitY, float hitZ)
	{
		if(hitTop(hitY)) return 0;
		return hitLeft(side, hitX, hitZ) ? 1 : 2;
	}
	
	@Override
	public boolean isHalfDepth(IBlockState state)
	{
		// Mojang code is wonderful... 
		if(drawerType == null) return false;
		return drawerType.isHalfDepth();
	}

	@Override
    public boolean isFullCube (IBlockState state) {
        return isOpaqueCube(state);
    }

    @Override
    public boolean isOpaqueCube (IBlockState state) 
    {
        return !isHalfDepth(state);
    }

    @Override
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		if (isHalfDepth(state))
		{
			TileEntityDrawers tile = getTileEntity(world, pos);
			return (tile != null && tile.getDirection() == face.getOpposite().getIndex());
		} else
			return true;
	}

    @Override
    @SuppressWarnings("deprecation")
    public boolean shouldSideBeRendered (IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    	if(isHalfDepth(state))
    	{
    		TileEntityDrawers tile = getTileEntity(blockAccess, pos);
            if (tile != null && tile.getDirection() == side.getIndex())
                return true;
    	}
    	return super.shouldSideBeRendered(state, blockAccess, pos, side);
    }

    @Override
    public boolean causesSuffocation (IBlockState state) 
    {
        return false;
    }
	
	@Override
	public BlockType retrimType()
	{
		// Builder's drawers cannot be retrimmed
		return super.retrimType();
	}
	
	public EnumBasicDrawer getDrawerType()
	{
		return drawerType;
	}
	
	@Override
	public StatusModelData getStatusInfo(IBlockState state)
	{
		return statusModel;
	}
	
	@Override
	public int getDrawerCount(IBlockState state)
	{
		return drawerType.getDrawerCount();
	}
}
