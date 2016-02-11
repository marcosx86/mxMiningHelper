package net.m21xx.mc.mininghelper;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class mxMiningHelperEventListener implements Listener {
	
	private mxMiningHelper vInstance;
	
	public class NotifyThread extends Thread {
		private mxMiningHelper vInstance;
		private Player vPlayer;
		private Block vBlock;
		private int vLight;
		private ArrayList<Block> vBlocks;
		
	    public NotifyThread(mxMiningHelper plugin, Player player, Block block, int light) {
	    	this.vInstance = plugin;
	    	this.vPlayer = player;
	    	this.vBlock = block;
	    	this.vLight = light;
	    }
	    
	    public void run() {
			Material blocktype = vInstance.canAnnounce(vBlock);
			
			ChatColor prefix = ChatColor.WHITE;
			String item = null;

			if (blocktype == Material.EMERALD_ORE) {
				prefix = ChatColor.GREEN;
				item = "emerald";
			}			
			if (blocktype == Material.DIAMOND_ORE) {
				prefix = ChatColor.AQUA;
				item = "diamond";
			}
			if (blocktype == Material.GOLD_ORE) {
				prefix = ChatColor.GOLD;
				item = "gold";
			}
			if (blocktype == Material.IRON_ORE) {
				prefix = ChatColor.GRAY;
				item = "iron";
			}
			if (blocktype == Material.LAPIS_ORE) {
				prefix = ChatColor.BLUE;
				item = "lapis lazuli";
			}
			if (blocktype == Material.REDSTONE_ORE || blocktype == Material.GLOWING_REDSTONE_ORE) {
				prefix = ChatColor.RED;
				item = "redstone";
			}
			if (vBlock.getType() == Material.COAL_ORE) {
				prefix = ChatColor.GRAY;
				item = "coal";
			}
			if (vInstance.ExtraBlks.size() > 0 && vInstance.ExtraBlks.contains(vBlock.getType().name())) {
				prefix = ChatColor.YELLOW;
				item = vBlock.getType().toString();
			}
			
			if (item == null)
				return;
				    	
			vBlocks = new ArrayList<Block>();
			String name = (vInstance.vCfgUseNickname ? vPlayer.getDisplayName() : vPlayer.getName());			
			getAllRelative(vBlock, vPlayer);			
			String lMsg = vInstance.vCfgMsgMining.replace("%ply", name).replace("%amt", String.valueOf(vBlocks.size())).replace("%blk", item).replace("%vis", String.valueOf(vLight));
			vPlayer.sendMessage(prefix + "[MH] " + lMsg);
			vBlocks.clear();
			vBlocks = null;
	    }

		private void getAllRelative(Block pBlock, Player pPlayer) {
			pBlock.setMetadata("Found", new FixedMetadataValue(vInstance, true));
			vBlocks.add(pBlock);

			BlockFace[] lFaces = {BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};
			
			for (BlockFace face : lFaces) {
				Block rel = pBlock.getRelative(face);
				if (rel != null && !vBlocks.contains(rel)) {
					Material mat1 = rel.getType();
					Material mat2 = pBlock.getType();
					if (mat1 == Material.GLOWING_REDSTONE_ORE) mat1 = Material.REDSTONE_ORE;
					if (mat2 == Material.GLOWING_REDSTONE_ORE) mat2 = Material.REDSTONE_ORE;

					if (mat1 == mat2) {
						if (vInstance.canAnnounce(rel) != null) {
							getAllRelative(rel, pPlayer);
						}
					}
				}
			}
		}
	}
	
	public mxMiningHelperEventListener(mxMiningHelper pInstance) {
		vInstance = pInstance;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (!event.isCancelled() && event.getAction() == Action.LEFT_CLICK_BLOCK) {
			Player player = event.getPlayer();
			Block block = event.getClickedBlock();
			
			Block rela = block.getRelative(event.getBlockFace());
			int light = (rela.isEmpty() ? Math.round(((rela.getLightLevel()) & 0xFF) * 100) / 15 : 15);

			
	    	Thread notify = new NotifyThread(vInstance, player, block, light);
			notify.start();
		}
	}
}
