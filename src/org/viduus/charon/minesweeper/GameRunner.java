/**
 * Copyright 2018, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Feb 5, 2018 by Ethan Toney
 */
package org.viduus.charon.minesweeper;

import org.viduus.charon.global.input.InputEngine;
import org.viduus.charon.global.input.controller.ControllerState;
import org.viduus.charon.global.util.systems.SystemsEngine;
import org.viduus.charon.minesweeper.event.EventEngine;
import org.viduus.charon.minesweeper.graphics.GraphicsEngine;

/**
 * @author ethan
 *
 */
public class GameRunner {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GameSystems game_systems = new GameSystems(
				new SystemsEngine(),
				new InputEngine(),
				new EventEngine(),
				new GraphicsEngine()
		);
		
		game_systems.input_engine.registerListener(0, "default-controls", (ControllerState e) -> {
			if (e.getKeyState(ControllerState.EXIT_GAME_KEY) == ControllerState.PRESSED_STATE) {
				game_systems.closeApplication();
			}
			if (e.getKeyState(ControllerState.ACTION3) == ControllerState.PRESSED_STATE) {
				game_systems.graphics_engine.showFrame(GameConstants.BEGINNER_SCREEN);
			}
			if (e.getKeyState(ControllerState.ACTION4) == ControllerState.PRESSED_STATE) {
				game_systems.graphics_engine.showFrame(GameConstants.INTERMEDIATE_SCREEN);
			}
			if (e.getKeyState(ControllerState.ACTION5) == ControllerState.PRESSED_STATE) {
				game_systems.graphics_engine.showFrame(GameConstants.EXPERT_SCREEN);
			}
		});
		
		// Will enter a semi-infinite loop here until the game is stopped
		game_systems.enterGameLoop();
		
		// FIXME: Force the game to close
		System.exit(0);
	}

}
