/**
 * Copyright 2018, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Feb 16, 2018 by Ethan Toney
 */
package org.viduus.charon.minesweeper.graphics.panels;

import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.graphics.core.AbstractGraphics;
import org.viduus.charon.global.graphics.core.AbstractPanelManager;
import org.viduus.charon.global.graphics.core.components.Component;
import org.viduus.charon.global.graphics.core.components.panels.Panel;
import org.viduus.charon.global.util.logging.OutputHandler;
import org.viduus.charon.global.util.time.CountdownTimer;
import org.viduus.charon.global.util.time.StopWatch;
import org.viduus.charon.global.util.time.TimeStep;
import org.viduus.charon.global.util.time.Timer;
import org.viduus.charon.minesweeper.GameConstants;
import org.viduus.charon.minesweeper.graphics.panels.components.MinesweeperButton;
import org.viduus.charon.minesweeper.graphics.panels.components.SevenSegDisplay;
import org.viduus.charon.minesweeper.graphics.panels.components.SmileyButton;
import org.viduus.charon.minesweeper.graphics.panels.components.SmileyButton.Display;
import org.viduus.charon.minesweeper.graphics.panels.components.UiUtil;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ethan
 *
 */
public abstract class AbstractMinesweeperScreen extends Panel {

	private static final Random random = new Random();
	private static final int
		BORDER_SIZE = 9,
		TILE_SIZE = 16;
	private final MinesweeperButton[][] button_grid;
	private final int
		num_bombs,
		grid_width,
		grid_height;
	private SmileyButton smiley_button;
	private SevenSegDisplay timer_display;
	private SevenSegDisplay score_board_display;
	private Timer game_timer = new Timer();
	private final AtomicInteger
		uncovered_buttons = new AtomicInteger(),
		flag_count = new AtomicInteger();

	AbstractMinesweeperScreen(AbstractPanelManager panel_manager, int grid_width, int grid_height, int num_bombs) {
		super(panel_manager);
		this.grid_width = grid_width;
		this.grid_height = grid_height;
		this.num_bombs = num_bombs;
		
		int top = BORDER_SIZE;
		int left = BORDER_SIZE;
		float width = computeWidth();
		
		// top panel
		smiley_button = new SmileyButton(this);
		smiley_button.setLocation((width-25)/2, top+4);
		add(smiley_button);

		timer_display = new SevenSegDisplay();
		timer_display.setLocation(left+5, top+4);
		add(timer_display);

		score_board_display = new SevenSegDisplay();
		score_board_display.setLocation(width-BORDER_SIZE-score_board_display.getWidth()-5, top+4);
		add(score_board_display);
		
		top += 44;
		
		// generate grid
		button_grid = new MinesweeperButton[grid_height][grid_width];
		for (int r=0 ; r<grid_height ; r++) {
			for (int c=0 ; c<grid_width ; c++) {
				button_grid[r][c] = new MinesweeperButton(this, c, r, button_grid);
				button_grid[r][c].setLocation(left+c*TILE_SIZE, top+r*TILE_SIZE);
				add(button_grid[r][c]);
			}
		}
		
		// set panel's size
		setBackgroundColor(GameConstants.GREY);
		setSize(computeWidth(), computeHeight());
		game_timer.stop();
	}
	
	private float computeWidth() {
		return 2*BORDER_SIZE + grid_width*TILE_SIZE;
	}
	
	private float computeHeight() {
		return 2*BORDER_SIZE + grid_width*TILE_SIZE + 44;
	}

	@Override
	protected void onActivate(AbstractGameSystems game_systems, int last_panel_id, List<Object> data_packet) {
		super.onActivate(game_systems, last_panel_id, data_packet);
		resetState();
		panel_manager.getGraphicsFrame().setSize(getWidth(), getHeight());
		panel_manager.getGraphicsFrame().setLocationRelativeTo(null);
	}

	@Override
	protected List<Object> onDeactivate(AbstractGameSystems game_systems, int next_panel_id) {
		resetState();
		return super.onDeactivate(game_systems, next_panel_id);
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.screens.AbstractGameScreen#updateLayout(int, int)
	 */
	@Override
	protected void updateLayout(int width, int height) {}
	
	@Override
	public void resetState() {
		uncovered_buttons.set(0);
		flag_count.set(0);
		super.resetState();
		resetTimer();
		placeBombs();
		enableGrid();
	}
	
	private void placeBombs() {
		int placed_bombs = 0;
		while (placed_bombs < num_bombs) {
			int nx = random.nextInt(grid_width);
			int ny = random.nextInt(grid_height);
			
			if (!button_grid[ny][nx].hasBomb()) {
				button_grid[ny][nx].giveBomb();
				placed_bombs++;
			}
		}
		
		assignNumbers();
	}
	
	private void assignNumbers() {
		for (int r=0 ; r<grid_height ; r++) {
			for (int c=0 ; c<grid_width ; c++) {
				button_grid[r][c].updateNeighboringBombs();
			}
		}
	}
	
	private void showBombs() {
		for (int r=0 ; r<grid_height ; r++) {
			for (int c=0 ; c<grid_width ; c++) {
				button_grid[r][c].showBomb();
			}
		}
	}

	private void resetTimer() {
		// set clock back to zero
		game_timer.reset();
		// don't tick the clock yet
		game_timer.stop();
		// update display
		updateTimer();
	}

	private void updateTimer() {
		timer_display.setNumber((int) game_timer.deltaTime().seconds());
	}

	private void stopTimer() {
		// don't tick the clock anymore
		game_timer.stop();
	}

	private void disableGrid() {
		for (int r=0 ; r<grid_height ; r++) {
			for (int c=0 ; c<grid_width ; c++) {
				button_grid[r][c].disable();
			}
		}
	}

	private void enableGrid() {
		for (int r=0 ; r<grid_height ; r++) {
			for (int c=0 ; c<grid_width ; c++) {
				button_grid[r][c].enable();
			}
		}
	}
	
	/**
	 * @param col
	 * @param row
	 * @param uncovered_buttons 
	 */
	public void buttonUncovered(int col, int row, int uncovered_buttons) {
		this.uncovered_buttons.getAndIncrement();

		if (!game_timer.isStarted()) {
			game_timer.reset();
		}
		
		// if they lost
		if (button_grid[row][col].hasBomb()) {
			stopTimer();
			showBombs();
			disableGrid();
			smiley_button.setDisplay(Display.LOSE);
			
		// if they won
		} else if (this.uncovered_buttons.get() == grid_width * grid_height - num_bombs) {
			stopTimer();
			showBombs();
			disableGrid();
			smiley_button.setDisplay(Display.WIN);
		}
	}

	@Override
	public void render(AbstractGraphics graphics, TimeStep time_delta) {
		game_timer.update(time_delta);
		updateTimer();
		score_board_display.setNumber(flag_count.get());
		
		super.render(graphics, time_delta);
		
		Component top_left = button_grid[0][0];
		Component bottom_right = button_grid[grid_height-1][grid_width-1];
		
		float left = top_left.getLocation().x;
		float top = top_left.getLocation().y;
		float right = (bottom_right.getLocation().x + bottom_right.getWidth());
		float bottom = (bottom_right.getLocation().y + bottom_right.getHeight());

		UiUtil.renderBorder(graphics, left, top, right-left, bottom-top, 3);
		UiUtil.renderBorder(graphics, 8, 8, getWidth()-16, 34, 2);
	}

	/**
	 * 
	 */
	public void incrementFlagCount() {
		flag_count.getAndIncrement();
	}

	/**
	 * 
	 */
	public void decrementFlagCount() {
		flag_count.getAndDecrement();
	}

}
