package com.jederSign.Commands;

import com.jederSign.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class signExecutor implements CommandExecutor {
    private final Main plugin;
    //設定構造器
    public signExecutor ( Main plugin ) {
        this.plugin = plugin ;
    }

    @Override
    public boolean onCommand (CommandSender sender , Command cmd , String label , String[] args ) {
        if ( cmd.getName().equalsIgnoreCase( "sign" ) || cmd.getName().equalsIgnoreCase( "jederSign" ) || cmd.getName().equalsIgnoreCase( "js" ) || cmd.getName().equalsIgnoreCase( "jes" ) || cmd.getName().equalsIgnoreCase( "jsg" ) ) {
            if ( sender instanceof Player ){
                //發出者是玩家
                Player player = plugin.getServer().getPlayer( sender.getName() ) ; //將發送者轉換為玩家
                Block block = player.getTargetBlock( null , 10 ) ; //獲取玩家看著的方塊
                Location pointTo = block.getLocation() ;
                System.out.println( pointTo.getX() + "," + pointTo.getY() + "," + pointTo.getZ() + "," + block.getType() );
                if ( args.length == 0 ){
                    sender.sendMessage( ChatColor.YELLOW + "[JederSign] " + ChatColor.RED + "參數不足。" );
                    return false ;
                }
                //輸入/sign指令時
                if ( args.length < 1 ){
                    //沒有輸入任何參數時
                    sender.sendMessage( ChatColor.YELLOW + "[JederSign] " + ChatColor.RED + "參數不足。" );
                    return false ;
                }
                else {
                    //參數符合了
                    if ( !block.getType().equals( Material.SIGN ) && !block.getType().equals( Material.SIGN_POST ) && !block.getType().equals( Material.WALL_SIGN ) ) {
                        //當玩家指向的位置不是告示牌
                        sender.sendMessage( ChatColor.YELLOW + "[JederSign] " + ChatColor.RED + "你需要指向一個告示牌！" );
                        return true ;
                    }
                    else {
                        //玩家指到了告示牌上
                        Location playerLocation = player.getLocation() ;
                        Double distance = Math.sqrt( ( playerLocation.getX() - pointTo.getX() ) * ( playerLocation.getX() - pointTo.getX() ) + ( playerLocation.getY() - pointTo.getY() ) * ( playerLocation.getY() - pointTo.getY() ) + ( playerLocation.getZ() - pointTo.getZ() ) * ( playerLocation.getZ() - pointTo.getZ() ) ) ;
                        //運用勾股定理算出兩點間的長度
                        if ( distance > 7 ){
                            //與牌子的距離大於7m時
                            Double dst = Double.valueOf(Math.round( distance * 100 ) / 100 );
                            sender.sendMessage( ChatColor.YELLOW + "[JederSign] " + ChatColor.GOLD + "你與牌子的距離是 " + ChatColor.AQUA + dst + ChatColor.GOLD + " m，你需要離牌子7m以內才能設置。" );
                            return true;
                        }
                        else
                        {
                            //甚麼問題都沒有，終於可以設置了
                            //沒有輸入任何內容
                            Sign sign = (Sign) block.getState() ;
                            int lineNum ;
                            try {
                                lineNum = Integer.valueOf( args[0] ) ;
                                //嘗試去獲取行號
                            } catch ( Exception e ) {
                                sender.sendMessage( ChatColor.YELLOW + "[JederSign] " + ChatColor.RED + "行號不合法，必須是數字。" );
                                System.err.println( e );
                                return true;
                            }
                            //獲取玩家的行號
                            if ( lineNum < 0 || lineNum > 3 ) {
                                //行號不是0到3的數字
                                sender.sendMessage( ChatColor.YELLOW + "[JederSign] " + ChatColor.RED + "行號不合法，必須是0~3的整數。" );
                                return true ;
                            }
                            else {
                                if ( args.length < 2 ) {
                                    //沒有輸入內容時
                                    sign.setLine( lineNum , "" );
                                    //清除該行
                                    sender.sendMessage( ChatColor.YELLOW + "[JederSign] " + ChatColor.GREEN + "成功清除該行。" );
                                    sign.update(); //更新塊狀態
                                    return true ;
                                }
                                else {
                                    if ( args.length > 2 ) {
                                        //當輸入的文本包含空白時，將其自動組合
                                        String text = "" ;
                                        for ( int i = 1 ; i < args.length ; i++ ) {
                                            text = text + args[i] ;
                                        }
                                        //將組合後的文本發出
                                        text = ChatColor.translateAlternateColorCodes( '&' , text ) ;
                                        //將所有的&當作§識別
                                        sign.setLine( lineNum , text );
                                        sender.sendMessage( ChatColor.YELLOW + "[JederSign] " + ChatColor.GREEN + "成功將該行文字設為 " + ChatColor.RESET + text + ChatColor.GREEN + " 。" );
                                        sign.update(); //更新塊狀態
                                        return true ;
                                    }
                                    else if ( args.length == 2 ){
                                        String text = args[1] ;
                                        text = ChatColor.translateAlternateColorCodes( '&' , text ) ;
                                        //將所有的&當作§識別
                                        sign.setLine( lineNum , text );
                                        sender.sendMessage( ChatColor.YELLOW + "[JederSign] " + ChatColor.GREEN + "成功將該行文字設為 " + ChatColor.RESET + text + ChatColor.GREEN + " 。" );
                                        sign.update(); //更新塊狀態
                                        return true ;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else
            {
                sender.sendMessage( ChatColor.YELLOW + "[JederSign] " + ChatColor.RED + "該指令僅允許玩家執行。" );
            }
            return true ;
        }
        return false ;
    }
}
