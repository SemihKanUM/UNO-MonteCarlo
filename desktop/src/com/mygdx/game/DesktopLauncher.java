package com.mygdx.game;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import java.awt.Dimension;
import java.awt.Toolkit;
import Logic.Board;
import Logic.Launch;
import Logic.MainFile;
public class DesktopLauncher {
	{}
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		
	{
		for (Graphics.DisplayMode mode : Lwjgl3ApplicationConfiguration.getDisplayModes(Lwjgl3ApplicationConfiguration.getPrimaryMonitor()))
		{
			if ((mode.width == 1920 && mode.height == 1080)||(mode.width == 2048 && mode.height == 1536)|| (mode.width == 2560 && mode.height == 1600))
			{
				config.setFullscreenMode(mode);
				break;
			}

		}
		config.setTitle("UNO");
				new Lwjgl3Application(new MainFile(), config);

		}
	}
}
