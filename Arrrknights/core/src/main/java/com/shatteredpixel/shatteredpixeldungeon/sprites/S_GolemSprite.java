/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM200;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class S_GolemSprite extends MobSprite {

	public S_GolemSprite() {
		super();

		texture( Assets.Sprites.S_GOLEM );

		TextureFilm frames = new TextureFilm( texture, 50, 50 );

		idle = new Animation( 10, true );
		idle.frames( frames, 0, 1 );

		run = new Animation( 10, true );
		run.frames( frames, 2, 3 );

		attack = new Animation( 15, false );
		attack.frames( frames, 1, 2,3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 );

		zap = new Animation( 15, false );
		zap.frames( frames, 1, 2,3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 );

		die = new Animation( 8, false );
		die.frames( frames, 9, 10, 11 );

		play( idle );
	}

	public void zap( int cell ) {

		turnTo( ch.pos , cell );
		play( zap );

		MagicMissile.boltFromChar( parent,
				MagicMissile.TOXIC_VENT,
				this,
				cell,
				new Callback() {
					@Override
					public void call() {
						((DM200)ch).onZapComplete();
					}
				} );
		Sample.INSTANCE.play( Assets.Sounds.GAS );
		GLog.w(Messages.get(DM200.class, "vent"));
	}

	@Override
	public void place(int cell) {
		if (parent != null) parent.bringToFront(this);
		super.place(cell);
	}

	@Override
	public void die() {
		emitter().burst( Speck.factory( Speck.WOOL ), 8 );
		super.die();
	}

	@Override
	public void onComplete( Animation anim ) {
		if (anim == zap) {
			idle();
		}
		super.onComplete( anim );
	}

	@Override
	public int blood() {
		return 0xFFFFFF88;
	}

}