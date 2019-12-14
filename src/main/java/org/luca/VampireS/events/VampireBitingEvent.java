package org.luca.VampireS.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class VampireBitingEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private Entity target;
    private boolean cancelled;

    public VampireBitingEvent(Player vampire, Entity target) {
    	super(vampire);

    	this.target = target;
        this.cancelled = false;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    /**
     * @return the bit entity
     */
    public Entity getTarget() {
    	return target;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

}
