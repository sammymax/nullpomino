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
package mu.nu.nullpo.gui.slick;

import mu.nu.nullpo.util.CustomProperties;
import mu.nu.nullpo.util.GeneralUtil;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * 全般の設定画面のステート
 */
public class StateConfigGeneral extends BasicGameState {
	/** This state's ID */
	public static final int ID = 6;

	/** UI Text identifier Strings */
	protected static final String[] UI_TEXT = {
		"ConfigGeneral_SE",
		"ConfigGeneral_BGM",
		"ConfigGeneral_BGMPreload",
		"ConfigGeneral_SEVolume",
		"ConfigGeneral_BGMVolume",
		"ConfigGeneral_Background",
		"ConfigGeneral_UseBackgroundFade",
		"ConfigGeneral_ShowLineEffect",
		"ConfigGeneral_LineEffectSpeed",
		"ConfigGeneral_ShowMeter",
		"ConfigGeneral_DarkNextArea",
		"ConfigGeneral_NextShadow",
		"ConfigGeneral_SideNext",
		"ConfigGeneral_BigSideNext",
		"ConfigGeneral_OutlineGhost",
		"ConfigGeneral_FieldBGBright",
		"ConfigGeneral_Fullscreen",
		"ConfigGeneral_ShowFPS",
		"ConfigGeneral_MaxFPS",
		"ConfigGeneral_FrameStep",
		"ConfigGeneral_BGMStreaming",
		"ConfigGeneral_VSync",
		"ConfigGeneral_AlternateFPSTiming",
		"ConfigGeneral_AlternateFPSDynamicAdjust",
		"ConfigGeneral_AlternateFPSPerfectMode",
	};

	/** Screenshot撮影 flag */
	protected boolean ssflag = false;

	/** Cursor position */
	protected int cursor = 0;

	/** フルスクリーン flag */
	protected boolean fullscreen;

	/** Sound effectsON/OFF */
	protected boolean se;

	/** BGMのON/OFF */
	protected boolean bgm;

	/** BGMの事前読み込み */
	protected boolean bgmpreload;

	/** BGMストリーミングのON/OFF */
	protected boolean bgmstreaming;

	/** Background表示 */
	protected boolean showbg;

	/** FPS表示 */
	protected boolean showfps;

	/**  frame ステップ is enabled */
	protected boolean enableframestep;

	/** MaximumFPS */
	protected int maxfps;

	/** Line clearエフェクト表示 */
	protected boolean showlineeffect;

	/** Line clear effect speed */
	protected int lineeffectspeed;

	/** 重い演出を使う */
	protected boolean heavyeffect;

	/** fieldBackgroundの明るさ */
	protected int fieldbgbright;

	/** NEXT欄を暗くする */
	protected boolean darknextarea;

	/** Sound effects volume */
	protected int sevolume;

	/** BGM volume */
	protected int bgmvolume;

	/** field右側にMeterを表示 */
	protected boolean showmeter;

	/** 垂直同期を待つ */
	protected boolean vsync;

	/** ghost ピースの上にNEXT表示 */
	protected boolean nextshadow;

	/** 枠線型ghost ピース */
	protected boolean outlineghost;

	/** Side piece preview */
	protected boolean sidenext;

	/** Bigger side piece preview */
	protected boolean bigsidenext;

	/** Timing of alternate FPS sleep (false=render true=update) */
	protected boolean alternateFPSTiming;

	/** Allow dynamic adjust of target FPS (as seen in Swing version) */
	protected boolean alternateFPSDynamicAdjust;

	/** Perfect FPS mode */
	protected boolean alternateFPSPerfectMode;

	/*
	 * Fetch this state's ID
	 */
	@Override
	public int getID() {
		return ID;
	}

	/*
	 * State initialization
	 */
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		loadConfig(NullpoMinoSlick.propConfig);
	}

	/**
	 * Load settings
	 * @param prop Property file to read from
	 */
	protected void loadConfig(CustomProperties prop) {
		fullscreen = prop.getProperty("option.fullscreen", false);
		se = prop.getProperty("option.se", true);
		bgm = prop.getProperty("option.bgm", false);
		bgmpreload = prop.getProperty("option.bgmpreload", false);
		bgmstreaming = prop.getProperty("option.bgmstreaming", true);
		showbg = prop.getProperty("option.showbg", true);
		showfps = prop.getProperty("option.showfps", true);
		enableframestep = prop.getProperty("option.enableframestep", false);
		maxfps = prop.getProperty("option.maxfps", 60);
		showlineeffect = prop.getProperty("option.showlineeffect", true);
		lineeffectspeed = prop.getProperty("option.lineeffectspeed", 0);
		heavyeffect = prop.getProperty("option.heavyeffect", false);
		fieldbgbright = prop.getProperty("option.fieldbgbright", 64);
		darknextarea = prop.getProperty("option.darknextarea", true);
		sevolume = prop.getProperty("option.sevolume", 128);
		bgmvolume = prop.getProperty("option.bgmvolume", 128);
		showmeter = prop.getProperty("option.showmeter", true);
		vsync = prop.getProperty("option.vsync", false);
		nextshadow = prop.getProperty("option.nextshadow", false);
		outlineghost = prop.getProperty("option.outlineghost", false);
		sidenext = prop.getProperty("option.sidenext", false);
		bigsidenext = prop.getProperty("option.bigsidenext", false);
		alternateFPSTiming = prop.getProperty("option.alternateFPSTiming", true);
		alternateFPSDynamicAdjust = prop.getProperty("option.alternateFPSDynamicAdjust", false);
		alternateFPSPerfectMode = prop.getProperty("option.alternateFPSPerfectMode", false);
	}

	/**
	 * Save settings
	 * @param prop Property file to save to
	 */
	protected void saveConfig(CustomProperties prop) {
		prop.setProperty("option.fullscreen", fullscreen);
		prop.setProperty("option.se", se);
		prop.setProperty("option.bgm", bgm);
		prop.setProperty("option.bgmpreload", bgmpreload);
		prop.setProperty("option.bgmstreaming", bgmstreaming);
		prop.setProperty("option.showbg", showbg);
		prop.setProperty("option.showfps", showfps);
		prop.setProperty("option.enableframestep", enableframestep);
		prop.setProperty("option.maxfps", maxfps);
		prop.setProperty("option.showlineeffect", showlineeffect);
		prop.setProperty("option.lineeffectspeed", lineeffectspeed);
		prop.setProperty("option.heavyeffect", heavyeffect);
		prop.setProperty("option.fieldbgbright", fieldbgbright);
		prop.setProperty("option.darknextarea", darknextarea);
		prop.setProperty("option.sevolume", sevolume);
		prop.setProperty("option.bgmvolume", bgmvolume);
		prop.setProperty("option.showmeter", showmeter);
		prop.setProperty("option.vsync", vsync);
		prop.setProperty("option.nextshadow", nextshadow);
		prop.setProperty("option.outlineghost", outlineghost);
		prop.setProperty("option.sidenext", sidenext);
		prop.setProperty("option.bigsidenext", bigsidenext);
		prop.setProperty("option.alternateFPSTiming", alternateFPSTiming);
		prop.setProperty("option.alternateFPSDynamicAdjust", alternateFPSDynamicAdjust);
		prop.setProperty("option.alternateFPSPerfectMode", alternateFPSPerfectMode);
	}

	/*
	 * Draw the screen
	 */
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		// Background
		g.drawImage(ResourceHolder.imgMenu, 0, 0);

		// Basic Options
		if(cursor < 16) {
			NormalFont.printFontGrid(1, 1, "GENERAL OPTIONS: BASIC (1/3)", NormalFont.COLOR_ORANGE);
			NormalFont.printFontGrid(1, 3 + cursor, "b", NormalFont.COLOR_RED);

			NormalFont.printFontGrid(2,  3, "SE:" + GeneralUtil.getOorX(se), (cursor == 0));
			NormalFont.printFontGrid(2,  4, "BGM:" + GeneralUtil.getOorX(bgm), (cursor == 1));
			NormalFont.printFontGrid(2,  5, "BGM PRELOAD:" + GeneralUtil.getOorX(bgmpreload), (cursor == 2));
			NormalFont.printFontGrid(2,  6, "SE VOLUME:" + sevolume, (cursor == 3));
			NormalFont.printFontGrid(2,  7, "BGM VOLUME:" + bgmvolume, (cursor == 4));
			NormalFont.printFontGrid(2,  8, "SHOW BACKGROUND:" + GeneralUtil.getOorX(showbg), (cursor == 5));
			NormalFont.printFontGrid(2,  9, "USE BACKGROUND FADE:" + GeneralUtil.getOorX(heavyeffect), (cursor == 6));
			NormalFont.printFontGrid(2, 10, "SHOW LINE EFFECT:" + GeneralUtil.getOorX(showlineeffect), (cursor == 7));
			NormalFont.printFontGrid(2, 11, "LINE EFFECT SPEED:" + "X " + (lineeffectspeed+1), (cursor == 8));
			NormalFont.printFontGrid(2, 12, "SHOW METER:" + GeneralUtil.getOorX(showmeter), (cursor == 9));
			NormalFont.printFontGrid(2, 13, "DARK NEXT AREA:" + GeneralUtil.getOorX(darknextarea), (cursor == 10));
			NormalFont.printFontGrid(2, 14, "SHOW NEXT ABOVE SHADOW:" + GeneralUtil.getOorX(nextshadow), (cursor == 11));
			NormalFont.printFontGrid(2, 15, "SHOW NEXT ON SIDE:" + GeneralUtil.getOorX(sidenext), (cursor == 12));
			NormalFont.printFontGrid(2, 16, "BIG SIDE NEXT:" + GeneralUtil.getOorX(bigsidenext), (cursor == 13));
			NormalFont.printFontGrid(2, 17, "OUTLINE GHOST PIECE:" + GeneralUtil.getOorX(outlineghost), (cursor == 14));
			NormalFont.printFontGrid(2, 18, "FIELD BG BRIGHT:" + fieldbgbright, (cursor == 15));
		}
		// Advanced Options
		else if(cursor < 20) {
			NormalFont.printFontGrid(1, 1, "GENERAL OPTIONS: ADVANCED (2/3)", NormalFont.COLOR_ORANGE);
			NormalFont.printFontGrid(1, 3 + (cursor - 16), "b", NormalFont.COLOR_RED);

			NormalFont.printFontGrid(2,  3, "FULLSCREEN:" + GeneralUtil.getOorX(fullscreen), (cursor == 16));
			NormalFont.printFontGrid(2,  4, "SHOW FPS:" + GeneralUtil.getOorX(showfps), (cursor == 17));
			NormalFont.printFontGrid(2,  5, "MAX FPS:" + maxfps, (cursor == 18));
			NormalFont.printFontGrid(2,  6, "FRAME STEP:" + GeneralUtil.getOorX(enableframestep), (cursor == 19));
		}
		// Slick Options
		else {
			NormalFont.printFontGrid(1, 1, "GENERAL OPTIONS: SLICK (3/3)", NormalFont.COLOR_ORANGE);
			NormalFont.printFontGrid(1, 3 + (cursor - 20), "b", NormalFont.COLOR_RED);

			NormalFont.printFontGrid(2,  3, "BGM STREAMING:" + GeneralUtil.getOorX(bgmstreaming), (cursor == 20));
			NormalFont.printFontGrid(2,  4, "VSYNC:" + GeneralUtil.getOorX(vsync), (cursor == 21));
			NormalFont.printFontGrid(2,  5, "FPS SLEEP TIMING:" + (alternateFPSTiming ? "UPDATE" : "RENDER"), (cursor == 22));
			NormalFont.printFontGrid(2,  6, "FPS DYNAMIC ADJUST:" + GeneralUtil.getOorX(alternateFPSDynamicAdjust), (cursor == 23));
			NormalFont.printFontGrid(2,  7, "FPS PERFECT MODE:" + GeneralUtil.getOorX(alternateFPSPerfectMode), (cursor == 24));
		}

		if((cursor >= 0) && (cursor < UI_TEXT.length)) NormalFont.printTTFFont(16, 432, NullpoMinoSlick.getUIText(UI_TEXT[cursor]));

		// FPS
		NullpoMinoSlick.drawFPS(container);
		// Observer
		NullpoMinoSlick.drawObserverClient();
		// Screenshot
		if(ssflag) {
			NullpoMinoSlick.saveScreenShot(container, g);
			ssflag = false;
		}

		if(!NullpoMinoSlick.alternateFPSTiming) NullpoMinoSlick.alternateFPSSleep();
	}

	/*
	 * Update game state
	 */
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if(!container.hasFocus()) {
			GameKey.gamekey[0].clear();
			if(NullpoMinoSlick.alternateFPSTiming) NullpoMinoSlick.alternateFPSSleep();
			return;
		}

		// TTF font 描画
		if(ResourceHolder.ttfFont != null) ResourceHolder.ttfFont.loadGlyphs();

		// Update key input states
		GameKey.gamekey[0].update(container.getInput());

		// Cursor movement
		if(GameKey.gamekey[0].isMenuRepeatKey(GameKey.BUTTON_UP)) {
		    cursor--;
			if(cursor < 0) cursor = 24;
			ResourceHolder.soundManager.play("cursor");
		}
		if(GameKey.gamekey[0].isMenuRepeatKey(GameKey.BUTTON_DOWN)) {
			cursor++;
			if(cursor > 24) cursor = 0;
			ResourceHolder.soundManager.play("cursor");
		}

		// Configuration changes
		int change = 0;
		if(GameKey.gamekey[0].isMenuRepeatKey(GameKey.BUTTON_LEFT)) change = -1;
		if(GameKey.gamekey[0].isMenuRepeatKey(GameKey.BUTTON_RIGHT)) change = 1;

		if(change != 0) {
			ResourceHolder.soundManager.play("change");

			switch(cursor) {
			case 0:
				se = !se;
				break;
			case 1:
				bgm = !bgm;
				break;
			case 2:
				bgmpreload = !bgmpreload;
				break;
			case 3:
				sevolume += change;
				if(sevolume < 0) sevolume = 128;
				if(sevolume > 128) sevolume = 0;
				break;
			case 4:
				bgmvolume += change;
				if(bgmvolume < 0) bgmvolume = 128;
				if(bgmvolume > 128) bgmvolume = 0;
				break;
			case 5:
				showbg = !showbg;
				break;
			case 6:
				heavyeffect = !heavyeffect;
				break;
			case 7:
				showlineeffect = !showlineeffect;
				break;
			case 8:
				lineeffectspeed += change;
				if(lineeffectspeed < 0) lineeffectspeed = 9;
				if(lineeffectspeed > 9) lineeffectspeed = 0;
				break;
			case 9:
				showmeter = !showmeter;
				break;
			case 10:
				darknextarea = !darknextarea;
				break;
			case 11:
				nextshadow = !nextshadow;
				break;
			case 12:
				sidenext = !sidenext;
				break;
			case 13:
				bigsidenext = !bigsidenext;
				break;
			case 14:
				outlineghost = !outlineghost;
				break;
			case 15:
				fieldbgbright += change;
				if(fieldbgbright < 0) fieldbgbright = 128;
				if(fieldbgbright > 128) fieldbgbright = 0;
				break;
			case 16:
				fullscreen = !fullscreen;
				break;
			case 17:
				showfps = !showfps;
				break;
			case 18:
				maxfps += change;
				if(maxfps < 0) maxfps = 99;
				if(maxfps > 99) maxfps = 0;
				break;
			case 19:
				enableframestep = !enableframestep;
				break;
			case 20:
				bgmstreaming = !bgmstreaming;
				break;
			case 21:
				vsync = !vsync;
				break;
			case 22:
				alternateFPSTiming = !alternateFPSTiming;
				break;
			case 23:
				alternateFPSDynamicAdjust = !alternateFPSDynamicAdjust;
				break;
			case 24:
				alternateFPSPerfectMode = !alternateFPSPerfectMode;
				break;
			}
		}

		// 決定 button
		if(GameKey.gamekey[0].isPushKey(GameKey.BUTTON_A)) {
			ResourceHolder.soundManager.play("decide");
			saveConfig(NullpoMinoSlick.propConfig);
			NullpoMinoSlick.saveConfig();
			NullpoMinoSlick.setGeneralConfig();
			if(showlineeffect) ResourceHolder.loadLineClearEffectImages();
			if(showbg) ResourceHolder.loadBackgroundImages();
			game.enterState(StateConfigMainMenu.ID);
		}

		// Cancel button
		if(GameKey.gamekey[0].isPushKey(GameKey.BUTTON_B)) {
		    loadConfig(NullpoMinoSlick.propConfig);
			game.enterState(StateConfigMainMenu.ID);
		}

		// Screenshot button
		if(GameKey.gamekey[0].isPushKey(GameKey.BUTTON_SCREENSHOT)) ssflag = true;

		// Exit button
		if(GameKey.gamekey[0].isPushKey(GameKey.BUTTON_QUIT)) container.exit();

		if(NullpoMinoSlick.alternateFPSTiming) NullpoMinoSlick.alternateFPSSleep();
	}
}
