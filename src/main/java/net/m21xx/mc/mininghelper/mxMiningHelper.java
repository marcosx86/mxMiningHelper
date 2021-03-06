package net.m21xx.mc.mininghelper;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import net.m21xx.mc.mininghelper.commands.mxMiningHelperMainCommand;

public class mxMiningHelper extends JavaPlugin {
	public CommandExecutor vCmdMain = new mxMiningHelperMainCommand(this);
	public Listener vListener = new mxMiningHelperEventListener(this);
	
	public boolean vCfgUseNickname;
	
	public String vCfgMsgMining;
	
	public List<Material> vBlocks;
	public List<String> ExtraBlks;
	
	@Override
	public void onEnable() {
		loadConfigurations();
		
		getCommand("mininghelper").setExecutor(vCmdMain);
		
    	getServer().getPluginManager().registerEvents(vListener, this);
	}
    
	public Material canAnnounce(Block block) {
		Material blockType = block.getType();

		if (!vBlocks.contains(blockType))
			return null;

		return blockType;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T parseConfig(String path, T def) {
		FileConfiguration config = getConfig();
		T rval = (T) config.get(path, def);
		config.set(path, rval);
		return rval;
	}
	
	private void loadConfigurations() {
		reloadConfig();
    	vCfgUseNickname = parseConfig("use_nicknames", false);
    	
    	vCfgMsgMining = parseConfig("messages.found", "%ply found %amt %blk(s) (Visibility: %vis%)");
    	
		vBlocks = new ArrayList<Material>();
		if (parseConfig("ores.emerald", true))
			vBlocks.add(Material.EMERALD_ORE);
		if (parseConfig("ores.diamonds", true))
			vBlocks.add(Material.DIAMOND_ORE);
		if (parseConfig("ores.gold", true))
			vBlocks.add(Material.GOLD_ORE);
		if (parseConfig("ores.iron", true))
			vBlocks.add(Material.IRON_ORE);
		if (parseConfig("ores.coal", true))
			vBlocks.add(Material.COAL_ORE);
		if (parseConfig("ores.lapis", true))
			vBlocks.add(Material.LAPIS_ORE);
		if (parseConfig("ores.redstone", true)) {
			vBlocks.add(Material.GLOWING_REDSTONE_ORE);
			vBlocks.add(Material.REDSTONE_ORE);
		}
    	ExtraBlks = parseConfig("Extra_Blocks", new ArrayList<String>());
    	
    	saveConfig();
	}
}
