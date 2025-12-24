package xiaoyu;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import xiaoyu.command.*;
import xiaoyu.event.CMI_event;
import xiaoyu.metrics.Metrics;

import java.io.File;

public class tax extends JavaPlugin {

    public static Economy economy;
    private static tax instance;
    public static tax getInstance() {return instance;}

    @Override
    public void onEnable() {
        instance =this;
        Bukkit.getPluginManager().registerEvents(new CMI_event(), this);
        this.getCommand("tax").setExecutor(new reload());
        this.getCommand("aili").setExecutor(new aili());
        this.getCommand("gzg").setExecutor(new guangzhiguo());
        this.getCommand("tkc").setExecutor(new tiankongcheng());
        this.getCommand("hzg").setExecutor(new huozhiguo());
        Metrics metrics = new xiaoyu.metrics.Metrics(this, 19361);
        this.getConfig().options().copyDefaults();
        this.saveDefaultConfig();

        // 检查Vault是否可用
        if (!setupEconomy()) {
            getLogger().severe("Vault未找到或未配置经济插件");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        File playertax = new File(tax.getInstance().getDataFolder(), "totalTax.yml");
        if (!playertax.exists()){
            tax.getInstance().saveResource("totalTax.yml",false);
        }
        File log = new File(tax.getInstance().getDataFolder(), "log.yml");
        if (!log.exists()){
            tax.getInstance().saveResource("log.yml",false);
        }
        File ct = new File(tax.getInstance().getDataFolder(), "countryTax.yml");
        if (!ct.exists()){
            tax.getInstance().saveResource("countryTax.yml",false);
        }
        File ctp = new File(tax.getInstance().getDataFolder(), "countryTaxPlayer.yml");
        if (!ctp.exists()){
            tax.getInstance().saveResource("countryTaxPlayer.yml",false);
        }
        Bukkit.getConsoleSender().sendMessage("§b================================");
        Bukkit.getConsoleSender().sendMessage("§b[YuTax]税收插件已加载");
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