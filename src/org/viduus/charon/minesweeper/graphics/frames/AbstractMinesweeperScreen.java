/**
 * Copyright 2018, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Feb 16, 2018 by Ethan Toney
 */
package org.viduus.charon.minesweeper.graphics.frames;

import java.util.Random;

import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.graphics.core.AbstractGraphics;
import org.viduus.charon.global.graphics.opengl.OpenGLFrame;
import org.viduus.charon.global.graphics.ui.components.UiComponent;
import org.viduus.charon.global.graphics.ui.panels.UiPanel;
import org.viduus.charon.global.util.logging.OutputHandler;
import org.viduus.charon.global.util.time.StopWatch;
import org.viduus.charon.minesweeper.graphics.frames.ui.MinesweeperButton;
import org.viduus.charon.minesweeper.graphics.frames.ui.SevenSegDisplay;
import org.viduus.charon.minesweeper.graphics.frames.ui.SmileyButton;
import org.viduus.charon.minesweeper.graphics.frames.ui.SmileyButton.Display;
import org.viduus.charon.minesweeper.graphics.frames.ui.UiUtil;

/**
 * @author ethan
 *
 */
public abstract class AbstractMinesweeperScreen extends UiPanel {

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
	private StopWatch game_timer = new StopWatch();
	private int 
		uncovered_buttons,
		flag_count;
	
	/**
	 * @param graphics_frame
	 */
	public AbstractMinesweeperScreen(OpenGLFrame graphics_frame, int grid_width, int grid_height, int num_bombs) {
		super(graphics_frame);
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
		setBackgroundColor(UiUtil.GREY);
		setSize(computeWidth(), computeHeight());
	}
	
	private float computeWidth() {
		return 2*BORDER_SIZE + grid_width*TILE_SIZE;
	}
	
	private float computeHeight() {
		return 2*BORDER_SIZE + grid_width*TILE_SIZE + 44;
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.screens.AbstractGameScreen#onActivate(int, org.viduus.charon.global.AbstractGameSystems)
	 */
	@Override
	protected void onActivate(int previous_screen_id, AbstractGameSystems game_systems) {
		OutputHandler.println(getWidth() + " " + getHeight());
		game_frame.setSize(getWidth(), getHeight());
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.screens.AbstractGameScreen#onDeactivate(org.viduus.charon.global.AbstractGameSystems)
	 */
	@Override
	protected void onDeactivate(AbstractGameSystems game_systems) {
		resetState();
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.graphics.screens.AbstractGameScreen#updateLayout(int, int)
	 */
	@Override
	protected void updateLayout(int width, int height) {}
	
	@Override
	public void resetState() {
		uncovered_buttons = 0;
		flag_count = 0;
		super.resetState();
		resetTimer();
		placeBombs();
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
		stopTimer();
		timer_display.setNumber(0);
	}

	private void updateTimer() {
		if (game_timer.isStarted()) {
			timer_display.setNumber((int) Math.round(game_timer.getSecTime()));
		}
	}
	
	private void stopTimer() {
		if (game_timer.isStarted()) {
			updateTimer();
			game_timer.stop();
			game_timer.reset();
		}
	}
	
	private void disableGrid() {
		for (int r=0 ; r<grid_height ; r++) {
			for (int c=0 ; c<grid_width ; c++) {
				button_grid[r][c].disableControllerListeners();
			}
		}
	}
	
	/**
	 * @param col
	 * @param row
	 * @param uncovered_buttons 
	 */
	public void buttonUncovered(int col, int row, int uncovered_buttons) {
		this.uncovered_buttons += uncovered_buttons;
		
		if (!game_timer.isStarted()) {
			game_timer.start();
		}
		
		// if they lost
		if (button_grid[row][col].hasBomb()) {
			stopTimer();
			showBombs();
			disableGrid();
			smiley_button.setDisplay(Display.LOSE);
			
		// if they won
		} else if (this.uncovered_buttons == grid_width * grid_height - num_bombs) {
			stopTimer();
			showBombs();
			disableGrid();
			smiley_button.setDisplay(Display.WIN);
		}
	}
	
	@Override
	public void render(AbstractGraphics graphics, double d_sec) {
		updateTimer();
		score_board_display.setNumber(flag_count);
		
		super.render(graphics, d_sec);
		
		UiComponent top_left = button_grid[0][0];
		UiComponent bottom_right = button_grid[grid_height-1][grid_width-1];
		
		float left = (float) top_left.getLocation().x;
		float top = (float) top_left.getLocation().y;
		float right = (float) (bottom_right.getLocation().x + bottom_right.getWidth());
		float bottom = (float) (bottom_right.getLocation().y + bottom_right.getHeight());

		UiUtil.renderBorder(graphics, left, top, right-left, bottom-top, 3);
		
		UiUtil.renderBorder(graphics, 8, 8, getWidth()-16, 34, 2);
	}

	/**
	 * 
	 */
	public void incrementFlagCount() {
		flag_count++;
	}

	/**
	 * 
	 */
	public void decrementFlagCount() {
		flag_count++;
	}

}
