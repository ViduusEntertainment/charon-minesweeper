/**
 * Copyright 2018, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Feb 16, 2018 by Ethan Toney
 */
package org.viduus.charon.minesweeper;

import org.viduus.charon.global.AbstractEngineSettings;
import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.util.resources.identification.ClasspathMapper;
import org.viduus.charon.minesweeper.graphics.GraphicsEngine;

/**
 * @author ethan
 *
 */
public class GameSystems extends AbstractGameSystems {

	public GameSystems(){
		super(
				new ClasspathMapper(),
				new AbstractEngineSettings() {},
				new GraphicsEngine()
		);
	}

}
