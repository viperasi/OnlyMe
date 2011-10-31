/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xu81.onlyme;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

import com.xu81.onlyme.view.MainFrame;

/**
 *
 * @author xu
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        MainFrame myWindow = new MainFrame();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        myWindow.setBounds(0, 0, (int)screen.getWidth(), (int)screen.getHeight());
        myWindow.setAlwaysOnTop(true);
        myWindow.setUndecorated(true);
        myWindow.setResizable(false);
        myWindow.setVisible(true);
    }
}
