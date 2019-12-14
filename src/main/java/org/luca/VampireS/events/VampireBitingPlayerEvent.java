package org.luca.VampireS.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class VampireBitingPlayerEvent extends VampireBitingEvent {
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    /**
     *
     * @param vampire the biter
     * @param target the bit
     */
    public VampireBitingPlayerEvent(Player vampire, Player target) {
    	super(vampire, target);
    }

    /**
     * @return the bit player
     */
    @Override
    public Player getTarget() {
    	return (Player) super.getTarget();
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

}
