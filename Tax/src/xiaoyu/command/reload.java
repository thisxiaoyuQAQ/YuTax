package xiaoyu.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import xiaoyu.tax;

import java.io.File;

public class reload implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Plugin plugin = tax.getPlugin(tax.class);
        CommandSender p = commandSender;
        if (strings.length == 0){
            p.sendMessage("=====税系统=====");
            p.sendMessage("/tax reload 重载插件");
            //p.sendMessage("/tax top 交税榜");
        }
        if (commandSender != null && commandSender.hasPermission("tax.reload"))
            if (strings.length == 1 && strings[0].equals("reload")) {
                plugin.reloadConfig();
                YamlConfiguration.loadConfiguration(new File("totalTax.yml"));
                commandSender.sendMessage("§a[YuTax]重载成功");
            }
        return false;
    }
}
