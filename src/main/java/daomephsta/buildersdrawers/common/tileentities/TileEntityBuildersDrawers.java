package daomephsta.buildersdrawers.common.tileentities;

import com.jaquadro.minecraft.chameleon.block.tiledata.TileDataShim;
import com.jaquadro.minecraft.storagedrawers.api.capabilities.IItemRepository;
import com.jaquadro.minecraft.storagedrawers.api.storage.*;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;
import com.jaquadro.minecraft.storagedrawers.capabilities.CapabilityItemRepository;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileEntityBuildersDrawers extends TileEntityDrawers implements ITickable
{	
	private IDrawerGroup group;
	private MaterialNetwork materialNetwork;
	
	public TileEntityBuildersDrawers() {}
	
	public TileEntityBuildersDrawers(int drawerCount)
	{
		this.group = new DrawerGroup(drawerCount);
	}
	
	@Override
	public void update()
	{
		
	}
	
	@Override
	public IDrawerGroup getGroup()
	{
		return group;
	}
	
	public void onBlockPlaced(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack itemStack)
	{
		connectTo(MaterialNetwork.findNetworkFor(this));
	}
	
	public void onBlockBroken(World world, BlockPos pos, IBlockState state)
	{
		/* Invalidate this TE so it isn't included in the pathfinding for
		 * creating any split networks*/
		this.invalidate();
		materialNetwork.disconnect(getPos());
	}
	
	void connectTo(MaterialNetwork network)
	{
		materialNetwork = network.connect(getPos());
	}
	
	public MaterialNetwork getMaterialNetwork()
	{ 
		return materialNetwork;
	}
	
	@Override
	protected void readFromFixedNBT(NBTTagCompound tag)
	{
		super.readFromFixedNBT(tag);
		group = new DrawerGroup(tag.getInteger("drawerCount"));
	}
	
	@Override
	protected NBTTagCompound writeToFixedNBT(NBTTagCompound tag)
	{
		tag = super.writeToFixedNBT(tag);
		tag.setInteger("drawerCount", getGroup().getDrawerCount());
		return tag;
	}
	
	private static final class DrawerGroup extends TileDataShim implements IDrawerGroup
	{
		private final int drawerCount;
		private /*final*/ IItemHandler inventory;
		private /*final*/ IItemRepository itemRepo;
		
		private DrawerGroup(int drawerCount)
		{
			this.drawerCount = drawerCount;
		}

		@Override
		public int getDrawerCount()
		{
			return drawerCount;
		}

		@Override
		public IDrawer getDrawer(int slot)
		{
			return Drawers.DISABLED;
		}

		@Override
		public int[] getAccessibleDrawerSlots()
		{
			return new int[0];
		}
		
		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing)
		{
			return false; //capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY 
					//|| capability == CapabilityItemRepository.ITEM_REPOSITORY_CAPABILITY;
		}
		
		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing)
		{
			if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) 
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
			if (capability == CapabilityItemRepository.ITEM_REPOSITORY_CAPABILITY)
				return CapabilityItemRepository.ITEM_REPOSITORY_CAPABILITY.cast(itemRepo);
			return IDrawerGroup.super.getCapability(capability, facing);
		}

		@Override
		public void readFromNBT(NBTTagCompound tag)
		{
			
		}

		@Override
		public NBTTagCompound writeToNBT(NBTTagCompound tag)
		{
			return null;
		}
	}
}
