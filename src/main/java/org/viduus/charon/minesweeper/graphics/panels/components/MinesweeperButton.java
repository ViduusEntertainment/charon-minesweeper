/**
 * Copyright 2018, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Feb 16, 2018 by Ethan Toney
 */

package org.viduus.charon.minesweeper.graphics.panels.components;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.graphics.animation.Animation;
import org.viduus.charon.global.graphics.core.AbstractGraphics;
import org.viduus.charon.global.graphics.core.components.Component;
import org.viduus.charon.global.graphics.core.input.controller.state.StateMap;
import org.viduus.charon.global.graphics.core.input.controller.state.impl.DefaultActionStates;
import org.viduus.charon.global.util.time.CountdownTimer;
import org.viduus.charon.global.util.time.Time;
import org.viduus.charon.global.util.time.TimeStep;
import org.viduus.charon.minesweeper.graphics.panels.AbstractMinesweeperScreen;

/**
 * @author ethan
 *
 */
public class MinesweeperButton extends Component {

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
	private final CountdownTimer
		input_timer = new CountdownTimer(Time.at(TimeUnit.SECONDS, 0.1));
	
	public MinesweeperButton(AbstractMinesweeperScreen screen, int col, int row, MinesweeperButton[][] button_grid) {
		this.col = col;
		this.row = row;
		this.button_grid = button_grid;
		
		setSize(16, 16);
		
		registerControllerListener(event -> {
			StateMap<DefaultActionStates> action_states = event.getDefaultActionState();

			if (!input_timer.done())
				return;

			if (action_states.stateIsPressed(DefaultActionStates.ACTION_1)) {
				if (!uncovered) {
					// update flag count
					if (has_flag) {
						has_flag = false;
						screen.decrementFlagCount();
					}
					// uncover the box
					int uncovered_buttons = recursiveUncover();
					if (uncovered_buttons == 0) {
						uncovered_buttons++;
						uncovered = true;
					}
					screen.buttonUncovered(col, row, uncovered_buttons);
					input_timer.reset();
				}
			}
			if (action_states.stateIsPressed(DefaultActionStates.ACTION_2)) {
				if (!uncovered) {
					has_flag = !has_flag;
					if (has_flag) {
						screen.incrementFlagCount();
					} else {
						screen.decrementFlagCount();
					}
					input_timer.reset();
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
		}, true);
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

	@Override
	protected void onActivate(AbstractGameSystems game_systems, int last_panel_id, List<Object> data_packet) {
		background_16 = game_systems.graphics_engine.resolve("vid:animation:tiles.background_16");
		box_16 = game_systems.graphics_engine.resolve("vid:animation:tiles.box_16");
		red_16 = game_systems.graphics_engine.resolve("vid:animation:tiles.red_16");
		bomb = game_systems.graphics_engine.resolve("vid:animation:icons.bomb");
		flag = game_systems.graphics_engine.resolve("vid:animation:icons.flag");
		for (int i=1 ; i<=8 ; i++) {
			numbers[i] = game_systems.graphics_engine.resolve("vid:animation:numbers.key_"+i);
		}
	}

	@Override
	protected List<Object> onDeactivate(AbstractGameSystems game_systems, int next_panel_id) {
		return List.of();
	}

	@Override
	public void resetState() {
		has_bomb = false;
		uncovered = false;
		display_bomb = false;
		has_flag = false;
		neighboring_bombs = 0;
	}

	@Override
	public void render(AbstractGraphics graphics, TimeStep time_delta) {
		input_timer.update(time_delta);
		float x = getX();
		float y = getY();

		graphics.render(renderer -> {
			if (uncovered) {
				if (has_bomb) {
					renderer.drawAnimation2D(red_16, time_delta, x, y)
							.drawAnimation2D(bomb, time_delta, x, y);
				} else {
					renderer.drawAnimation2D(background_16, time_delta, x, y);
					if (neighboring_bombs != 0) {
						renderer.drawAnimation2D(numbers[neighboring_bombs], time_delta, x, y);
					}
				}
			} else if (display_bomb && has_bomb) {
				renderer.drawAnimation2D(background_16, time_delta, x, y)
						.drawAnimation2D(bomb, time_delta, x, y);
			} else {
				renderer.drawAnimation2D(box_16, time_delta, x, y);
				if (has_flag) {
					renderer.drawAnimation2D(flag, time_delta, x, y);
				}
			}
		});
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
