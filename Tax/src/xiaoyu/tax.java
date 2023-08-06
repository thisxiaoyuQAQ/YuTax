package xiaoyu;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import xiaoyu.command.reload;
import xiaoyu.metrics.Metrics;

import java.io.File;

public class tax extends JavaPlugin {

    public static Economy economy;
    private static tax instance;
    public static tax getInstance() {return instance;}

    @Override
    public void onEnable() {
        instance =this;
        Bukkit.getPluginManager().registerEvents(new event(), this);
        this.getCommand("tax").setExecutor(new reload());
        Metrics metrics = new xiaoyu.metrics.Metrics(this, 19361);
        this.getConfig().options().copyDefaults();
        this.saveDefaultConfig();

        // 检查Vault是否可用
        if (!setupEconomy()) {
            getLogger().severe("Vault未找到或未配置经济插件");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        File uptax = new File(tax.getInstance().getDataFolder(), "totalTax.yml");
        if (!uptax.exists()){
            tax.getInstance().saveResource("totalTax.yml",false);
        }
        File log = new File(tax.getInstance().getDataFolder(), "log.yml");
        if (!log.exists()){
            tax.getInstance().saveResource("log.yml",false);
        }

        Bukkit.getConsoleSender().sendMessage("§b================================");
        Bukkit.getConsoleSender().sendMessage("§b[YuTax]税收插件已加载");
        Bukkit.getConsoleSender().sendMessage("§b 作者:小雨        QQ:3066156386");
        Bukkit.getConsoleSender().sendMessage("§b   强烈建议添加反馈群:426996480");
        Bukkit.getConsoleSender().sendMessage("§b爱发电 https://afdian.net/@ixiaoyu");
        Bukkit.getConsoleSender().sendMessage("§b================================");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }

        economy = rsp.getProvider();
        return economy != null;
    }
    public void onDisable(){
        Bukkit.getConsoleSender().sendMessage("§b[YuTax]税收插件已卸载");
    }


}