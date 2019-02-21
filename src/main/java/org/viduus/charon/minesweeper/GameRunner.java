/**
 * Copyright 2018, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Feb 5, 2018 by Ethan Toney
 */
package org.viduus.charon.minesweeper;

import org.viduus.charon.global.AbstractEngineSettings;
import org.viduus.charon.global.graphics.core.input.controller.state.StateMap;
import org.viduus.charon.global.graphics.core.input.controller.state.impl.DefaultActionStates;
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
		GameSystems game_systems = new GameSystems();
		
		game_systems.graphics_engine.callback_manager.registerListener(0, "default-controls", event -> {
			StateMap<DefaultActionStates> action_states = event.getDefaultActionState();

			if (action_states.stateIsPressed(DefaultActionStates.DEV_1)) {
				game_systems.closeApplication();
			}
			if (action_states.stateIsPressed(DefaultActionStates.DEV_2)) {
				((GraphicsEngine)game_systems.graphics_engine).showFrame(GameConstants.BEGINNER_SCREEN);
			}
			if (action_states.stateIsPressed(DefaultActionStates.DEV_3)) {
				((GraphicsEngine)game_systems.graphics_engine).showFrame(GameConstants.INTERMEDIATE_SCREEN);
			}
			if (action_states.stateIsPressed(DefaultActionStates.DEV_4)) {
				((GraphicsEngine)game_systems.graphics_engine).showFrame(GameConstants.EXPERT_SCREEN);
			}
		});
		
		// Will enter a semi-infinite loop here until the game is stopped
		game_systems.enterGameLoop();
		
		// FIXME: Force the game to close
		System.exit(0);
	}

}
