package xiaoyu.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import xiaoyu.tax;

import java.io.File;
import java.io.IOException;

public class huozhiguo implements CommandExecutor {
    File countrytax = new File(tax.getInstance().getDataFolder(), "countryTax.yml");
    YamlConfiguration ct = YamlConfiguration.loadConfiguration(countrytax);
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Plugin plugin = tax.getPlugin(tax.class);

        if (commandSender.hasPermission("huozhiguo.tax")){
            double st1 = Double.parseDouble(strings[1]);
            if (strings.length == 2 && strings[0].equals("tax")){
                // alli tax 0.1
                plugin.getConfig().set("huozhiguo",st1);
                plugin.saveConfig();
                commandSender.sendMessage("§7[§a火之国§7] §d税已设为"+ st1);
            }
        }else{
            commandSender.sendMessage("§4§l你不是火之国的国王!");
        }
        if (commandSender.hasPermission("huozhiguo.look")){
            if (strings.length == 1 && strings[0].equals("look")){
                // huozhiguo look
                double st1 = plugin.getConfig().getDouble("huozhiguo");
                double st2 = ct.getDouble("huozhiguo");
                commandSender.sendMessage("§7[§a火之国§7] §d当前税率为"+ st1);
                commandSender.sendMessage("§7[§a火之国§7] §d当前总国税"+ st2);
            }
        }else{
            commandSender.sendMessage("§4§l你不是火之国的子民!");
        }
        if (commandSender.hasPermission("tax.admin")){
            if (strings.length == 2 && strings[0].equals("set")){
                // huozhiguo set qian
                double st1 = Double.parseDouble(strings[1]);
                ct.getDouble("huozhiguo", st1);
                try {
                    ct.save("countrytax");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                ct.set("huozhiguo", st1);
                commandSender.sendMessage("§7[§a火之国§7] §d国税设置为"+ st1);
            }
        }else{
            commandSender.sendMessage("无权限");
        }
        return false;
    }
}
