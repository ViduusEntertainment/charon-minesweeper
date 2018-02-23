/**
 * Copyright 2018, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Feb 16, 2018 by Ethan Toney
 */
package org.viduus.charon.minesweeper.graphics;

import org.viduus.charon.global.graphics.opengl.OpenGLFrame;

/**
 * @author ethan
 *
 */
public class GameFrame extends OpenGLFrame {

	public GameFrame() {
		super("Viduus: Minesweeper", false, false, true);
		
		setLocationRelativeTo(null);
		setVisible(true);
		setFPSVisible(false);
		setDefaultCloseOperation(OpenGLFrame.KILL_ON_CLOSE);
	}
	
}
