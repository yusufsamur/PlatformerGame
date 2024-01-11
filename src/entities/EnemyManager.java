package entities;

import gamestates.Playing;
import utilz.LoadSave;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.*;

public class EnemyManager {

    private Playing playing;
    private BufferedImage[][] goblinArr;
    private ArrayList<Goblin> goblins = new ArrayList<>();

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
        addEnemies();

    }

    private void addEnemies() {

        goblins = LoadSave.GetGoblins();
        System.out.println("size of goblins : " + goblins.size());

    }

    public void update(int[][] lvlData, Player player) {
        for (Goblin g : goblins) {
            if (g.isActive())
                g.update(lvlData, player);
        }

    }

    public void draw(Graphics g, int xLvlOffset) {
        drawGoblins(g, xLvlOffset);

    }

    private void drawGoblins(Graphics graph, int xLvlOffset) {
        for (Goblin g : goblins)
            if (g.isActive()) {
                graph.drawImage(goblinArr[g.getEnemyState()][g.getAniIndex()],
                        (int) g.getHitbox().x - xLvlOffset - GOBLIN_DRAWOFFSET_X + g.flipX(),
                        (int) g.getHitbox().y - GOBLIN_DRAWOFFSET_Y,
                        GOBLIN_WIDTH * g.flipW(), GOBLIN_HEIGHT, null);
                //  g.drawHitbox(graph, xLvlOffset);    //TEST İÇİN KULLANILABİLİR
                //  g.drawAttackBox(graph, xLvlOffset);

            }
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        for (Goblin g : goblins)
            if (g.isActive()) {
                if (attackBox.intersects(g.getHitbox())) {
                    g.hurt(4);
                    return;
                }
            }

    }

    private void loadEnemyImgs() {
        goblinArr = new BufferedImage[5][8];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.GOBLIN_SPRİTE);
        for (int j = 0; j < goblinArr.length; j++)
            for (int i = 0; i < goblinArr[j].length; i++)
                goblinArr[j][i] = temp.getSubimage(i * GOBLIN_WIDTH_DEFAULT, j * GOBLIN_HEIGHT_DEFAULT,
                        GOBLIN_WIDTH_DEFAULT, GOBLIN_HEIGHT_DEFAULT);

    }

    public void resetAllEnemies() {
        for (Goblin g : goblins)
            g.resetEnemy();

    }

}
