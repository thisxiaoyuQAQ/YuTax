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
            p.sendMessage("/aili/gzg/hzg/tkc tax 0.1 设置税率 - 国王");
            p.sendMessage("/aili/gzg/hzg/tkc look 查看税率和总税额 - 子民");
            p.sendMessage("/aili/gzg/hzg/tkc set 钱 设置总税额 - admin");
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
