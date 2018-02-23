/**
 * Copyright 2018, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Feb 16, 2018 by Ethan Toney
 */
package org.viduus.charon.minesweeper.graphics.frames;

import org.viduus.charon.global.graphics.opengl.OpenGLFrame;

/**
 * @author ethan
 *
 */
public class BeginnerScreen extends AbstractMinesweeperScreen {
	
	/**
	 * @param graphics_frame
	 */
	public BeginnerScreen(OpenGLFrame graphics_frame) {
		super(graphics_frame, 8, 8, 10);
	}

}
