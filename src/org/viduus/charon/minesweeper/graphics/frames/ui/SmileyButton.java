/**
 * Copyright 2018, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Feb 22, 2018 by Ethan Toney
 */
package org.viduus.charon.minesweeper.graphics.frames.ui;

import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.graphics.animation.sprite.Animation;
import org.viduus.charon.global.graphics.core.shaders.AbstractShaderProgram;
import org.viduus.charon.global.graphics.opengl.shapes.Rectangle;
import org.viduus.charon.global.graphics.ui.components.UiComponent;
import org.viduus.charon.global.input.controller.ControllerState;
import org.viduus.charon.global.util.logging.ErrorHandler;
import org.viduus.charon.minesweeper.graphics.frames.AbstractMinesweeperScreen;

/**
 * @author ethan
 *
 */
public class SmileyButton extends UiComponent {

	public static enum Display {
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
		
		setRenderFunction((graphics, d_sec) -> {
			ErrorHandler.tryRunThrow(() -> {
				float x = getX();
				float y = getY();
				
				graphics.renderer().useShader("opengl_frame");
				AbstractShaderProgram program = graphics.shader_manager.getActiveShader();
				program.getUniformVariable("set_Color").setValue(UiUtil.DARK_GREY);
				Rectangle.fillRectangle(graphics, "in_Position", x-1, y-1, getWidth()+2, getHeight()+2);
				
				box.renderAnimation(graphics, d_sec, x, y, 1);
				
				switch (display) {
				case LOSE:
					dead.renderAnimation(graphics, d_sec, x, y, 1);
					break;
				case NORMAL:
					normal.renderAnimation(graphics, d_sec, x, y, 1);
					break;
				case SUSPENCE:
					normal.renderAnimation(graphics, d_sec, x, y, 1);
					break;
				case WIN:
					cool.renderAnimation(graphics, d_sec, x, y, 1);
					break;
				}
			});
		});
		
		registerControllerListener(event -> {
			if (event.getKeyState(ControllerState.ACTION1) == ControllerState.PRESSED_STATE) {
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
	
	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.ui.components.UiComponent#onActivate(org.viduus.charon.global.AbstractGameSystems)
	 */
	@Override
	public void onActivate(AbstractGameSystems game_systems) {
		dead = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:smileys.dead");
		normal = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:smileys.normal");
		cool = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:smileys.cool");
		box = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:tiles.box_25");
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
		display = Display.NORMAL;
	}
	
	public void setDisplay(Display display) {
		this.display = display;
	}

}
