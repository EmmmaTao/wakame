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
 */ /*
 *  Authors:
 *  	Ivaylo Boyadzhiev <iboy@cs.cornell.edu>
 *     Pramook Khungurn <pramook@cs.cornell.edu>
 *
 *  Credits:
 *  	Some part of the code is shamelessly stolen from
 *  	http://stackoverflow.com/questions/4862438/jogl-navigate-scene-with-keyboard
 */
package yumyai.jogl.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.swing.JFrame;
import javax.swing.Timer;

public abstract class GLFrame
        extends JFrame
        implements GLController, ActionListener
{
    private static final long serialVersionUID = 1L;
    protected int initialWidth;
    protected int initialHeight;
    protected int initialFrameRate;
    protected GLView glView;
    protected Timer timer;

    public GLFrame(String title, int width, int height, int frameRate)
    {
        super(title);

        initialWidth = width;
        initialHeight = height;
        initialFrameRate = frameRate;

        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent windowevent)
            {
                terminate();
            }
        });

        GLProfile glProfile = GLProfile.getDefault();
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);
        glView = GLViewUtil.create(glCapabilities);
        glView.addGLController(this);

        timer = new Timer(1000 / initialFrameRate, this);

        getContentPane().add((Component)glView, BorderLayout.CENTER);
    }

    public void run()
    {
        setSize(initialWidth, initialHeight);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void init(GLAutoDrawable drawable)
    {
        final GL2 gl = drawable.getGL().getGL2();

        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        gl.glEnable(GL2.GL_COLOR_MATERIAL);

        // Set depth buffer.
        gl.glClearDepth(1.0f);
        gl.glDepthFunc(GL2.GL_LESS);
        gl.glEnable(GL2.GL_DEPTH_TEST);

        // Set blending mode.
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL2.GL_BLEND);

        // Forces OpenGL to normalize transformed normals to be of
        // unit length before using the normals in OpenGL's lighting equations.
        gl.glEnable(GL2.GL_NORMALIZE);

        // Cull back faces.
        gl.glEnable(GL2.GL_CULL_FACE);

        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);

        startAnimation();
    }

    public abstract void display(GLAutoDrawable drawable);

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged)
    {
        // NO-OP
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
    {
        // NO-OP
    }

    public void keyTyped(KeyEvent key)
    {
        // NO-OP
    }

    public void keyPressed(KeyEvent key)
    {
        switch (key.getKeyCode())
        {
            case KeyEvent.VK_ESCAPE:
                terminate();
            default:
                break;
        }
    }

    public void keyReleased(KeyEvent key)
    {
        // NO-OP
    }

    public void mouseClicked(MouseEvent mouse)
    {
        // NO-OP
    }

    public void mousePressed(MouseEvent mouse)
    {
        // NO-OP
    }

    public void mouseReleased(MouseEvent mouse)
    {
        // NO-OP
    }

    public void mouseEntered(MouseEvent mouse)
    {
        // NO-OP
    }

    public void mouseExited(MouseEvent mouse)
    {
        // NO-OP
    }

    public void mouseDragged(MouseEvent mouse)
    {
        // NO-OP
    }

    public void mouseMoved(MouseEvent e)
    {
        // NO-OP
    }

    public void dispose(GLAutoDrawable drawable)
    {
        // NO-OP
    }

    public void startAnimation()
    {
        timer.start();
    }

    public void stopAnimation()
    {
        timer.stop();
    }

    public void addGLController(GLController controller)
    {
        glView.addGLController(controller);
    }

    protected void terminate()
    {
        new Thread()
        {
            public void run()
            {
                timer.stop();
            }
        }.start();
        dispose();
        System.exit(0);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == timer)
        {
            glView.repaint();
        }
    }
}