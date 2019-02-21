/**
 * Copyright 2018, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Feb 22, 2018 by Ethan Toney
 */
package org.viduus.charon.minesweeper.graphics.panels.components;

import org.viduus.charon.global.graphics.core.AbstractGraphics;
import org.viduus.charon.minesweeper.GameConstants;

/**
 * @author ethan
 *
 */
public class UiUtil {
	
	public static void renderBorder(AbstractGraphics graphics, float x, float y, float width, float height, int thickness) {
		graphics.render(renderer -> {
			for (int i=1 ; i<=thickness ; i++) {
				float left = x-i;
				float right = x+width+i;
				float top = y-i;
				float bottom = y+height+i;
				float bwidth = right - left;
				float bheight = bottom - top;

				renderer.color(GameConstants.DARK_GREY)
						.fillRectangle2D(left, top, bwidth-1, 1)
						.fillRectangle2D(left, top, 1, bheight-1)
						.color(GameConstants.WHITE)
						.fillRectangle2D(left+1, bottom-1, bwidth-1, 1)
						.fillRectangle2D(right-1, top+1, 1, bheight-1)
						.color(GameConstants.GREY)
						.fillRectangle2D(right-1, top, 1, 1)
						.fillRectangle2D(left, bottom-1, 1, 1);
			}
		});
	}
	
}
