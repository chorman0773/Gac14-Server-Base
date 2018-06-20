package gmail.chorman64.gac14.basic.commands;

import java.util.List;

import gmail.chorman64.gac14.basic.permission.IPermission;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.JsonUtils;

public class CommandClearBase extends PermissibleCommandBase {

	public interface TargetInventory{
		IInventory getInventory(EntityPlayerMP src);
		int clearAll(EntityPlayerMP src);
		int clear(EntityPlayerMP src,Item i,int count,int data,NBTTagCompound comp);
		String targetName();
	}

	public enum Vanilla implements TargetInventory{
		INV{

			@Override
			public InventoryPlayer getInventory(EntityPlayerMP src) {
				// TODO Auto-generated method stub
				return src.inventory;
			}
			public int clearAll(EntityPlayerMP src) {
				return src.inventory.clearMatchingItems(null, -1, -1, null);
			}
			public int clear(EntityPlayerMP src,Item i,int count,int data,NBTTagCompound comp) {
				return src.inventory.clearMatchingItems(i, data, count, comp);
			}

			public String targetName() {
				return "Inventory";
			}

		},ENDER{

			@Override
			public InventoryEnderChest getInventory(EntityPlayerMP src) {
				// TODO Auto-generated method stub
				return src.getInventoryEnderChest();
			}
			public int clearAll(EntityPlayerMP src) {
				return clearEnder(getInventory(src),null, -1, -1, null);
			}
			public int clear(EntityPlayerMP src,Item i,int count,int data,NBTTagCompound comp) {
				return clearEnder(getInventory(src),i, data, count, comp);
			}

			public String targetName() {
				return "Ender Chest";
			}

		};
		public abstract IInventory getInventory(EntityPlayerMP src);
	};

	public static final int clearEnder(IInventory e,Item i,int count,int data,NBTTagCompound comp) {
		int c = 0;
		if(i==null)
		{
			for(int j=0;j<e.getSizeInventory();j++) {
				ItemStack stack = e.getStackInSlot(j);
				c += stack.getCount();
			}
			e.clear();
		}else {
			for(int j=0;j<e.getSizeInventory();j++) {
				if(count>0&&c>=count)
					break;
				ItemStack stack = e.getStackInSlot(j);
				if(stack.getItem().equals(i)) {
					if(data>=0&&stack.getMetadata()!=data)
						continue;
					if(comp!=null) {
						if(stack.getTagCompound()==null)
							continue;
						else if(!NBTUtil.areNBTEquals(comp, stack.getTagCompound(), true))
							continue;
					}
					if(stack.getCount()+c >count)
						e.decrStackSize(j, count-c);
					else
						e.removeStackFromSlot(j);

				}
			}
		}

		return c;
	}

	private TargetInventory target;



	public CommandClearBase(IPermission node, String name, String usage,TargetInventory t) {
		super(node, name, usage);
		this.target = t;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		EntityPlayerMP target;
		int c;
		if(args.length==0) {
			target = CommandBase.getCommandSenderAsPlayer(sender.getCommandSenderEntity());
		}else {
			target = CommandBase.getPlayer(server, sender, args[0]);
		}
		if(args.length<2) {
			c = this.target.clearAll(target);
		}else {
			Item i = CommandBase.getItemByText(sender, args[0]);
			int data = args.length>2?CommandBase.parseInt(args[2]):-1;
			int count = args.length>3?CommandBase.parseInt(args[2]):-1;
			NBTTagCompound comp;
			try {
				comp = args.length>4?JsonToNBT.getTagFromJson(args[4]):null;
			} catch (NBTException e) {
				comp = null;
			}
			c = this.target.clear(target, i, count, data, comp);
		}
		if(c<=0)
			CommandBase.notifyCommandListener(sender, this, String.format("Could not clear the %s of %s, no items to remove.", target.getName(),this.target.targetName()));
		else
			CommandBase.notifyCommandListener(sender, this, String.format("Cleared the %s of %s, removing %d", target.getName(),this.target.targetName(),c));
	}

}
