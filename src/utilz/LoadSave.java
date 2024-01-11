package utilz;

import static utilz.Constants.EnemyConstants.GOBLIN;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entities.Goblin;
import main.Game;

public class LoadSave {

	public static final String PLAYER_ATLAS = "/res/player_sprites.png";
	public static final String LEVEL_ATLAS = "/res/outside_sprites.png";
	public static final String LEVEL_ONE_DATA = "/res/1.png";
	public static final String MENU_BUTTONS = "/res/button_atlas.png";
	public static final String MENU_BACKGROUND = "/res/menu_background.png";
	public static final String PAUSE_BACKGROUND = "/res/pause_menu.png";
	public static final String SOUND_BUTTONS = "/res/sound_button.png";
	public static final String URM_BUTTONS = "/res/urm_buttons.png";
	public static final String VOLUME_BUTTONS = "/res/volume_buttons.png";
	public static final String MENU_BACKGROUND_IMG = "/res/background_menu.png";
	public static final String PLAYING_BG_IMG = "/res/playing_bg_img.png";
	public static final String BIG_CLOUDS = "/res/big_clouds.png";
	public static final String SMALL_CLOUDS = "/res/small_clouds.png";
	public static final String GOBLIN_SPRİTE = "/res/goblin_sprites.png";
	public static final String STATUS_BAR = "/res/health_power_bar.png";
	public static final String DEATH_SCREEN = "/res/death_screen.png";
	public static final String OPTIONS_MENU = "/res/options_background.png";


	public static BufferedImage GetSpriteAtlas(String fileName) {
		BufferedImage img = null;
		InputStream is = LoadSave.class.getResourceAsStream(fileName);
		try {
			img = ImageIO.read(is);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return img;
	}

	public static ArrayList<Goblin> GetGoblins() {
		BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
		ArrayList<Goblin> list = new ArrayList<>();

		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getGreen();
				if (value == GOBLIN)
					list.add(new Goblin(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
			}
		return list;

	}

	public static int[][] GetLevelData() {
		BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
		int[][] lvlData = new int[img.getHeight()][img.getWidth()];

		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getRed();
				if (value >= 48)
					value = 0;
				lvlData[j][i] = value;
			}
		return lvlData;

	}
}