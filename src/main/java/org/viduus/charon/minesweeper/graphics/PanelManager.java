package org.viduus.charon.minesweeper.graphics;

import org.viduus.charon.global.graphics.core.AbstractPanelManager;
import org.viduus.charon.minesweeper.GameConstants;
import org.viduus.charon.minesweeper.graphics.panels.BeginnerScreen;
import org.viduus.charon.minesweeper.graphics.panels.ExpertScreen;
import org.viduus.charon.minesweeper.graphics.panels.IntermediateScreen;

public class PanelManager extends AbstractPanelManager {

	public PanelManager() {
		super(GameConstants.EXPERT_SCREEN);
	}

	@Override
	protected void registerAllPanels() {
		registerPanel(GameConstants.BEGINNER_SCREEN, new BeginnerScreen(this));
		registerPanel(GameConstants.INTERMEDIATE_SCREEN, new IntermediateScreen(this));
		registerPanel(GameConstants.EXPERT_SCREEN, new ExpertScreen(this));
	}

}
