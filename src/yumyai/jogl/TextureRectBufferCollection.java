/*
 * This file is part of Wakame, a Java reimplementation of Nori, an educational ray tracer by Wenzel Jakob.
 *
 * Copyright (c) 2015 by Pramook Khungurn
 *
 * Wakame is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License Version 3
 * as published by the Free Software Foundation.
 *
 * Wakame is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package yumyai.jogl;

import javax.media.opengl.GL2;

public class TextureRectBufferCollection {
    private static int INTERNAL_FORMAT = GL2.GL_RGBA32F;
    public DoubleTextureRect[] colorBuffers;
    public TextureRect depthBuffer;
    public boolean hasDepthBuffer;

    public TextureRectBufferCollection(int colorBufferCount, boolean hasDepthBuffer) {
        colorBuffers = new DoubleTextureRect[colorBufferCount];
        if (hasDepthBuffer)
            this.hasDepthBuffer = hasDepthBuffer;
    }

    public void allocate(GL2 gl, int width, int height, int format, int type) {
        for (int i = 0; i < colorBuffers.length; i++) {
            if (colorBuffers[i] == null) {
                colorBuffers[i] = new DoubleTextureRect(gl, INTERNAL_FORMAT);
            }
            if (colorBuffers[i].getWidth() != width || colorBuffers[i].getHeight() != height) {
                colorBuffers[i].allocate(width, height, format, type);
            }
        }
        if (hasDepthBuffer) {
            if (depthBuffer == null) {
                depthBuffer = new TextureRect(gl, GL2.GL_DEPTH_COMPONENT32);
            }
            if (depthBuffer.getWidth() != width || depthBuffer.getHeight() != height) {
                depthBuffer.allocate(width, height, GL2.GL_DEPTH_COMPONENT, GL2.GL_UNSIGNED_INT);
            }
        }
    }

    public void allocate(GL2 gl, int width, int height) {
    	allocate(gl, width, height, GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE);
    }

    public void attachTo(Fbo fbo) {
        for (int i = 0; i < colorBuffers.length; i++) {
            fbo.attachColorBuffer(i, colorBuffers[i].getWriteBuffer());
        }
        if (hasDepthBuffer) {
            fbo.attachDepthBuffer(depthBuffer);
        }
    }

    public void swap() {
        for (int i = 0; i < colorBuffers.length; i++) {
            colorBuffers[i].swap();
        }
    }

    public void disposeGL() {
        for (int i = 0; i < colorBuffers.length; i++) {
            if (colorBuffers[i] != null)
                colorBuffers[i].disposeGL();
        }
        if (hasDepthBuffer && depthBuffer != null)
            depthBuffer.disposeGL();
    }
}
