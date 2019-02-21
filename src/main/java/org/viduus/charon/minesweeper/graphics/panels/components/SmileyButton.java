/**
 * Copyright 2018, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Feb 22, 2018 by Ethan Toney
 */
package org.viduus.charon.minesweeper.graphics.panels.components;

import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.graphics.animation.Animation;
import org.viduus.charon.global.graphics.core.components.Component;
import org.viduus.charon.global.graphics.core.input.controller.state.StateMap;
import org.viduus.charon.global.graphics.core.input.controller.state.impl.DefaultActionStates;
import org.viduus.charon.minesweeper.GameConstants;
import org.viduus.charon.minesweeper.graphics.panels.AbstractMinesweeperScreen;

import java.util.List;

/**
 * @author ethan
 *
 */
public class SmileyButton extends Component {

	public enum Display {
		NORMAL,
		SUSPENCE,
		WIN,
		LOSE
	}
	
	private Animation<?>
		dead,
		normal,
		cool,
		box;
	private Display display;
	
	public SmileyButton(AbstractMinesweeperScreen screen) {
		setSize(25, 25);
		
		setRenderFunction((graphics, time_delta) -> {
			float x = getX();
			float y = getY();

			graphics.render(renderer -> {
				renderer.color(GameConstants.DARK_GREY)
						.fillRectangle2D(x-1, y-1, getWidth()+2, getHeight()+2)
						.drawAnimation2D(box, time_delta, x, y);

				switch (display) {
					case LOSE:
						renderer.drawAnimation2D(dead, time_delta, x, y);
						break;
					case NORMAL:
						renderer.drawAnimation2D(normal, time_delta, x, y);
						break;
					case SUSPENCE:
						renderer.drawAnimation2D(normal, time_delta, x, y);
						break;
					case WIN:
						renderer.drawAnimation2D(cool, time_delta, x, y);
						break;
				}
			});
		});
		
		registerControllerListener(event -> {
			StateMap<DefaultActionStates> action_states = event.getDefaultActionState();

			if (action_states.stateIsPressed(DefaultActionStates.ACTION_1)) {
				switch (display) {
				case LOSE:
					display = Display.NORMAL;
					screen.resetState();
					break;
				case WIN:
					display = Display.NORMAL;
					screen.resetState();
					break;
				case NORMAL:
					screen.resetState();
					break;
				default:
					break;
				}
			}
		});
	}

	@Override
	protected void onActivate(AbstractGameSystems game_systems, int last_panel_id, List<Object> data_packet) {
		dead = game_systems.graphics_engine.resolve("vid:animation:smileys.dead");
		normal = game_systems.graphics_engine.resolve("vid:animation:smileys.normal");
		cool = game_systems.graphics_engine.resolve("vid:animation:smileys.cool");
		box = game_systems.graphics_engine.resolve("vid:animation:tiles.box_25");
	}

	@Override
	protected List<Object> onDeactivate(AbstractGameSystems game_systems, int next_panel_id) {
		return List.of();
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.core.components.AbstractComponent#resetState()
	 */
	@Override
	public void resetState() {
		display = Display.NORMAL;
	}
	
	public void setDisplay(Display display) {
		this.display = display;
	}

}
