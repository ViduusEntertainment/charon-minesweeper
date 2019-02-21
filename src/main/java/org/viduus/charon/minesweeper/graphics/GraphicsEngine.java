/**
 * Copyright 2018, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Feb 16, 2018 by Ethan Toney
 */
package org.viduus.charon.minesweeper.graphics;

import org.viduus.charon.global.GameInfo;
import org.viduus.charon.global.graphics.AbstractGraphicsEngine;
import org.viduus.charon.global.graphics.core.AbstractFrame;
import org.viduus.charon.global.graphics.core.FrameConfig;
import org.viduus.charon.global.graphics.core.FrameType;
import org.viduus.charon.global.graphics.core.components.panels.Panel;
import org.viduus.charon.global.graphics.opengl.OpenGLFrame;

/**
 * @author ethan
 *
 */
public class GraphicsEngine extends AbstractGraphicsEngine {

	public GraphicsEngine() {
		super(new PanelManager());
	}

	@Override
	protected AbstractFrame<?> createGameFrame() {
		FrameConfig config = new FrameConfig();
		config.title = "Viduus - Minesweeper";
		config.type = FrameType.WINDOWED;
		config.is_2d = true;

		AbstractFrame<?> frame = new OpenGLFrame<>(config);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(AbstractFrame.KILL_ON_CLOSE);
		frame.setResizeable(false);

		return frame;
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.GameEngine#onLoadGame(org.viduus.charon.global.GameInfo)
	 */
	@Override
	protected void onLoadGame(GameInfo game_info) {
		super.onLoadGame(game_info);
		
		graphics_frame.setDesiredFPS(55);
	}

	public void showFrame(int id) {
		panel_manager.setPanel(game_systems, id);
	}

}
