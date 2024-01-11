package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.Game;

public abstract class Entity {

	protected float x, y;
	protected int width, height;
	protected int state;
	protected Rectangle2D.Float hitbox;
	protected int aniTick, aniIndex;
	protected float airSpeed;
	protected boolean inAir = false;
	protected int maxHealth = 100;
	protected int currentHealth = maxHealth;
	protected Rectangle2D.Float attackBox;
	protected float walkSpeed;

	public Entity(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

	}

	// hitbox testi için
	protected void drawHitbox(Graphics g, int xLvlOffset) {
		g.setColor(Color.PINK);
		g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);

	}

	protected void drawAttackBox(Graphics graph, int xLvlOffset) {
		graph.setColor(Color.red);
		graph.drawRect((int) (attackBox.x - xLvlOffset), (int) attackBox.y, (int) attackBox.width,
				(int) attackBox.height);

	}

	protected void initHitbox(int width, int height) {
		hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
	}

	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}

	public int getAniIndex() {
		return aniIndex;
	}

}