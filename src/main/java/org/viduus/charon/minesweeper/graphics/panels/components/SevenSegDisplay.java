/**
 * Copyright 2018, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Feb 22, 2018 by Ethan Toney
 */
package org.viduus.charon.minesweeper.graphics.panels.components;

import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.graphics.animation.Animation;
import org.viduus.charon.global.graphics.core.components.Component;

import java.util.List;

/**
 * @author ethan
 *
 */
public class SevenSegDisplay extends Component {

	private Animation<?>[] numbers = new Animation<?>[10];
	private Animation<?> background;
	private int number = 0;
	
	public SevenSegDisplay() {
		setSize(13*3, 23);
		
		setRenderFunction((graphics, time_delta) -> {
			float x = getX();
			float y = getY();
			
			int[] digits = new int[] {
					(number/100) % 10,
					(number/10) % 10,
					number % 10
			};

			graphics.render(renderer -> {
				for (int i=0 ; i<digits.length ; i++) {
					float tx = x+i*13;
					renderer.drawAnimation2D(background, time_delta, tx, y)
							.drawAnimation2D(numbers[digits[i]], time_delta, tx, y);
				}
			});
			
			UiUtil.renderBorder(graphics, x, y, getWidth(), getHeight(), 1);
		});
	}

	@Override
	protected void onActivate(AbstractGameSystems game_systems, int last_panel_id, List<Object> data_packet) {
		background = game_systems.graphics_engine.resolve("vid:animation:seven_seg.background");
		for (int i=0 ; i<10 ; i++) {
			numbers[i] = game_systems.graphics_engine.resolve("vid:animation:seven_seg.key_"+i);
		}
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
		number = 0;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}

}
