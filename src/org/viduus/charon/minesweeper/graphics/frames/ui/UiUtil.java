/**
 * Copyright 2018, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Feb 22, 2018 by Ethan Toney
 */
package org.viduus.charon.minesweeper.graphics.frames.ui;

import org.viduus.charon.global.graphics.core.AbstractGraphics;
import org.viduus.charon.global.graphics.core.render.AbstractRenderer;
import org.viduus.charon.global.graphics.core.shaders.AbstractShaderProgram;
import org.viduus.charon.global.graphics.opengl.shapes.Rectangle;
import org.viduus.charon.global.graphics.util.Color;
import org.viduus.charon.global.util.logging.ErrorHandler;

/**
 * @author ethan
 *
 */
public class UiUtil {

	public static final Color
		DARK_GREY = new Color(0.5019607843f, 0.5019607843f, 0.5019607843f, 1),
		GREY = new Color(0.7490196078f, 0.7490196078f, 0.7490196078f, 1),
		WHITE = new Color(1, 1, 1, 1);
	
	public static void renderBorder(AbstractGraphics graphics, float x, float y, float width, float height, int thickness) {
		ErrorHandler.tryRunThrow(() -> {
			AbstractRenderer renderer = graphics.renderer();
			renderer.useShader("opengl_frame");
			
			AbstractShaderProgram program = graphics.shader_manager.getActiveShader();
			
			for (int i=1 ; i<=thickness ; i++) {
				float left = x-i;
				float right = x+width+i;
				float top = y-i;
				float bottom = y+height+i;
				float bwidth = right - left;
				float bheight = bottom - top;
				
				program.getUniformVariable("set_Color").setValue(DARK_GREY);
				Rectangle.fillRectangle(graphics, "in_Position", left, top, bwidth-1, 1);
				Rectangle.fillRectangle(graphics, "in_Position", left, top, 1, bheight-1);
				
				program.getUniformVariable("set_Color").setValue(WHITE);
				Rectangle.fillRectangle(graphics, "in_Position", left+1, bottom-1, bwidth-1, 1);
				Rectangle.fillRectangle(graphics, "in_Position", right-1, top+1, 1, bheight-1);
				
				program.getUniformVariable("set_Color").setValue(GREY);
				Rectangle.fillRectangle(graphics, "in_Position", right-1, top, 1, 1);
				Rectangle.fillRectangle(graphics, "in_Position", left, bottom-1, 1, 1);
			}
		});
	}
	
}
