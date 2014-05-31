package net.piedmontmc.ffagame;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;


/*
 * Got this class from: https://forums.bukkit.org/threads/countdown-timer.204243/
 * Modified it myself and added the "final String st"
 */
public class Countdown
{
    private int countdownTimer;
 
    public void start(final int time, final String st)
    {
        this.countdownTimer = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable()
        {
            int i = time;
 
            public void run()
            {
                Bukkit.broadcastMessage(ChatColor.GREEN + st + " in " + i + " seconds!");
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
