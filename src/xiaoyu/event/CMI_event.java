package xiaoyu.event;

import com.Zrips.CMI.events.CMIUserBalanceChangeEvent;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xiaoyu.tax;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static xiaoyu.tax.economy;

public class CMI_event implements Listener {
    File playertax = new File(tax.getInstance().getDataFolder(), "totalTax.yml");
    File log = new File(tax.getInstance().getDataFolder(), "log.yml");
    YamlConfiguration config = YamlConfiguration.loadConfiguration(playertax);
    YamlConfiguration logs = YamlConfiguration.loadConfiguration(log);
    File countrytax = new File(tax.getInstance().getDataFolder(), "countryTax.yml");
    YamlConfiguration ct = YamlConfiguration.loadConfiguration(countrytax);
    File countrytaxplayer = new File(tax.getInstance().getDataFolder(), "countryTaxPlayer.yml");
    YamlConfiguration ctp = YamlConfiguration.loadConfiguration(countrytaxplayer);
    Date date = new Date();
    DecimalFormat df = new DecimalFormat("#.###%");
    public void easy(String country,String FullCountry,double amount,Player p,String time,double newBalance){
        String FormatTax = df.format(tax.getInstance().getConfig().getDouble(country));
        double Tax = tax.getInstance().getConfig().getDouble(country);
        if (Tax == 0){
            p.sendMessage("§7[ " + FullCountry + " §7]" + " 你国国王未收税~");
            return;
        }
        double taxAmount = amount * Tax;
        String countrymsg = "§7[ " + FullCountry + " §7] §a%player% 收到了 %receive% 金币 , 扣除了 %tax% 金币作为税收。 结余 %left% 余额 %money% 税率 %taxRate%"
                .replaceAll("%player%",p.getName())
                .replaceAll("%receive%", String.valueOf(amount)).replaceAll("%tax%", String.valueOf(taxAmount))
                .replaceAll("%left%", String.valueOf(amount-taxAmount))
                .replaceAll("%money%", String.valueOf(newBalance-taxAmount))
                .replaceAll("%taxRate%", FormatTax);
        economy.withdrawPlayer(p, taxAmount); // 从玩家账户中扣除税收
        p.sendMessage(countrymsg);
        ct.set(country, ct.getDouble(country)+taxAmount);
        logs.set(time,countrymsg);
        ctp.set(country+"."+p.getName(), ctp.getDouble(p.getName())+taxAmount);
        try {
            logs.save(log);
            ct.save(countrytax);
            ctp.save(countrytaxplayer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void onMoneyReceived(CMIUserBalanceChangeEvent event) throws IOException {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = formatter.format(date);
        Player p = event.getUser().getPlayer();
        //p.sendMessage("开始就爆了");
        double oldBalance = event.getFrom(); //原来的钱
        double newBalance = event.getTo(); //现在的钱
        double amount = newBalance - oldBalance; //拿到的钱
        int x=0;
        int y;
        //格式化数字 留一位小数
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(1);
        // 先写免税权 然后看人多少钱 然后看获得多少钱 最后判断会员权限
        if(oldBalance<=newBalance){
            if (!p.hasPermission("tax.bypass")){
                for (x = 100; x>0 ; x--) {
                    double money = tax.getInstance().getConfig().getDouble("tax"+x+".money");
                    double receive = tax.getInstance().getConfig().getDouble("tax"+x+".receive");
                    if (tax.getInstance().getConfig().contains("tax" + x)) {
                        //p.sendMessage("进x循环");
                        if (oldBalance >= money){
                            if (amount >= receive && amount > 0){
                                if (p.hasPermission("hzg.tax.hand")){
                                    easy("huozhiguo","§c火之国",amount,p,time,newBalance);
                                }else if (p.hasPermission("gzg.tax.hand")){
                                    easy("guangzhiguo","§e光之国",amount,p,time,newBalance);
                                }else if (p.hasPermission("aili.tax.hand")){
                                    easy("aili","§a爱丽菲莉娅斯",amount,p,time,newBalance);
                                }else if (p.hasPermission("tkc.tax.hand")){
                                    easy("tiankongcheng","§b天空城",amount,p,time,newBalance);
                                }
                                //p.sendMessage("马上进循环了");
                                for(y = 100; y >0; y--){
                                    if (tax.getInstance().getConfig().contains("tax" + x+".taxRate"+y) && tax.getInstance().getConfig().contains("tax" + x+".taxMsg"+y)) {
                                        if (p.hasPermission("tax."+y)){
                                            String TA = "tax"+x+".taxRate"+y;
                                            double tax1 = tax.getInstance().getConfig().getDouble(TA);
                                            String MS = "tax"+x+".msg"+y;
                                            String msg = tax.getInstance().getConfig().getString(MS);
                                            String FormatTax = df.format(tax1);
                                            double taxAmount = amount * tax1; // 计算税额

                                            economy.withdrawPlayer(p, taxAmount); // 从玩家账户中扣除税收
                                            String msg1 = msg.replaceAll("&","§").replaceAll("%player%",p.getName())
                                                    .replaceAll("%receive%", String.valueOf(amount)).replaceAll("%tax%", String.valueOf(taxAmount))
                                                    .replaceAll("%left%", String.valueOf(nf.format(amount-taxAmount)))
                                                    .replaceAll("%money%", String.valueOf(nf.format(newBalance-taxAmount)))
                                                    .replaceAll("%taxRate%", FormatTax);
                                            p.sendMessage(msg1);
                                            config.set(p.getName(), config.getDouble(p.getName())+taxAmount);
                                            config.save(playertax);
                                            logs.set(time,msg1);
                                            logs.save(log);
                                            return;
                                        }
                                    }
                                }
                                double tax1 = tax.getInstance().getConfig().getDouble("tax"+x+".defaultRate");
                                String FormatTax = df.format(tax1);
                                String msg = tax.getInstance().getConfig().getString("tax"+x+".defaultMsg");
                                double taxAmount = amount * tax1; // 计算税额
                                economy.withdrawPlayer(p, taxAmount); // 从玩家账户中扣除税收
                                String msg1 = msg.replaceAll("&","§").replaceAll("%player%",p.getName())
                                        .replaceAll("%receive%", String.valueOf(amount)).replaceAll("%tax%", String.valueOf(taxAmount))
                                        .replaceAll("%left%", String.valueOf(nf.format(amount-taxAmount)))
                                        .replaceAll("%money%", String.valueOf(nf.format(newBalance-taxAmount)))
                                        .replaceAll("%taxRate%", FormatTax);
                                p.sendMessage(msg1);
                                config.set(p.getName(), config.getDouble(p.getName())+taxAmount);
                                config.save(playertax);
                                logs.set(time,msg1);
                                logs.save(log);
                                return;
                            }
                        }
                    }
                    }
                }else{
                if (config.getBoolean("AvoidTax")) {
                    p.sendMessage("§a§l您有免税权 本次交易不收取税!");
                }
            }

            }
        }
    }