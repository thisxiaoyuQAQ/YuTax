package xiaoyu;

import com.Zrips.CMI.events.CMIUserBalanceChangeEvent;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static xiaoyu.tax.economy;

public class event implements Listener {
    File uptax = new File(tax.getInstance().getDataFolder(), "totalTax.yml");
    File log = new File(tax.getInstance().getDataFolder(), "log.yml");
    YamlConfiguration config = YamlConfiguration.loadConfiguration(uptax);
    YamlConfiguration logs = YamlConfiguration.loadConfiguration(log);

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

        DecimalFormat df = new DecimalFormat("#.###%");

        // 先写免税权 然后看人多少钱 然后看获得多少钱 最后判断会员权限
        if(oldBalance<=newBalance){
            if (!p.hasPermission("tax.bypass")){
                for (x = 100; x>0 ; x--) {
                    double money = tax.getInstance().getConfig().getDouble("tax"+x+".money");
                    double receive = tax.getInstance().getConfig().getDouble("tax"+x+".receive");
                    if (tax.getInstance().getConfig().contains("tax" + x)) {
                        //p.sendMessage("进x循环");
                        if (oldBalance >= money){
                            //p.sendMessage(oldBalance + "大于限定值"+money);
                            if (amount >= receive){
                                //p.sendMessage("马上进循环了");
                                for(y = 100; y >0; y--){
                                    if (tax.getInstance().getConfig().contains("tax" + x+".taxRate"+y) && tax.getInstance().getConfig().contains("tax" + x+".taxMsg"+y)) {
                                        if (p.hasPermission("tax."+y)){
                                            //p.sendMessage("x="+x+" y="+y);

                                            String TA = "tax"+x+".taxRate"+y;
                                            double tax1 = tax.getInstance().getConfig().getDouble(TA);
                                            String MS = "tax"+x+".msg"+y;
                                            String msg = tax.getInstance().getConfig().getString(MS);
                                            String formattax = df.format(tax1);
                                            double taxAmount = amount * tax1; // 计算税额
                                            economy.withdrawPlayer(p, taxAmount); // 从玩家账户中扣除税收
                                            String msg1 = msg.replaceAll("&","§").replaceAll("%player%",p.getName())
                                                    .replaceAll("%receive%", String.valueOf(amount)).replaceAll("%tax%", String.valueOf(taxAmount))
                                                    .replaceAll("%left%", String.valueOf(amount-taxAmount))
                                                    .replaceAll("%money%", String.valueOf(newBalance-taxAmount))
                                                    .replaceAll("%taxRate%", formattax);
                                            p.sendMessage(msg1);
                                            config.set(p.getName(), config.getDouble(p.getName())+taxAmount);
                                            config.save(uptax);
                                            logs.set(time,msg1);
                                            logs.save(log);
                                            return;
                                        }
                                    }
                                }
                                //p.sendMessage("x="+x+" default");
                                double tax1 = tax.getInstance().getConfig().getDouble("tax"+x+".defaultRate");
                                String formattax = df.format(tax1);
                                String msg = tax.getInstance().getConfig().getString("tax"+x+".defaultMsg");
                                double taxAmount = amount * tax1; // 计算税额
                                economy.withdrawPlayer(p, taxAmount); // 从玩家账户中扣除税收
                                String msg1 = msg.replaceAll("&","§").replaceAll("%player%",p.getName())
                                        .replaceAll("%receive%", String.valueOf(amount)).replaceAll("%tax%", String.valueOf(taxAmount))
                                        .replaceAll("%left%", String.valueOf(amount-taxAmount))
                                        .replaceAll("%money%", String.valueOf(newBalance-taxAmount))
                                        .replaceAll("%taxRate%", formattax);
                                p.sendMessage(msg1);
                                config.set(p.getName(), config.getDouble(p.getName())+taxAmount);
                                config.save(uptax);
                                logs.set(time,msg1);
                                logs.save(log);
                                return;
                            }
                        }

                    }
                    }
                }else{
                p.sendMessage("§a§l您有免税权 本次交易不收取税!");
            }

            }
        }


    }
/* if (oldBalance <= newBalance){
               if (amount >= 50000){
                    if (player.hasPermission("tax.3")){
                        double taxAmount = amount * 0.03; // 计算10%的交税金额
                        economy.withdrawPlayer(player, taxAmount); // 从玩家账户中扣除税收
                        player.sendMessage("§a尊贵的 §8[§bMVP§8] §r" + player.getName() + " §a收到了 " + amount + " 金币，扣除了 " + taxAmount + " 金币作为税收。 结余: " + (amount-taxAmount) + "余额"+ (newBalance-taxAmount));
                    } else if (player.hasPermission("tax.2")) {
                        double taxAmount = amount * 0.07; // 计算10%的交税金额
                        economy.withdrawPlayer(player, taxAmount); // 从玩家账户中扣除税收
                        player.sendMessage("§a尊贵的 §8[§eSVIP§8] §r"+player.getName() + " §a收到了 " + amount + " 金币，扣除了 " + taxAmount + " 金币作为税收。结余: " + (amount-taxAmount)+ " 余额"+ (newBalance-taxAmount));
                    } else if (player.hasPermission("tax.1")) {
                        double taxAmount = amount * 0.09; // 计算10%的交税金额
                        economy.withdrawPlayer(player, taxAmount); // 从玩家账户中扣除税收
                        player.sendMessage("§a尊贵的 §8[§aVIP§8] §r"+player.getName() + " §a收到了 " + amount + " 金币，扣除了 " + taxAmount + " 金币作为税收。结余: " + (amount-taxAmount)+ " 余额"+ (newBalance-taxAmount));
                    } else {
                        double taxAmount = amount * 0.15; // 计算10%的交税金额
                        economy.withdrawPlayer(player, taxAmount); // 从玩家账户中扣除税收
                        player.sendMessage(player.getName() + " §a收到了 " + amount + " 金币，扣除了 " + taxAmount + " 金币作为税收。结余: " + (amount-taxAmount)+ " 余额"+ (newBalance-taxAmount));
                    }
                } else if (amount >= 10000) {
                    if (player.hasPermission("tax.3")){
                        double taxAmount = amount * 0.02; // 计算10%的交税金额
                        economy.withdrawPlayer(player, taxAmount); // 从玩家账户中扣除税收
                        player.sendMessage("§a尊贵的 §8[§bMVP§8] §r" + player.getName() + " §a收到了 " + amount + " 金币，扣除了 " + taxAmount + " 金币作为税收。 结余: " + (amount-taxAmount)+ " 余额"+ (newBalance-taxAmount));
                    } else if (player.hasPermission("tax.2")) {
                        double taxAmount = amount * 0.05; // 计算10%的交税金额
                        economy.withdrawPlayer(player, taxAmount); // 从玩家账户中扣除税收
                        player.sendMessage("§a尊贵的 §8[§eSVIP§8] §r"+player.getName() + " §a收到了 " + amount + " 金币，扣除了 " + taxAmount + " 金币作为税收。结余: " + (amount-taxAmount)+ " 余额"+ (newBalance-taxAmount));
                    } else if (player.hasPermission("tax.1")) {
                        double taxAmount = amount * 0.08; // 计算10%的交税金额
                        economy.withdrawPlayer(player, taxAmount); // 从玩家账户中扣除税收
                        player.sendMessage("§a尊贵的 §8[§aVIP§8] §r"+player.getName() + " §a收到了 " + amount + " 金币，扣除了 " + taxAmount + " 金币作为税收。结余: " + (amount-taxAmount)+ " 余额"+ (newBalance-taxAmount));
                    } else {
                        double taxAmount = amount * 0.1; // 计算10%的交税金额
                        economy.withdrawPlayer(player, taxAmount); // 从玩家账户中扣除税收
                        player.sendMessage(player.getName() + " §a收到了 " + amount + " 金币，扣除了 " + taxAmount + " 金币作为税收。结余: " + (amount-taxAmount)+ "余额"+ (newBalance-taxAmount));
                    }
                }else{
                    if (player.hasPermission("tax.3")){
                        double taxAmount = amount * 0.01; // 计算10%的交税金额
                        economy.withdrawPlayer(player, taxAmount); // 从玩家账户中扣除税收
                        player.sendMessage("§a尊贵的 §8[§bMVP§8] §r" + player.getName() + " §a收到了 " + amount + " 金币，扣除了 " + taxAmount + " 金币作为税收。 结余: " + (amount-taxAmount)+ " 余额"+ (newBalance-taxAmount));
                    } else if (player.hasPermission("tax.2")) {
                        double taxAmount = amount * 0.03; // 计算10%的交税金额
                        economy.withdrawPlayer(player, taxAmount); // 从玩家账户中扣除税收
                        player.sendMessage("§a尊贵的 §8[§eSVIP§8] §r"+player.getName() + " §a收到了 " + amount + " 金币，扣除了 " + taxAmount + " 金币作为税收。结余: " + (amount-taxAmount)+ " 余额"+ (newBalance-taxAmount));
                    } else if (player.hasPermission("tax.1")) {
                        double taxAmount = amount * 0.06; // 计算10%的交税金额
                        economy.withdrawPlayer(player, taxAmount); // 从玩家账户中扣除税收
                        player.sendMessage("§a尊贵的 §8[§aVIP§8] §r"+player.getName() + " §a收到了 " + amount + " 金币，扣除了 " + taxAmount + " 金币作为税收。结余: " + (amount-taxAmount)+ " 余额"+ (newBalance-taxAmount));
                    } else {
                        double taxAmount = amount * 0.08; // 计算10%的交税金额
                        economy.withdrawPlayer(player, taxAmount); // 从玩家账户中扣除税收
                        player.sendMessage(player.getName() + " §a收到了 " + amount + " 金币，扣除了 " + taxAmount + " 金币作为税收。结余: " + (amount-taxAmount)+ " 余额"+ (newBalance-taxAmount));
                    }
                }
            }*/