package net.piedmontmc.ffagame;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Countdown
{
    private int countdownTimer;
 
    public void start(final int time)
    {
        this.countdownTimer = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable()
        {
            int i = time;
 
            public void run()
            {
                Bukkit.broadcastMessage(ChatColor.GREEN + "Starting in " + i + " seconds!");
                this.i--;
                if (this.i <= 0)
                {
                    cancel();
                    //ended
                }
            }
        }
        , 0L, 20L);
    }
 
    public void cancel()
    {
        Bukkit.getScheduler().cancelTask(this.countdownTimer);
    }
}
