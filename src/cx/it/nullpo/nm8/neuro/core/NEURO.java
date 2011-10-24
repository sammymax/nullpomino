package cx.it.nullpo.nm8.neuro.core;

import cx.it.nullpo.nm8.gui.framework.NFGraphics;
import cx.it.nullpo.nm8.neuro.event.NEUROEvent;
import cx.it.nullpo.nm8.neuro.plugin.NEUROPlugin;

/**
 * NEURO (NullpoMino End-User Relational Overlayer) is the layer that controls everything, so to speak. It
 * establishes an event-subscriber network with all of the plugins that register with it, and passes the
 * events between them. It can also bring up an overlay and draw things to the screen.
 * @author Zircean
 *
 */
public interface NEURO {

	/**
	 * Gets the name of this NEURO instance.
	 */
	String getName();

	/**
	 * Gets the version of this NEURO instance.
	 */
	float getVersion();

	/**
	 * Registers the given plugin so it will be handled by NEURO.
	 * @param p the Plugin to register
	 */
	void addPlugin(NEUROPlugin p);

	/**
	 * Registers the given plugin to receive events of the given type.
	 * @param p the Plugin to register
	 * @param type the class representing the event type to listen for
	 */
	void addListener(NEUROPlugin p, Class<? extends NEUROEvent> type);

	/**
	 * Dispatches the given event to all plugins that are subscribed to it.
	 * @param e the NEUROEvent to dispatch
	 */
	void dispatchEvent(NEUROEvent e);

	/**
	 * Update the internal state of the game and this NEURO instance.
	 * @param delta Time elapsed from the last execution
	 */
	void update(long delta);

	/**
	 * Draws this NEURO instance to the screen. All drawing should be done through NEURO (such as the actual game).
	 * @param g the NFGraphics with which to draw this NEURO instance
	 */
	void draw(NFGraphics g);
}
