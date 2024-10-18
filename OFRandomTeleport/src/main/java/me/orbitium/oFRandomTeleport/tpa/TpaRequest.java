package me.orbitium.oFRandomTeleport.tpa;

import org.bukkit.entity.Player;

public class TpaRequest {

    public Player sender;
    public Player receiver;
    public long date;

    public TpaRequest(Player sender, Player receiver, long date) {
        this.sender = sender;
        this.receiver = receiver;
        this.date = date;
    }

}
