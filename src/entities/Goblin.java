package entities;

import static utilz.Constants.EnemyConstants.*;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.Directions.*;

import main.Game;

public class Goblin extends Enemy {

    public Goblin(float x, float y) {
        super(x, y, GOBLIN_WIDTH, GOBLIN_HEIGHT, GOBLIN);

        initHitbox(22, 31);
        initAttackBox();

    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (50 * Game.SCALE), (int) (30 * Game.SCALE));

    }

    public void update(int[][] lvlData, Player player) {
        updateBehavior(lvlData, player);
        updateAnimationTick();
        updateAttackBox();

    }

    private void updateAttackBox() {
        if (walkDir == RIGHT) {
            attackBox.x = hitbox.x + hitbox.width - (int) (Game.SCALE * 15);

        } else if (walkDir == LEFT) {
            attackBox.x = hitbox.x - hitbox.width - (int) (Game.SCALE * 15);

        }
        attackBox.y = hitbox.y + (Game.SCALE * 10);

    }

    private void updateBehavior(int[][] lvlData, Player player) {
        if (firstUpdate)
            firstUpdateCheck(lvlData);

        if (inAir)
            updateInAir(lvlData);

        else {
            switch (enemyState) {
                case IDLE:
                    newState(RUNNING);
                    break;
                case RUNNING:
                    if (canSeePlayer(lvlData, player)) {
                        turnTowardsPlayer(player);
                        if (isPlayerCloseForAttack(player))
                            newState(ATTACK);
                    }
                    move(lvlData);
                    break;
                case ATTACK:
                    if (aniIndex == 0) {
                        attackChecked = false;
                    }

                    if (aniIndex == 6 && !attackChecked) {
                        checkEnemyHit(attackBox, player);

                    }

                    break;
                case HIT:
                    break;

            }

        }

    }

    public int flipX() {
        if (walkDir == LEFT)
            return width;
        else
            return 0;

    }

    public int flipW() {
        if (walkDir == LEFT)
            return -1;
        else
            return 1;

    }

}
