/**
 * Copyright 2018, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Feb 22, 2018 by Ethan Toney
 */
package org.viduus.charon.minesweeper.graphics.frames.ui;

import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.graphics.animation.sprite.Animation;
import org.viduus.charon.global.graphics.ui.components.UiComponent;

/**
 * @author ethan
 *
 */
public class SevenSegDisplay extends UiComponent {

	private Animation<?>[] numbers = new Animation<?>[10];
	private Animation<?>
		background;
	private int number = 0;
	
	public SevenSegDisplay() {
		setSize(13*3, 23);
		
		setRenderFunction((graphics, d_sec) -> {
			float x = getX();
			float y = getY();
			
			int[] digits = new int[] {
					(number/100) % 10,
					(number/10) % 10,
					number % 10
			};
			
			for (int i=0 ; i<digits.length ; i++) {
				float tx = x+i*13;
				background.renderAnimation(graphics, d_sec, tx, y, 1);
				numbers[digits[i]].renderAnimation(graphics, d_sec, tx, y, 1);
			}
			
			UiUtil.renderBorder(graphics, x, y, getWidth(), getHeight(), 1);
		});
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.ui.components.UiComponent#onActivate(org.viduus.charon.global.AbstractGameSystems)
	 */
	@Override
	public void onActivate(AbstractGameSystems game_systems) {
		background = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:seven_seg.background");
		for (int i=0 ; i<10 ; i++) {
			numbers[i] = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:seven_seg."+i);
		}
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.ui.components.UiComponent#onDeactivate(org.viduus.charon.global.AbstractGameSystems)
	 */
	@Override
	public void onDeactivate(AbstractGameSystems game_systems) {
		// TODO Auto-generated method stub
		
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
