package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silence;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WaveCasterSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.utils.Random;

public class TiacauhRitualist extends Mob{
    private static final float TIME_TO_ZAP	= 1f;

    {
        spriteClass = WaveCasterSprite.class;

        HP = HT = 115;
        defenseSkill = 23;

        EXP = 16;
        maxLvl = 35;

        loot = Generator.Category.WAND;
        lootChance = 0.125f;

    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 36, 48 );
    }

    @Override
    public int attackSkill( Char target ) {
        return 40;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(0, 16);
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
    }

    //used so resistances can differentiate between melee and magical attacks
    public static class TiacauhBolat{}

    @Override
    protected boolean doAttack( Char enemy ) {

        if (Dungeon.level.distance( pos, enemy.pos ) <= 1) {

            return super.doAttack( enemy );

        } else if (buff(Silence.class) != null) {

            spend( TIME_TO_ZAP );

            if (hit( this, enemy, true )) {
                int dmg = Random.NormalIntRange(12, 18);
                enemy.damage( dmg, new TiacauhRitualist.TiacauhBolat() );

                if (enemy.sprite.visible) {
                    enemy.sprite.flash();
                }

                if (enemy == Dungeon.hero) {

                    Camera.main.shake( 2, 0.3f );

                    if (!enemy.isAlive()) {
                        Dungeon.fail( getClass() );
                        GLog.n( Messages.get(TiacauhRitualist.class, "zap_kill") );
                    }
                }
            } else {
                enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
            }

            if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                sprite.zap( enemy.pos );
                return false;
            } else {
                return true;
            }
        }
        else return false;
    }

    public void onZapComplete() {
        next();
    }

    public void call() {
        next();
    }
}