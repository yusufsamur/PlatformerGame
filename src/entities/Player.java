package entities;


import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;
import static utilz.Constants.GRAVITY;

import java.awt.Color;
import java.awt.Graphics;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import gamestates.Playing;
import main.Game;
import utilz.LoadSave;

public class Player extends Entity {
	private BufferedImage[][] animations;
	private int aniSpeed = 15;
	private int playerAction = IDLE;
	private boolean moving = false, attacking = false;
	private boolean left, right, jump;

	private int[][] lvlData;
	private float xDrawOffset = 16 * Game.SCALE;
	private float yDrawOffset = 7.6f * Game.SCALE;

	// zıplama / yerçekimi

	private float jumpSpeed = -2.25f * Game.SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.SCALE;

	// can barı arayüzü
	private BufferedImage statusBarImg;

	private int statusBarWidth = (int) (192 * Game.SCALE);
	private int statusBarHeight = (int) (58 * Game.SCALE);
	private int statusBarX = (int) (10 * Game.SCALE);
	private int statusBarY = (int) (10 * Game.SCALE);

	private int healthBarWidth = (int) (150 * Game.SCALE);
	private int healthBarHeight = (int) (4 * Game.SCALE);
	private int healthBarXStart = (int) (34 * Game.SCALE);
	private int healthBarYStart = (int) (14 * Game.SCALE);

	private int healthWidth = healthBarWidth;


	private int flipX = 0; // karakterin yürüyüşüne göre yüzünün döndüğü yeri değiştircez( updatePos'ta
							// yapıcaz)
	private int flipW = 1;

	private boolean attackChecked = false;
	private Playing playing;

	public Player(float x, float y, int width, int height, Playing playing) {
		super(x, y, width, height);
		this.maxHealth = 100;
		this.currentHealth = maxHealth;
		this.walkSpeed = 1.0f * Game.SCALE;
		this.playing = playing;
		loadAnimations();
		initHitbox(15, 27);
		initAttackBox();

	}

	private void initAttackBox() {

		attackBox = new Rectangle2D.Float(x, y, (int) (35 * Game.SCALE), (int) (20 * Game.SCALE));
	}

	public void update() {

		updateHealthBar();
		if (currentHealth <= 0) {
			if (state != DEATH) {
				state = DEATH;
				aniTick = 0;
				aniIndex = 0;
				playing.setPlayerDying(true);
				playing.setGameOver(true);
			} else
				updateAnimationTick();
			return;
		}
		updateAttackBox();

		updatePos();
		if (attacking)
			checkAttack();
		updateAnimationTick();
		setAnimation();
	}

	private void checkAttack() {

		if (attackChecked || aniIndex != 5) // ana karakterin saldırı çiziminin olduğu index 
			return;
		attackChecked = true;
		playing.checkEnemyHit(attackBox);

	}

	private void updateAttackBox() {
		if (right) {
			attackBox.x = hitbox.x + hitbox.width - (int) (Game.SCALE * 4); 

		} else if (left) {
			attackBox.x = hitbox.x - hitbox.width - (int) (Game.SCALE * 14.5); 

		}
		attackBox.y = hitbox.y + (Game.SCALE * 10);

	}

	private void updateHealthBar() {
		healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);

	}

	public void render(Graphics g, int lvlOffset) {
		g.drawImage(animations[playerAction][aniIndex], (int) (hitbox.x - xDrawOffset) - lvlOffset + flipX,
				(int) (hitbox.y - yDrawOffset), width * flipW, height, null);
		//  drawHitbox(g, lvlOffset);   //TEST İÇİN KULLANILABİLİR
		//  drawAttackBox(g, lvlOffset);

		drawUI(g);
	}

	

	private void drawUI(Graphics g) {

		g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
		g.setColor(Color.red);
		g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
	}

	private void updateAnimationTick() {
		aniTick++;
		if (aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(playerAction)) {
				aniIndex = 0;
				attacking = false;
				attackChecked = false;
			}

		}

	}

	private void setAnimation() {
		int startAni = playerAction;

		if (moving)
			playerAction = RUNNING;
		else
			playerAction = IDLE;

		if (inAir) {
			if (airSpeed < 0)
				playerAction = JUMP;
			else
				playerAction = FALL;
		}

		if (attacking) {
			playerAction = ATTACK;
			if (startAni != ATTACK) {
				aniIndex = 3; // saldırı animasyonunun başlayacağı çizim
				aniTick = 0;
				return;
			}
		}
		if (startAni != playerAction)
			resetAniTick();
	}

	private void resetAniTick() {
		aniTick = 0;
		aniIndex = 0;
	}

	private void updatePos() {
		moving = false;

		if (jump)
			jump();

		if (!inAir)
			if ((!left && !right) || (right && left))
				return;

		float xSpeed = 0;

		if (left) {
			xSpeed -= walkSpeed;
			flipX = (int) (width - xDrawOffset); 
			flipW = -1;
		}

		if (right) {
			xSpeed += walkSpeed;
			flipX = 0;
			flipW = 1;
		}

		if (!inAir)
			if (!IsEntityOnFloor(hitbox, lvlData))
				inAir = true;

		if (inAir) {
			if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
				hitbox.y += airSpeed;
				airSpeed += GRAVITY;
				updateXPos(xSpeed);
			} else {
				hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
				if (airSpeed > 0)
					resetInAir();
				else
					airSpeed = fallSpeedAfterCollision;
				updateXPos(xSpeed);
			}

		} else
			updateXPos(xSpeed);
		moving = true;
	}

	private void jump() {
		if (inAir)
			return;
		inAir = true;
		airSpeed = jumpSpeed;

	}

	private void resetInAir() {
		inAir = false;
		airSpeed = 0;

	}

	private void updateXPos(float xSpeed) {
		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
			hitbox.x += xSpeed;
		} else {
			hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
		}

	}

	public void changeHealth(int value) {
		currentHealth += value;
		if (currentHealth <= 0) {
			currentHealth = 0;
		} else if (currentHealth >= maxHealth) {
			currentHealth = maxHealth;
		}
	}

	private void loadAnimations() {

		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

		animations = new BufferedImage[8][12];
		for (int j = 0; j < animations.length; j++)
			for (int i = 0; i < animations[j].length; i++)
				animations[j][i] = img.getSubimage(i * 69, j * 44, 69, 44);

		statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);

	}

	public void loadLvlData(int[][] lvlData) {
		this.lvlData = lvlData;
		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;

	}

	public void resetDirBooleans() {
		left = false;
		right = false;

	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public void setJump(boolean jump) {
		this.jump = jump;
	}

	public void resetAll() {
		resetDirBooleans();
		inAir = false;
		attacking = false;
		moving = false;
		playerAction = IDLE;
		currentHealth = maxHealth;
		hitbox.x = x;
		hitbox.y = y;

		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;

	}

}