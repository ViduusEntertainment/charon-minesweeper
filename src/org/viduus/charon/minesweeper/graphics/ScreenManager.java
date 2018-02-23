/**
 * Copyright 2018, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Feb 16, 2018 by Ethan Toney
 */
package org.viduus.charon.minesweeper.graphics;

import org.viduus.charon.global.graphics.opengl.OpenGLFrame;
import org.viduus.charon.global.graphics.ui.panels.UiPanelManager;
import org.viduus.charon.minesweeper.GameConstants;
import org.viduus.charon.minesweeper.graphics.frames.BeginnerScreen;
import org.viduus.charon.minesweeper.graphics.frames.ExpertScreen;
import org.viduus.charon.minesweeper.graphics.frames.IntermediateScreen;

/**
 * @author ethan
 *
 */
public class ScreenManager extends UiPanelManager {

	/**
	 * @param default_screen
	 */
	public ScreenManager(OpenGLFrame graphics_frame) {
		super(GameConstants.BEGINNER_SCREEN, new BeginnerScreen(graphics_frame));
		registerGameScreen(GameConstants.INTERMEDIATE_SCREEN, new IntermediateScreen(graphics_frame));
		registerGameScreen(GameConstants.EXPERT_SCREEN, new ExpertScreen(graphics_frame));
	}

}
