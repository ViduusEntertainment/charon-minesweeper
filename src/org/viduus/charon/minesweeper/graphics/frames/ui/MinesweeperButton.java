/**
 * Copyright 2018, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Feb 16, 2018 by Ethan Toney
 */
package org.viduus.charon.minesweeper.graphics.frames.ui;

import java.util.function.Consumer;

import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.graphics.animation.sprite.Animation;
import org.viduus.charon.global.graphics.core.AbstractGraphics;
import org.viduus.charon.global.graphics.ui.components.UiComponent;
import org.viduus.charon.global.input.controller.ControllerState;
import org.viduus.charon.minesweeper.graphics.frames.AbstractMinesweeperScreen;

/**
 * @author ethan
 *
 */
public class MinesweeperButton extends UiComponent {

	private static int[] dx = {-1, -1, 0, 1, 1, 1, 0, -1};
	private static int[] dy = {0, 1, 1, 1, 0, -1, -1, -1};
	
	private final MinesweeperButton[][] button_grid;
	private final int col, row;
	private final Animation<?>[] numbers = new Animation<?>[9];
	private boolean
		has_bomb = false,
		uncovered = false,
		display_bomb = false,
		has_flag = false;
	private Animation<?>
		background_16,
		box_16,
		red_16,
		bomb,
		flag;
	private int
		neighboring_bombs = 0,
		this_uncovered = 0;
	
	public MinesweeperButton(AbstractMinesweeperScreen screen, int col, int row, MinesweeperButton[][] button_grid) {
		this.col = col;
		this.row = row;
		this.button_grid = button_grid;
		
		setSize(16, 16);
		
		registerControllerListener(event -> {
			if (event.getKeyState(ControllerState.ACTION1) == ControllerState.PRESSED_STATE) {
				if (!uncovered) {
					int uncovered_buttons = recursiveUncover();
					if (uncovered_buttons == 0) {
						uncovered_buttons++;
						uncovered = true;
					}
					screen.buttonUncovered(col, row, uncovered_buttons);
				}
			}
			if (event.getKeyState(ControllerState.ACTION2) == ControllerState.PRESSED_STATE) {
				has_flag = !has_flag;
				if (has_flag) {
					screen.incrementFlagCount();
				} else {
					screen.decrementFlagCount();
				}
			}
		});
	}
	
	private void operateOnNeighbors(Consumer<MinesweeperButton> consumer, boolean include_diagonals) {
		int left = 0, right = button_grid[0].length-1, top = 0, bottom = button_grid.length-1;
		int di = (include_diagonals) ? 1 : 2;
		for (int i=0 ; i<8 ; i+=di) {
			int c = col + dx[i];
			int r = row + dy[i];

			if (c>=left && c<=right && r>=top && r<=bottom) {
				consumer.accept(button_grid[r][c]);
			}
		}
	}
	
	private int recursiveUncover() {
		// base cases
		if (has_bomb || uncovered) {
			return 0;
		}
		if (neighboring_bombs != 0) {
			uncovered = true;
			return 1;
		}
		
		// recursive step
		uncovered = true;
		this_uncovered = 1;
		operateOnNeighbors(neighbor -> {
			this_uncovered += neighbor.recursiveUncover();
		}, false);
		return this_uncovered;
	}
	
	public void updateNeighboringBombs() {
		neighboring_bombs = 0;
		operateOnNeighbors(neighbor -> {
			if (neighbor.has_bomb) {
				neighboring_bombs++;
			}
		}, true);
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.ui.components.UiComponent#onActivate(org.viduus.charon.global.AbstractGameSystems)
	 */
	@Override
	public void onActivate(AbstractGameSystems game_systems) {
		background_16 = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:tiles.background_16");
		box_16 = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:tiles.box_16");
		red_16 = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:tiles.red_16");
		bomb = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:icons.bomb");
		flag = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:icons.flag");
		for (int i=1 ; i<=8 ; i++) {
			numbers[i] = (Animation<?>) game_systems.graphics_engine.resolve("vid:animation:numbers."+i);
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
	 * @see org.viduus.charon.global.graphics.opengl.components.OpenGLComponent#resetState()
	 */
	@Override
	public void resetState() {
		super.resetState();
		has_bomb = false;
		uncovered = false;
		display_bomb = false;
		has_flag = false;
		neighboring_bombs = 0;
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.core.render.Renderable#render(org.viduus.charon.global.graphics.core.AbstractGraphics, double)
	 */
	@Override
	public void render(AbstractGraphics graphics, double d_sec) {
		float x = getX();
		float y = getY();
		
		if (uncovered) {
			if (has_bomb) {
				red_16.renderAnimation(graphics, d_sec, x, y, 1);
				bomb.renderAnimation(graphics, d_sec, x, y, 1);
			} else {
				background_16.renderAnimation(graphics, d_sec, x, y, 1);
				if (neighboring_bombs != 0) {
					numbers[neighboring_bombs].renderAnimation(graphics, d_sec, x, y, 1);
				}
			}
		} else if (display_bomb && has_bomb) {
			background_16.renderAnimation(graphics, d_sec, x, y, 1);
			bomb.renderAnimation(graphics, d_sec, x, y, 1);
		} else {
			box_16.renderAnimation(graphics, d_sec, x, y, 1);
			
			if (has_flag) {
				flag.renderAnimation(graphics, d_sec, x, y, 1);
			}
		}
	}
	
	public boolean hasBomb() {
		return has_bomb;
	}
	
	public void giveBomb() {
		has_bomb = true;
	}

	/**
	 * 
	 */
	public void showBomb() {
		display_bomb = true;
	}
	
}
