package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Silence;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith_donut;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CustomeSet;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.Rock_CrabSprite;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class ImageoverForm extends MeleeWeapon {
    {
        image = ItemSpriteSheet.VANILLA_AXE;
        hitSound = Assets.Sounds.HIT_SLASH;
        hitSoundPitch = 1f;

        tier = 5;
    }

    @Override
    public int min(int lvl) {
        if (Dungeon.hero.lvl >= 21) {
            return tier + lvl + damagebouns();
        }
        return tier + lvl;
    }

    @Override
    public int max(int lvl) {
        if (Dungeon.hero.lvl >= 21)
            return  5*(tier-1) +    //20 + 5
                    lvl*(tier);   //scaling unchanged
        else return  4*(tier-1) +    //16 + 4
                    lvl*(tier-1);   //scaling unchanged
    }

    @Override
    public String desc() {
        if (Dungeon.hero.lvl >= 28) return Messages.get(this, "desc_up2");
        else if (Dungeon.hero.lvl >= 21) return Messages.get(this, "desc_up1");
        else return Messages.get(this, "desc");
    }

    public int damagebouns() {
        int bouns = Statistics.enemiesSlain / 50;
        return Math.min(bouns, 15);
    }




    public static class LittleInstinct extends Mob {
        {
            spriteClass = Rock_CrabSprite.class;

            defenseSkill = 0;

            immunities.add(Silence.class);
            alignment = Alignment.ALLY;

            state = WANDERING;
        }

        int level = 0;

        public LittleInstinct (int setlvl)
        {
            CustomeSet.CustomSetBuff setBuff = Dungeon.hero.buff( CustomeSet.CustomSetBuff.class);
            int itembuff = 0;
            if (setBuff != null) itembuff = setBuff.itemLevel();
            HP=HT=30 + setlvl * 4 + itembuff * 3;
            level = setlvl + itembuff / 2;
        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange( 5, 6 + level );
        }

        @Override
        public int attackSkill( Char target ) {
            return 12 + level;
        }

        @Override
        public int drRoll() {
            return Random.NormalIntRange(0, 7 + level);
        }

        private static final String LVL = "level";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(LVL, level);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            level = bundle.getInt(LVL);
        }

    }
}