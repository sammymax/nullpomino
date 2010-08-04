/*
    Copyright (c) 2010, NullNoname
    All rights reserved.

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions are met:

        * Redistributions of source code must retain the above copyright
          notice, this list of conditions and the following disclaimer.
        * Redistributions in binary form must reproduce the above copyright
          notice, this list of conditions and the following disclaimer in the
          documentation and/or other materials provided with the distribution.
        * Neither the name of NullNoname nor the names of its
          contributors may be used to endorse or promote products derived from
          this software without specific prior written permission.

    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
    AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
    IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
    ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
    LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
    CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
    SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
    INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
    CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
    ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
    POSSIBILITY OF SUCH DAMAGE.
*/
package org.game_host.hebo.nullpomino.game.component;

import java.io.Serializable;

import org.game_host.hebo.nullpomino.game.play.GameEngine;

/**
 * ブロック
 */
public class Block implements Serializable {
	/** シリアルバージョンID */
	private static final long serialVersionUID = -7126899262733374545L;

	/** ブロックの色の定数 */
	public static final int BLOCK_COLOR_INVALID = -1,
							BLOCK_COLOR_NONE = 0,
							BLOCK_COLOR_GRAY = 1,
							BLOCK_COLOR_RED = 2,
							BLOCK_COLOR_ORANGE = 3,
							BLOCK_COLOR_YELLOW = 4,
							BLOCK_COLOR_GREEN = 5,
							BLOCK_COLOR_CYAN = 6,
							BLOCK_COLOR_BLUE = 7,
							BLOCK_COLOR_PURPLE = 8,
							BLOCK_COLOR_GEM_RED = 9,
							BLOCK_COLOR_GEM_ORANGE = 10,
							BLOCK_COLOR_GEM_YELLOW = 11,
							BLOCK_COLOR_GEM_GREEN = 12,
							BLOCK_COLOR_GEM_CYAN = 13,
							BLOCK_COLOR_GEM_BLUE = 14,
							BLOCK_COLOR_GEM_PURPLE = 15,
							BLOCK_COLOR_SQUARE_GOLD_1 = 16,
							BLOCK_COLOR_SQUARE_GOLD_2 = 17,
							BLOCK_COLOR_SQUARE_GOLD_3 = 18,
							BLOCK_COLOR_SQUARE_GOLD_4 = 19,
							BLOCK_COLOR_SQUARE_GOLD_5 = 20,
							BLOCK_COLOR_SQUARE_GOLD_6 = 21,
							BLOCK_COLOR_SQUARE_GOLD_7 = 22,
							BLOCK_COLOR_SQUARE_GOLD_8 = 23,
							BLOCK_COLOR_SQUARE_GOLD_9 = 24,
							BLOCK_COLOR_SQUARE_SILVER_1 = 25,
							BLOCK_COLOR_SQUARE_SILVER_2 = 26,
							BLOCK_COLOR_SQUARE_SILVER_3 = 27,
							BLOCK_COLOR_SQUARE_SILVER_4 = 28,
							BLOCK_COLOR_SQUARE_SILVER_5 = 29,
							BLOCK_COLOR_SQUARE_SILVER_6 = 30,
							BLOCK_COLOR_SQUARE_SILVER_7 = 31,
							BLOCK_COLOR_SQUARE_SILVER_8 = 32,
							BLOCK_COLOR_SQUARE_SILVER_9 = 33,
							BLOCK_COLOR_RAINBOW = 34,
							BLOCK_COLOR_GEM_RAINBOW = 35;

	/** アイテムの定数 */
	public static final int BLOCK_ITEM_NONE = 0,
							BLOCK_ITEM_RANDOM = 1;

	/** 通常のブロックの色の最大数 */
	public static final int BLOCK_COLOR_COUNT = 9;

	/** 通常＋宝石ブロックの色の最大数 */
	public static final int BLOCK_COLOR_EXT_COUNT = 16;

	/** ブロック表示あり */
	public static final int BLOCK_ATTRIBUTE_VISIBLE = 1;

	/** 枠線表示あり */
	public static final int BLOCK_ATTRIBUTE_OUTLINE = 2;

	/** 骨ブロック */
	public static final int BLOCK_ATTRIBUTE_BONE = 4;

	/** 上のブロックと繋がっている */
	public static final int BLOCK_ATTRIBUTE_CONNECT_UP = 8;

	/** 下のブロックと繋がっている */
	public static final int BLOCK_ATTRIBUTE_CONNECT_DOWN = 16;

	/** 左のブロックと繋がっている */
	public static final int BLOCK_ATTRIBUTE_CONNECT_LEFT = 32;

	/** 右のブロックと繋がっている */
	public static final int BLOCK_ATTRIBUTE_CONNECT_RIGHT = 64;

	/** 自分で置いたブロック */
	public static final int BLOCK_ATTRIBUTE_SELFPLACED = 128;

	/** 壊れたピースの一部分 */
	public static final int BLOCK_ATTRIBUTE_BROKEN = 256;

	/** 邪魔ブロック */
	public static final int BLOCK_ATTRIBUTE_GARBAGE = 512;

	/** 壁 */
	public static final int BLOCK_ATTRIBUTE_WALL = 1024;

	/** 消える予定のブロック */
	public static final int BLOCK_ATTRIBUTE_ERASE = 2048;

	/** Temporary mark for block linking check algorithm */
	public static final int BLOCK_ATTRIBUTE_TEMP_MARK = 4096;

	/** "Block has fallen" flag for cascade gravity */
	public static final int BLOCK_ATTRIBUTE_CASCADE_FALL = 8192;

	/** Anti-gravity flag (The block will not fall by gravity) */
	public static final int BLOCK_ATTRIBUTE_ANTIGRAVITY = 16384;

	/** ブロックの色 */
	public int color;

	/** ブロックの絵柄 */
	public int skin;

	/** ブロックの属性 */
	public int attribute;

	/** 固定してから経過したフレーム数 */
	public int elapsedFrames;

	/** ブロックの暗さ、または明るさ（0.03だったら3%暗く、-0.05だったら5%明るい） */
	public float darkness;

	/** 透明度（1.0fで不透明、0.0fで完全に透明） */
	public float alpha;

	/** ゲームが始まってから何番目に置いたブロックか（負数だったら初期配置や邪魔ブロック） */
	public int pieceNum;

	/** アイテム番号 */
	public int item;
	
	/** Number of extra clears required before block is erased */
	public int hard;
	
	/** Color-shift phase for rainbow blocks */
	public static int rainbowPhase = 0;
	
	/** Color to turn into when garbage block turns into a regular block */
	public int secondaryColor = 0;

	/**
	 * コンストラクタ
	 */
	public Block() {
		reset();
	}

	/**
	 * 色指定可能なコンストラクタ
	 * @param color ブロックの色
	 */
	public Block(int color) {
		reset();
		this.color = color;
	}

	/**
	 * 色と絵柄の指定が可能なコンストラクタ
	 * @param color ブロックの色
	 * @param skin ブロックの絵柄
	 */
	public Block(int color, int skin) {
		reset();
		this.color = color;
		this.skin = skin;
	}

	/**
	 * 色と絵柄と属性の指定が可能なコンストラクタ
	 * @param color ブロックの色
	 * @param skin ブロックの絵柄
	 * @param attribute ブロックの属性
	 */
	public Block(int color, int skin, int attribute) {
		reset();
		this.color = color;
		this.skin = skin;
		this.attribute = attribute;
	}

	/**
	 * コピーコンストラクタ
	 * @param b コピー元
	 */
	public Block(Block b) {
		copy(b);
	}

	/**
	 * 設定を初期値に戻す
	 */
	public void reset() {
		color = BLOCK_COLOR_NONE;
		skin = 0;
		attribute = 0;
		elapsedFrames = 0;
		darkness = 0f;
		alpha = 1f;
		pieceNum = -1;
		item = 0;
		hard = 0;
		secondaryColor = 0;
	}

	/**
	 * 設定を他のBlockからコピー
	 * @param b コピー元
	 */
	public void copy(Block b) {
		color = b.color;
		skin = b.skin;
		attribute = b.attribute;
		elapsedFrames = b.elapsedFrames;
		darkness = b.darkness;
		alpha = b.alpha;
		pieceNum = b.pieceNum;
		item = b.item;
		secondaryColor = b.secondaryColor;
	}

	/**
	 * 指定した属性の状態を調べる
	 * @param attr 調べたい属性
	 * @return 指定した属性がすべてセットされている場合はtrue
	 */
	public boolean getAttribute(int attr) {
		return ((attribute & attr) != 0);
	}

	/**
	 * 属性を変更する
	 * @param attr 変更したい属性
	 * @param status 変更後の状態
	 */
	public void setAttribute(int attr, boolean status) {
		if(status) attribute |= attr;
		else attribute &= ~attr;
	}

	/**
	 * このブロックが空白かどうか判定
	 * @return このブロックが空白だったらtrue
	 */
	public boolean isEmpty() {
		return (color < BLOCK_COLOR_GRAY);
	}

	/**
	 * このブロックが宝石ブロックかどうか判定
	 * @return このブロックが宝石ブロックだったらtrue
	 */
	public boolean isGemBlock() {
		return ((color >= BLOCK_COLOR_GEM_RED) && (color <= BLOCK_COLOR_GEM_PURPLE)) ||
				(color == BLOCK_COLOR_GEM_RAINBOW);
	}

	/**
	 * Checks to see if <code>this</code> is a gold square block
	 * @return <code>true</code> if the block is a gold square block
	 */
	public boolean isGoldSquareBlock() {
		return (color >= BLOCK_COLOR_SQUARE_GOLD_1) && (color <= BLOCK_COLOR_SQUARE_GOLD_9);
	}

	/**
	 * Checks to see if <code>this</code> is a silver square block
	 * @return <code>true</code> if the block is a silver square block
	 */
	public boolean isSilverSquareBlock() {
		return (color >= BLOCK_COLOR_SQUARE_SILVER_1) && (color <= BLOCK_COLOR_SQUARE_SILVER_9);
	}

	/**
	 * Checks to see if <code>this</code> is a normal block (gray to purple)
	 * @return <code>true</code> if the block is a normal block
	 */
	public boolean isNormalBlock() {
		return (color >= BLOCK_COLOR_GRAY) && (color <= BLOCK_COLOR_PURPLE);
	}
	
	public int getDrawColor() {
		if (color == BLOCK_COLOR_GEM_RAINBOW)
			return BLOCK_COLOR_GEM_RED + (rainbowPhase/3);
		else if (color == BLOCK_COLOR_RAINBOW)
			return BLOCK_COLOR_RED + (rainbowPhase/3);
		else
			return color;
	}

	public static void updateRainbowPhase(int time) {
		rainbowPhase = time%21;
	}

	public static void updateRainbowPhase(GameEngine engine) {
		if (engine != null && engine.timerActive)
			updateRainbowPhase(engine.statistics.time);
		else
		{
			rainbowPhase++;
			if (rainbowPhase >= 21)
				rainbowPhase = 0;
		}
	}
}
