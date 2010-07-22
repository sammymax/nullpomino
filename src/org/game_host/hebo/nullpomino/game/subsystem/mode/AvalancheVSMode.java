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
package org.game_host.hebo.nullpomino.game.subsystem.mode;

import java.util.Random;

import org.game_host.hebo.nullpomino.game.component.BGMStatus;
import org.game_host.hebo.nullpomino.game.component.Block;
import org.game_host.hebo.nullpomino.game.component.Controller;
import org.game_host.hebo.nullpomino.game.component.Field;
import org.game_host.hebo.nullpomino.game.component.Piece;
import org.game_host.hebo.nullpomino.game.event.EventReceiver;
import org.game_host.hebo.nullpomino.game.play.GameEngine;
import org.game_host.hebo.nullpomino.game.play.GameManager;
import org.game_host.hebo.nullpomino.util.CustomProperties;
import org.game_host.hebo.nullpomino.util.GeneralUtil;

/**
 * AVALANCHE VS-BATTLE mode (beta)
 */
public class AvalancheVSMode extends DummyMode {
	/** 現在のバージョン */
	private static final int CURRENT_VERSION = 0;
	
	/** Enabled piece types */
	private static final int[] PIECE_ENABLE = {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0};
	
	/** Block colors */
	private static final int[] BLOCK_COLORS =
	{
		Block.BLOCK_COLOR_RED,
		Block.BLOCK_COLOR_GREEN,
		Block.BLOCK_COLOR_BLUE,
		Block.BLOCK_COLOR_YELLOW,
		Block.BLOCK_COLOR_PURPLE
	};
	
	/** Fever map files list */
	private static final String[] FEVER_MAPS = 
	{
		"Fever", "15th", "15thDS", "7", "Compendium"
	};

	/** プレイヤーの数 */
	private static final int MAX_PLAYERS = 2;
	
	/** Ojama counter setting constants */
	private final int OJAMA_COUNTER_OFF = 0, OJAMA_COUNTER_ON = 1, OJAMA_COUNTER_FEVER = 2;

	/** Names of ojama counter settings */
	private final String[] OJAMA_COUNTER_STRING = {"OFF", "ON", "FEVER"};

	/** Zenkeshi  setting constants */
	private final int ZENKESHI_MODE_OFF = 0, ZENKESHI_MODE_ON = 1, ZENKESHI_MODE_FEVER = 2;

	/** Names of zenkeshi settings */
	private final String[] ZENKESHI_TYPE_NAMES = {"OFF", "ON", "FEVER"};
	
	/** 各プレイヤーの枠の色 */
	private final int[] PLAYER_COLOR_FRAME = {GameEngine.FRAME_COLOR_RED, GameEngine.FRAME_COLOR_BLUE};

	/** このモードを所有するGameManager */
	private GameManager owner;

	/** 描画などのイベント処理 */
	private EventReceiver receiver;

	/** Rule settings for countering ojama not yet dropped */
	private int[] ojamaCounterMode;

	/** 溜まっている邪魔ブロックの数 */
	private int[] ojama;

	/** 送った邪魔ブロックの数 */
	private int[] ojamaSent;

	/** 最後にスコア獲得してから経過した時間 */
	private int[] scgettime;

	/** 使用するBGM */
	private int bgmno;

	/** ビッグ */
	private boolean[] big;

	/** 効果音ON/OFF */
	private boolean[] enableSE;

	/** マップ使用フラグ */
	private boolean[] useMap;

	/** 使用するマップセット番号 */
	private int[] mapSet;

	/** マップ番号(-1でランダム) */
	private int[] mapNumber;

	/** 最後に使ったプリセット番号 */
	private int[] presetNumber;

	/** 勝者 */
	private int winnerID;

	/** マップセットのプロパティファイル */
	private CustomProperties[] propMap;

	/** 最大マップ番号 */
	private int[] mapMaxNo;

	/** バックアップ用フィールド（マップをリプレイに保存するときに使用） */
	private Field[] fldBackup;

	/** マップ選択用乱数 */
	private Random randMap;

	/** バージョン */
	private int version;
	
	/** Flag for all clear */
	private boolean[] zenKeshi;

	/** Amount of points earned from most recent clear */
	private int[] lastscore, lastmultiplier;
	
	/** Amount of ojama added in current chain */
	private int[] ojamaAdd;
	
	/** Score */
	private int[] score;
	
	/** Max amount of ojama dropped at once */
	private int[] maxAttack;

	/** Number of colors to use */
	private int[] numColors;

	/** Minimum chain count needed to send ojama */
	private int[] rensaShibari;

	/** Denominator for score-to-ojama conversion */
	private int[] ojamaRate;
	
	/** Settings for hard ojama blocks */
	private int[] ojamaHard;

	/** Hurryup開始までの秒数(0でHurryupなし) */
	private int[] hurryupSeconds;

	/** Fever points needed to enter Fever Mode */
	private int[] feverThreshold;
	
	/** Fever points */
	private int[] feverPoints;

	/** Fever time */
	private int[] feverTime;

	/** Minimum and maximum fever time */
	private int[] feverTimeMin, feverTimeMax;

	/** Flag set to true when player is in Fever Mode */
	private boolean[] inFever;

	/** Backup fields for Fever Mode */
	private Field[] feverBackupField;

	/** Second ojama counter for Fever Mode */
	private int[] ojamaFever;

	/** Set to true when opponent starts chain while in Fever Mode */
	private boolean[] ojamaAddToFever;
	
	/** Set to true when last drop resulted in a clear */
	private boolean[] cleared;
	
	/** Set to true when dropping ojama blocks */
	private boolean[] ojamaDrop;
	
	/** Selected fever map set file */
	private int[] feverMapSet;
	
	/** Selected fever map set file's subset list */
	private String[][] feverMapSubsets;
	
	/** Time to display "ZENKESHI!" */
	private int[] zenKeshiDisplay;
	
	/** Zenkeshi reward type */
	private int[] zenKeshiType;
	
	/** Fever map CustomProperties */
	private CustomProperties[] propFeverMap;
	
	/** Chain levels for Fever Mode */
	private int[] feverChain;
	
	/** Chain level boundaries for Fever Mode */
	private int[] feverChainMin, feverChainMax;

	/*
	 * モード名
	 */
	@Override
	public String getName() {
		return "AVALANCHE VS-BATTLE (BETA)";
	}

	/*
	 * プレイヤー数
	 */
	@Override
	public int getPlayers() {
		return MAX_PLAYERS;
	}

	/*
	 * モードの初期化
	 */
	@Override
	public void modeInit(GameManager manager) {
		owner = manager;
		receiver = owner.receiver;

		ojamaCounterMode = new int[MAX_PLAYERS];
		ojama = new int[MAX_PLAYERS];
		ojamaSent = new int[MAX_PLAYERS];

		scgettime = new int[MAX_PLAYERS];
		bgmno = 0;
		big = new boolean[MAX_PLAYERS];
		enableSE = new boolean[MAX_PLAYERS];
		hurryupSeconds = new int[MAX_PLAYERS];
		useMap = new boolean[MAX_PLAYERS];
		mapSet = new int[MAX_PLAYERS];
		mapNumber = new int[MAX_PLAYERS];
		presetNumber = new int[MAX_PLAYERS];
		propMap = new CustomProperties[MAX_PLAYERS];
		mapMaxNo = new int[MAX_PLAYERS];
		fldBackup = new Field[MAX_PLAYERS];
		randMap = new Random();

		zenKeshi = new boolean[MAX_PLAYERS];
		lastscore = new int[MAX_PLAYERS];
		lastmultiplier = new int[MAX_PLAYERS];
		ojamaAdd = new int[MAX_PLAYERS];
		score = new int[MAX_PLAYERS];
		numColors = new int[MAX_PLAYERS];
		maxAttack = new int[MAX_PLAYERS];
		rensaShibari = new int[MAX_PLAYERS];
		ojamaRate = new int[MAX_PLAYERS];
		ojamaHard = new int[MAX_PLAYERS];

		feverThreshold = new int[MAX_PLAYERS];
		feverPoints = new int[MAX_PLAYERS];
		feverTime = new int[MAX_PLAYERS];
		feverTimeMin = new int[MAX_PLAYERS];
		feverTimeMax = new int[MAX_PLAYERS];
		inFever = new boolean[MAX_PLAYERS];
		feverBackupField = new Field[MAX_PLAYERS];
		ojamaFever = new int[MAX_PLAYERS];
		ojamaAddToFever = new boolean[MAX_PLAYERS];
		cleared = new boolean[MAX_PLAYERS];
		ojamaDrop = new boolean[MAX_PLAYERS];
		feverMapSet = new int[MAX_PLAYERS];
		zenKeshiDisplay = new int[MAX_PLAYERS];
		zenKeshiType = new int[MAX_PLAYERS];
		propFeverMap = new CustomProperties[MAX_PLAYERS];
		feverChain = new int[MAX_PLAYERS];
		feverChainMin = new int[MAX_PLAYERS];
		feverChainMax = new int[MAX_PLAYERS];
		feverMapSubsets = new String[MAX_PLAYERS][];

		winnerID = -1;
	}

	/**
	 * スピードプリセットを読み込み
	 * @param engine GameEngine
	 * @param prop 読み込み元のプロパティファイル
	 * @param preset プリセット番号
	 */
	private void loadPreset(GameEngine engine, CustomProperties prop, int preset) {
		engine.speed.gravity = prop.getProperty("avalanchevs.gravity." + preset, 4);
		engine.speed.denominator = prop.getProperty("avalanchevs.denominator." + preset, 256);
		engine.speed.are = prop.getProperty("avalanchevs.are." + preset, 24);
		engine.speed.areLine = prop.getProperty("avalanchevs.areLine." + preset, 24);
		engine.speed.lineDelay = prop.getProperty("avalanchevs.lineDelay." + preset, 10);
		engine.speed.lockDelay = prop.getProperty("avalanchevs.lockDelay." + preset, 30);
		engine.speed.das = prop.getProperty("avalanchevs.das." + preset, 14);
	}

	/**
	 * スピードプリセットを保存
	 * @param engine GameEngine
	 * @param prop 保存先のプロパティファイル
	 * @param preset プリセット番号
	 */
	private void savePreset(GameEngine engine, CustomProperties prop, int preset) {
		prop.setProperty("avalanchevs.gravity." + preset, engine.speed.gravity);
		prop.setProperty("avalanchevs.denominator." + preset, engine.speed.denominator);
		prop.setProperty("avalanchevs.are." + preset, engine.speed.are);
		prop.setProperty("avalanchevs.areLine." + preset, engine.speed.areLine);
		prop.setProperty("avalanchevs.lineDelay." + preset, engine.speed.lineDelay);
		prop.setProperty("avalanchevs.lockDelay." + preset, engine.speed.lockDelay);
		prop.setProperty("avalanchevs.das." + preset, engine.speed.das);
	}

	/**
	 * スピード以外の設定を読み込み
	 * @param engine GameEngine
	 * @param prop 読み込み元のプロパティファイル
	 */
	private void loadOtherSetting(GameEngine engine, CustomProperties prop) {
		int playerID = engine.playerID;
		bgmno = prop.getProperty("avalanchevs.bgmno", 0);
		ojamaCounterMode[playerID] = prop.getProperty("avalanchevs.ojamaCounterMode", OJAMA_COUNTER_ON);
		big[playerID] = prop.getProperty("avalanchevs.big.p" + playerID, false);
		enableSE[playerID] = prop.getProperty("avalanchevs.enableSE.p" + playerID, true);
		hurryupSeconds[playerID] = prop.getProperty("vsbattle.hurryupSeconds.p" + playerID, 192);
		useMap[playerID] = prop.getProperty("avalanchevs.useMap.p" + playerID, false);
		mapSet[playerID] = prop.getProperty("avalanchevs.mapSet.p" + playerID, 0);
		mapNumber[playerID] = prop.getProperty("avalanchevs.mapNumber.p" + playerID, -1);
		presetNumber[playerID] = prop.getProperty("avalanchevs.presetNumber.p" + playerID, 0);
		maxAttack[playerID] = prop.getProperty("avalanchevs.maxAttack.p" + playerID, 30);
		numColors[playerID] = prop.getProperty("avalanchevs.numColors.p" + playerID, 5);
		rensaShibari[playerID] = prop.getProperty("avalanchevs.rensaShibari.p" + playerID, 1);
		ojamaRate[playerID] = prop.getProperty("avalanchevs.ojamaRate.p" + playerID, 120);
		ojamaHard[playerID] = prop.getProperty("avalanchevs.ojamaHard.p" + playerID, 0);
		feverThreshold[playerID] = prop.getProperty("avalanchevs.feverThreshold.p" + playerID, 0);
		feverTimeMin[playerID] = prop.getProperty("avalanchevs.feverTimeMin.p" + playerID, 15);
		feverTimeMax[playerID] = prop.getProperty("avalanchevs.feverTimeMax.p" + playerID, 30);
		feverMapSet[playerID] = prop.getProperty("avalanchevs.feverMapSet.p" + playerID, 0);
		zenKeshiType[playerID] = prop.getProperty("avalanchevs.zenKeshiType.p" + playerID, 1);
	}

	/**
	 * スピード以外の設定を保存
	 * @param engine GameEngine
	 * @param prop 保存先のプロパティファイル
	 */
	private void saveOtherSetting(GameEngine engine, CustomProperties prop) {
		int playerID = engine.playerID;
		prop.setProperty("avalanchevs.bgmno", bgmno);
		prop.setProperty("avalanchevs.ojamaCounterMode", ojamaCounterMode[playerID]);
		prop.setProperty("avalanchevs.big.p" + playerID, big[playerID]);
		prop.setProperty("avalanchevs.enableSE.p" + playerID, enableSE[playerID]);
		prop.setProperty("vsbattle.hurryupSeconds.p" + playerID, hurryupSeconds[playerID]);
		prop.setProperty("avalanchevs.useMap.p" + playerID, useMap[playerID]);
		prop.setProperty("avalanchevs.mapSet.p" + playerID, mapSet[playerID]);
		prop.setProperty("avalanchevs.mapNumber.p" + playerID, mapNumber[playerID]);
		prop.setProperty("avalanchevs.presetNumber.p" + playerID, presetNumber[playerID]);
		prop.setProperty("avalanchevs.maxAttack.p" + playerID, maxAttack[playerID]);
		prop.setProperty("avalanchevs.numColors.p" + playerID, numColors[playerID]);
		prop.setProperty("avalanchevs.rensaShibari.p" + playerID, rensaShibari[playerID]);
		prop.setProperty("avalanchevs.ojamaRate.p" + playerID, ojamaRate[playerID]);
		prop.setProperty("avalanchevs.ojamaHard.p" + playerID, ojamaHard[playerID]);
		prop.setProperty("avalanchevs.feverThreshold.p" + playerID, feverThreshold[playerID]);
		prop.setProperty("avalanchevs.feverMapSet.p" + playerID, feverMapSet[playerID]);
		prop.setProperty("avalanchevs.zenKeshiType.p" + playerID, zenKeshiType[playerID]);
	}

	/**
	 * マップ読み込み
	 * @param field フィールド
	 * @param prop 読み込み元のプロパティファイル
	 * @param preset 任意のID
	 */
	private void loadMap(Field field, CustomProperties prop, int id) {
		field.reset();
		//field.readProperty(prop, id);
		field.stringToField(prop.getProperty("map." + id, ""));
		field.setAllAttribute(Block.BLOCK_ATTRIBUTE_VISIBLE, true);
		field.setAllAttribute(Block.BLOCK_ATTRIBUTE_OUTLINE, true);
		field.setAllAttribute(Block.BLOCK_ATTRIBUTE_SELFPLACED, false);
	}

	/**
	 * マップ保存
	 * @param field フィールド
	 * @param prop 保存先のプロパティファイル
	 * @param id 任意のID
	 */
	private void saveMap(Field field, CustomProperties prop, int id) {
		//field.writeProperty(prop, id);
		prop.setProperty("map." + id, field.fieldToString());
	}

	/**
	 * プレビュー用にマップを読み込み
	 * @param engine GameEngine
	 * @param playerID プレイヤー番号
	 * @param id マップID
	 * @param forceReload trueにするとマップファイルを強制再読み込み
	 */
	private void loadMapPreview(GameEngine engine, int playerID, int id, boolean forceReload) {
		if((propMap[playerID] == null) || (forceReload)) {
			mapMaxNo[playerID] = 0;
			propMap[playerID] = receiver.loadProperties("config/map/avalanche/" + mapSet[playerID] + ".map");
		}

		if((propMap[playerID] == null) && (engine.field != null)) {
			engine.field.reset();
		} else if(propMap[playerID] != null) {
			mapMaxNo[playerID] = propMap[playerID].getProperty("map.maxMapNumber", 0);
			engine.createFieldIfNeeded();
			loadMap(engine.field, propMap[playerID], id);
			engine.field.setAllSkin(engine.getSkin());
		}
	}

	/*
	 * 各プレイヤーの初期化
	 */
	@Override
	public void playerInit(GameEngine engine, int playerID) {
		if(playerID == 1) {
			engine.randSeed = owner.engine[0].randSeed;
			engine.random = new Random(owner.engine[0].randSeed);
		}

		engine.framecolor = PLAYER_COLOR_FRAME[playerID];
		engine.clearMode = GameEngine.CLEAR_COLOR;
		engine.garbageColorClear = true;
		engine.lineGravityType = GameEngine.LINE_GRAVITY_CASCADE;
		for(int i = 0; i < Piece.PIECE_COUNT; i++)
			engine.nextPieceEnable[i] = (PIECE_ENABLE[i] == 1);
		engine.blockColors = BLOCK_COLORS;
		engine.randomBlockColor = true;
		engine.connectBlocks = false;

		ojama[playerID] = 0;
		ojamaSent[playerID] = 0;
		score[playerID] = 0;
		zenKeshi[playerID] = false;
		scgettime[playerID] = 0;
		feverPoints[playerID] = 0;
		feverTime[playerID] = feverTimeMin[playerID] * 60;
		inFever[playerID] = false;
		feverBackupField[playerID] = null;
		cleared[playerID] = false;
		ojamaDrop[playerID] = false;
		zenKeshiDisplay[playerID] = 0;

		if(engine.owner.replayMode == false) {
			loadOtherSetting(engine, engine.owner.modeConfig);
			loadPreset(engine, engine.owner.modeConfig, -1 - playerID);
			version = CURRENT_VERSION;
		} else {
			loadOtherSetting(engine, engine.owner.replayProp);
			loadPreset(engine, engine.owner.replayProp, -1 - playerID);
			version = owner.replayProp.getProperty("avalanchevs.version", 0);
		}
	}

	/*
	 * 設定画面の処理
	 */
	@Override
	public boolean onSetting(GameEngine engine, int playerID) {
		// メニュー
		if((engine.owner.replayMode == false) && (engine.statc[4] == 0)) {
			// 上
			if(engine.ctrl.isMenuRepeatKey(Controller.BUTTON_UP)) {
				engine.statc[2]--;
				if(engine.statc[2] < 0) engine.statc[2] = 26;
				engine.playSE("cursor");
			}
			// 下
			if(engine.ctrl.isMenuRepeatKey(Controller.BUTTON_DOWN)) {
				engine.statc[2]++;
				if(engine.statc[2] > 24) engine.statc[2] = 0;
				engine.playSE("cursor");
			}

			// 設定変更
			int change = 0;
			if(engine.ctrl.isMenuRepeatKey(Controller.BUTTON_LEFT)) change = -1;
			if(engine.ctrl.isMenuRepeatKey(Controller.BUTTON_RIGHT)) change = 1;

			if(change != 0) {
				engine.playSE("change");

				int m = 1;
				if(engine.ctrl.isPress(Controller.BUTTON_E)) m = 100;
				if(engine.ctrl.isPress(Controller.BUTTON_F)) m = 1000;

				switch(engine.statc[2]) {
				case 0:
					engine.speed.gravity += change * m;
					if(engine.speed.gravity < -1) engine.speed.gravity = 99999;
					if(engine.speed.gravity > 99999) engine.speed.gravity = -1;
					break;
				case 1:
					engine.speed.denominator += change * m;
					if(engine.speed.denominator < -1) engine.speed.denominator = 99999;
					if(engine.speed.denominator > 99999) engine.speed.denominator = -1;
					break;
				case 2:
					engine.speed.are += change;
					if(engine.speed.are < 0) engine.speed.are = 99;
					if(engine.speed.are > 99) engine.speed.are = 0;
					break;
				case 3:
					engine.speed.areLine += change;
					if(engine.speed.areLine < 0) engine.speed.areLine = 99;
					if(engine.speed.areLine > 99) engine.speed.areLine = 0;
					break;
				case 4:
					engine.speed.lineDelay += change;
					if(engine.speed.lineDelay < 0) engine.speed.lineDelay = 99;
					if(engine.speed.lineDelay > 99) engine.speed.lineDelay = 0;
					break;
				case 5:
					engine.speed.lockDelay += change;
					if(engine.speed.lockDelay < 0) engine.speed.lockDelay = 99;
					if(engine.speed.lockDelay > 99) engine.speed.lockDelay = 0;
					break;
				case 6:
					engine.speed.das += change;
					if(engine.speed.das < 0) engine.speed.das = 99;
					if(engine.speed.das > 99) engine.speed.das = 0;
					break;
				case 7:
				case 8:
					presetNumber[playerID] += change;
					if(presetNumber[playerID] < 0) presetNumber[playerID] = 99;
					if(presetNumber[playerID] > 99) presetNumber[playerID] = 0;
					break;
				case 9:
					ojamaCounterMode[playerID] += change;
					if(ojamaCounterMode[playerID] < 0) ojamaCounterMode[playerID] = 2;
					if(ojamaCounterMode[playerID] > 2) ojamaCounterMode[playerID] = 0;
					break;
				case 10:
					maxAttack[playerID] += change;
					if(maxAttack[playerID] < 0) maxAttack[playerID] = 99;
					if(maxAttack[playerID] > 99) maxAttack[playerID] = 0;
					break;
				case 11:
					numColors[playerID] += change;
					if(numColors[playerID] < 3) numColors[playerID] = 5;
					if(numColors[playerID] > 5) numColors[playerID] = 3;
					break;
				case 12:
					rensaShibari[playerID] += change;
					if(rensaShibari[playerID] < 1) rensaShibari[playerID] = 20;
					if(rensaShibari[playerID] > 20) rensaShibari[playerID] = 1;
					break;
				case 13:
					ojamaRate[playerID] += change*10;
					if(ojamaRate[playerID] < 10) ojamaRate[playerID] = 1000;
					if(ojamaRate[playerID] > 1000) ojamaRate[playerID] = 10;
					break;
				case 14:
					big[playerID] = !big[playerID];
					break;
				case 15:
					enableSE[playerID] = !enableSE[playerID];
					break;
				case 16:
					hurryupSeconds[playerID] += change;
					if(hurryupSeconds[playerID] < 0) hurryupSeconds[playerID] = 300;
					if(hurryupSeconds[playerID] > 300) hurryupSeconds[playerID] = 0;
					break;
				case 17:
					ojamaHard[playerID] += change;
					if(ojamaHard[playerID] < 0) ojamaHard[playerID] = 9;
					if(ojamaHard[playerID] > 9) ojamaHard[playerID] = 0;
					break;
				case 18:
					bgmno += change;
					if(bgmno < 0) bgmno = BGMStatus.BGM_COUNT - 1;
					if(bgmno > BGMStatus.BGM_COUNT - 1) bgmno = 0;
					break;
				case 19:
					useMap[playerID] = !useMap[playerID];
					if(!useMap[playerID]) {
						if(engine.field != null) engine.field.reset();
					} else {
						loadMapPreview(engine, playerID, (mapNumber[playerID] < 0) ? 0 : mapNumber[playerID], true);
					}
					break;
				case 20:
					mapSet[playerID] += change;
					if(mapSet[playerID] < 0) mapSet[playerID] = 99;
					if(mapSet[playerID] > 99) mapSet[playerID] = 0;
					if(useMap[playerID]) {
						mapNumber[playerID] = -1;
						loadMapPreview(engine, playerID, (mapNumber[playerID] < 0) ? 0 : mapNumber[playerID], true);
					}
					break;
				case 21:
					if(useMap[playerID]) {
						mapNumber[playerID] += change;
						if(mapNumber[playerID] < -1) mapNumber[playerID] = mapMaxNo[playerID] - 1;
						if(mapNumber[playerID] > mapMaxNo[playerID] - 1) mapNumber[playerID] = -1;
						loadMapPreview(engine, playerID, (mapNumber[playerID] < 0) ? 0 : mapNumber[playerID], true);
					} else {
						mapNumber[playerID] = -1;
					}
					break;
				case 22:
					feverThreshold[playerID] += change;
					if(feverThreshold[playerID] < 0) feverThreshold[playerID] = 9;
					if(feverThreshold[playerID] > 9) feverThreshold[playerID] = 0;
					break;
				case 23:
					feverTimeMin[playerID] += change;
					if(feverTimeMin[playerID] < 1) feverTimeMin[playerID] = feverTimeMax[playerID];
					if(feverTimeMin[playerID] > feverTimeMax[playerID]) feverTimeMin[playerID] = 1;
					break;
				case 24:
					feverTimeMax[playerID] += change;
					if(feverTimeMax[playerID] < feverTimeMin[playerID]) feverTimeMax[playerID] = 99;
					if(feverTimeMax[playerID] > 99) feverTimeMax[playerID] = feverTimeMin[playerID];
					break;
				case 25:
					feverMapSet[playerID] += change;
					if(feverMapSet[playerID] < 0) feverMapSet[playerID] = FEVER_MAPS.length-1;
					if(feverMapSet[playerID] >= FEVER_MAPS.length) feverMapSet[playerID] = 0;
					break;
				case 26:
					zenKeshiType[playerID] += change;
					if(zenKeshiType[playerID] < 0) zenKeshiType[playerID] = 2;
					if(zenKeshiType[playerID] > 2) zenKeshiType[playerID] = 0;
					break;
				}
			}

			// 決定
			if(engine.ctrl.isPush(Controller.BUTTON_A) && (engine.statc[3] >= 5)) {
				engine.playSE("decide");

				if(engine.statc[2] == 7) {
					loadPreset(engine, owner.modeConfig, presetNumber[playerID]);
				} else if(engine.statc[2] == 8) {
					savePreset(engine, owner.modeConfig, presetNumber[playerID]);
					receiver.saveModeConfig(owner.modeConfig);
				} else {
					saveOtherSetting(engine, owner.modeConfig);
					savePreset(engine, owner.modeConfig, -1 - playerID);
					receiver.saveModeConfig(owner.modeConfig);
					engine.statc[4] = 1;
				}
			}

			// キャンセル
			if(engine.ctrl.isPush(Controller.BUTTON_B)) {
				engine.quitflag = true;
			}

			// プレビュー用マップ読み込み
			if(useMap[playerID] && (engine.statc[3] == 0)) {
				loadMapPreview(engine, playerID, (mapNumber[playerID] < 0) ? 0 : mapNumber[playerID], true);
			}

			// ランダムマッププレビュー
			if(useMap[playerID] && (propMap[playerID] != null) && (mapNumber[playerID] < 0)) {
				if(engine.statc[3] % 30 == 0) {
					engine.statc[5]++;
					if(engine.statc[5] >= mapMaxNo[playerID]) engine.statc[5] = 0;
					loadMapPreview(engine, playerID, engine.statc[5], false);
				}
			}

			engine.statc[3]++;
		} else if(engine.statc[4] == 0) {
			engine.statc[3]++;
			engine.statc[2] = 0;

			if(engine.statc[3] >= 60) {
				engine.statc[2] = 9;
			}
			if(engine.statc[3] >= 120) {
				engine.statc[4] = 1;
			}
		} else {
			// 開始
			if((owner.engine[0].statc[4] == 1) && (owner.engine[1].statc[4] == 1) && (playerID == 1)) {
				owner.engine[0].stat = GameEngine.STAT_READY;
				owner.engine[1].stat = GameEngine.STAT_READY;
				owner.engine[0].resetStatc();
				owner.engine[1].resetStatc();
			}
			// キャンセル
			else if(engine.ctrl.isPush(Controller.BUTTON_B)) {
				engine.statc[4] = 0;
			}
		}

		return true;
	}

	private void loadMapSetFever(GameEngine engine, int playerID, int id, boolean forceReload) {
		if((propFeverMap[playerID] == null) || (forceReload)) {
			propFeverMap[playerID] = receiver.loadProperties("config/map/avalanche/" +
					FEVER_MAPS[id] + ".map");
			feverChainMin[playerID] = propFeverMap[playerID].getProperty("minChain", 3);
			feverChainMax[playerID] = propFeverMap[playerID].getProperty("maxChain", 15);
			String subsets = propFeverMap[playerID].getProperty("sets");
			feverMapSubsets[playerID] = subsets.split(",");
		}
	}

	/*
	 * 設定画面の描画
	 */
	@Override
	public void renderSetting(GameEngine engine, int playerID) {
		if(engine.statc[4] == 0) {
			if(engine.statc[2] < 9) {
				if(owner.replayMode == false) {
					receiver.drawMenuFont(engine, playerID, 0, (engine.statc[2] * 2) + 1, "b",
										  (playerID == 0) ? EventReceiver.COLOR_RED : EventReceiver.COLOR_BLUE);
				}

				receiver.drawMenuFont(engine, playerID, 0,  0, "GRAVITY", EventReceiver.COLOR_ORANGE);
				receiver.drawMenuFont(engine, playerID, 1,  1, String.valueOf(engine.speed.gravity), (engine.statc[2] == 0));
				receiver.drawMenuFont(engine, playerID, 0,  2, "G-MAX", EventReceiver.COLOR_ORANGE);
				receiver.drawMenuFont(engine, playerID, 1,  3, String.valueOf(engine.speed.denominator), (engine.statc[2] == 1));
				receiver.drawMenuFont(engine, playerID, 0,  4, "ARE", EventReceiver.COLOR_ORANGE);
				receiver.drawMenuFont(engine, playerID, 1,  5, String.valueOf(engine.speed.are), (engine.statc[2] == 2));
				receiver.drawMenuFont(engine, playerID, 0,  6, "ARE LINE", EventReceiver.COLOR_ORANGE);
				receiver.drawMenuFont(engine, playerID, 1,  7, String.valueOf(engine.speed.areLine), (engine.statc[2] == 3));
				receiver.drawMenuFont(engine, playerID, 0,  8, "LINE DELAY", EventReceiver.COLOR_ORANGE);
				receiver.drawMenuFont(engine, playerID, 1,  9, String.valueOf(engine.speed.lineDelay), (engine.statc[2] == 4));
				receiver.drawMenuFont(engine, playerID, 0, 10, "LOCK DELAY", EventReceiver.COLOR_ORANGE);
				receiver.drawMenuFont(engine, playerID, 1, 11, String.valueOf(engine.speed.lockDelay), (engine.statc[2] == 5));
				receiver.drawMenuFont(engine, playerID, 0, 12, "DAS", EventReceiver.COLOR_ORANGE);
				receiver.drawMenuFont(engine, playerID, 1, 13, String.valueOf(engine.speed.das), (engine.statc[2] == 6));
				receiver.drawMenuFont(engine, playerID, 0, 14, "LOAD", EventReceiver.COLOR_GREEN);
				receiver.drawMenuFont(engine, playerID, 1, 15, String.valueOf(presetNumber[playerID]), (engine.statc[2] == 7));
				receiver.drawMenuFont(engine, playerID, 0, 16, "SAVE", EventReceiver.COLOR_GREEN);
				receiver.drawMenuFont(engine, playerID, 1, 17, String.valueOf(presetNumber[playerID]), (engine.statc[2] == 8));
			} else if(engine.statc[2] < 19) {
				if(owner.replayMode == false) {
					receiver.drawMenuFont(engine, playerID, 0, ((engine.statc[2] - 9) * 2) + 1, "b",
										  (playerID == 0) ? EventReceiver.COLOR_RED : EventReceiver.COLOR_BLUE);
				}

				receiver.drawMenuFont(engine, playerID, 0,  0, "COUNTER", EventReceiver.COLOR_CYAN);
				receiver.drawMenuFont(engine, playerID, 1,  1, OJAMA_COUNTER_STRING[ojamaCounterMode[playerID]], (engine.statc[2] == 9));
				receiver.drawMenuFont(engine, playerID, 0,  2, "MAX ATTACK", EventReceiver.COLOR_CYAN);
				receiver.drawMenuFont(engine, playerID, 1,  3, String.valueOf(maxAttack[playerID]), (engine.statc[2] == 10));
				receiver.drawMenuFont(engine, playerID, 0,  4, "COLORS", EventReceiver.COLOR_CYAN);
				receiver.drawMenuFont(engine, playerID, 1,  5, String.valueOf(numColors[playerID]), (engine.statc[2] == 11));
				receiver.drawMenuFont(engine, playerID, 0,  6, "MIN CHAIN", EventReceiver.COLOR_CYAN);
				receiver.drawMenuFont(engine, playerID, 1,  7, String.valueOf(rensaShibari[playerID]), (engine.statc[2] == 12));
				receiver.drawMenuFont(engine, playerID, 0,  8, "OJAMA RATE", EventReceiver.COLOR_CYAN);
				receiver.drawMenuFont(engine, playerID, 1,  9, String.valueOf(ojamaRate[playerID]), (engine.statc[2] == 13));
				receiver.drawMenuFont(engine, playerID, 0, 10, "BIG", EventReceiver.COLOR_CYAN);
				receiver.drawMenuFont(engine, playerID, 1, 11, GeneralUtil.getONorOFF(big[playerID]), (engine.statc[2] == 14));
				receiver.drawMenuFont(engine, playerID, 0, 12, "SE", EventReceiver.COLOR_CYAN);
				receiver.drawMenuFont(engine, playerID, 1, 13, GeneralUtil.getONorOFF(enableSE[playerID]), (engine.statc[2] == 15));
				receiver.drawMenuFont(engine, playerID, 0, 14, "HURRYUP", EventReceiver.COLOR_CYAN);
				receiver.drawMenuFont(engine, playerID, 1, 15, (hurryupSeconds[playerID] == 0) ? "NONE" : hurryupSeconds[playerID]+"SEC",
				                      (engine.statc[2] == 16));
				receiver.drawMenuFont(engine, playerID, 0, 16, "HARD OJAMA", EventReceiver.COLOR_CYAN);
				receiver.drawMenuFont(engine, playerID, 1, 17, String.valueOf(ojamaHard[playerID]), (engine.statc[2] == 17));
				receiver.drawMenuFont(engine, playerID, 0, 18, "BGM", EventReceiver.COLOR_PINK);
				receiver.drawMenuFont(engine, playerID, 1, 19, String.valueOf(bgmno), (engine.statc[2] == 18));
			} else {
				if(owner.replayMode == false) {
					receiver.drawMenuFont(engine, playerID, 0, ((engine.statc[2] - 19) * 2) + 1, "b",
										  (playerID == 0) ? EventReceiver.COLOR_RED : EventReceiver.COLOR_BLUE);
				}

				receiver.drawMenuFont(engine, playerID, 0,  0, "USE MAP", EventReceiver.COLOR_CYAN);
				receiver.drawMenuFont(engine, playerID, 1,  1, GeneralUtil.getONorOFF(useMap[playerID]), (engine.statc[2] == 19));
				receiver.drawMenuFont(engine, playerID, 0,  2, "MAP SET", EventReceiver.COLOR_CYAN);
				receiver.drawMenuFont(engine, playerID, 1,  3, String.valueOf(mapSet[playerID]), (engine.statc[2] == 20));
				receiver.drawMenuFont(engine, playerID, 0,  4, "MAP NO.", EventReceiver.COLOR_CYAN);
				receiver.drawMenuFont(engine, playerID, 1,  5, (mapNumber[playerID] < 0) ? "RANDOM" : mapNumber[playerID]+"/"+(mapMaxNo[playerID]-1),
									  (engine.statc[2] == 21));
				receiver.drawMenuFont(engine, playerID, 0,  6, "FEVER", EventReceiver.COLOR_CYAN);
				receiver.drawMenuFont(engine, playerID, 1,  7, (feverThreshold[playerID] == 0) ? "NONE" : feverThreshold[playerID]+" PTS",
				                      (engine.statc[2] == 22));
				receiver.drawMenuFont(engine, playerID, 0,  8, "F-MIN TIME", EventReceiver.COLOR_CYAN);
				receiver.drawMenuFont(engine, playerID, 1,  9, feverTimeMin[playerID] + "SEC", (engine.statc[2] == 23));
				receiver.drawMenuFont(engine, playerID, 0, 10, "F-MAX TIME", EventReceiver.COLOR_CYAN);
				receiver.drawMenuFont(engine, playerID, 1, 11, feverTimeMax[playerID] + "SEC", (engine.statc[2] == 24));
				receiver.drawMenuFont(engine, playerID, 0, 12, "F-MAP SET", EventReceiver.COLOR_CYAN);
				receiver.drawMenuFont(engine, playerID, 1, 13, FEVER_MAPS[feverMapSet[playerID]].toUpperCase(), (engine.statc[2] == 25));
				receiver.drawMenuFont(engine, playerID, 0, 14, "ZENKESHI", EventReceiver.COLOR_CYAN);
				receiver.drawMenuFont(engine, playerID, 1, 15, ZENKESHI_TYPE_NAMES[zenKeshiType[playerID]], (engine.statc[2] == 26));
			}
		} else {
			receiver.drawMenuFont(engine, playerID, 3, 10, "WAIT", EventReceiver.COLOR_YELLOW);
		}
	}

	/*
	 * Readyの時の初期化処理（初期化前）
	 */
	@Override
	public boolean onReady(GameEngine engine, int playerID) {
		if(engine.statc[0] == 0) {
			engine.numColors = numColors[playerID];
			feverTime[playerID] = feverTimeMin[playerID] * 60;
			feverChain[playerID] = 5;
			// マップ読み込み・リプレイ保存用にバックアップ
			if(useMap[playerID]) {
				if(owner.replayMode) {
					engine.createFieldIfNeeded();
					loadMap(engine.field, owner.replayProp, playerID);
					engine.field.setAllSkin(engine.getSkin());
				} else {
					if(propMap[playerID] == null) {
						propMap[playerID] = receiver.loadProperties("config/map/vsbattle/" + mapSet[playerID] + ".map");
					}

					if(propMap[playerID] != null) {
						engine.createFieldIfNeeded();

						if(mapNumber[playerID] < 0) {
							if((playerID == 1) && (useMap[0]) && (mapNumber[0] < 0)) {
								engine.field.copy(owner.engine[0].field);
							} else {
								int no = (mapMaxNo[playerID] < 1) ? 0 : randMap.nextInt(mapMaxNo[playerID]);
								loadMap(engine.field, propMap[playerID], no);
							}
						} else {
							loadMap(engine.field, propMap[playerID], mapNumber[playerID]);
						}

						engine.field.setAllSkin(engine.getSkin());
						fldBackup[playerID] = new Field(engine.field);
					}
				}
			} else if(engine.field != null) {
				engine.field.reset();
			}
			loadMapSetFever(engine, playerID, feverMapSet[playerID], true);
		}

		return false;
	}

	/*
	 * ゲーム開始時の処理
	 */
	@Override
	public void startGame(GameEngine engine, int playerID) {
		engine.b2bEnable = false;
		engine.comboType = GameEngine.COMBO_TYPE_DISABLE;
		engine.big = big[playerID];
		engine.enableSE = enableSE[playerID];
		if(playerID == 1) owner.bgmStatus.bgm = bgmno;
		engine.colorClearSize = big[playerID] ? 12 : 4;

		engine.tspinAllowKick = false;
		engine.tspinEnable = false;
		engine.useAllSpinBonus = false;
	}

	/*
	 * スコア表示
	 */
	@Override
	public void renderLast(GameEngine engine, int playerID) {
		// ステータス表示
		if(playerID == 0) {
			receiver.drawScoreFont(engine, playerID, -1, 0, "AVALANCHE VS", EventReceiver.COLOR_GREEN);

			receiver.drawScoreFont(engine, playerID, -1, 2, "OJAMA", EventReceiver.COLOR_PURPLE);
			String ojamaStr1P = String.valueOf(ojama[0]);
			if (ojamaAdd[0] > 0 && !(inFever[0] && ojamaAddToFever[0]))
				ojamaStr1P = ojamaStr1P + "(+" + String.valueOf(ojamaAdd[0]) + ")";
			String ojamaStr2P = String.valueOf(ojama[1]);
			if (ojamaAdd[1] > 0 && !(inFever[1] && ojamaAddToFever[1]))
				ojamaStr2P = ojamaStr2P + "(+" + String.valueOf(ojamaAdd[1]) + ")";
			receiver.drawScoreFont(engine, playerID, -1, 3, "1P:", EventReceiver.COLOR_RED);
			receiver.drawScoreFont(engine, playerID, 3, 3, ojamaStr1P, (ojama[0] > 0));
			receiver.drawScoreFont(engine, playerID, -1, 4, "2P:", EventReceiver.COLOR_BLUE);
			receiver.drawScoreFont(engine, playerID, 3, 4, ojamaStr2P, (ojama[1] > 0));

			receiver.drawScoreFont(engine, playerID, -1, 6, "ATTACK", EventReceiver.COLOR_GREEN);
			receiver.drawScoreFont(engine, playerID, -1, 7, "1P: " + String.valueOf(ojamaSent[0]), EventReceiver.COLOR_RED);
			receiver.drawScoreFont(engine, playerID, -1, 8, "2P: " + String.valueOf(ojamaSent[1]), EventReceiver.COLOR_BLUE);

			receiver.drawScoreFont(engine, playerID, -1, 10, "SCORE", EventReceiver.COLOR_PURPLE);
			receiver.drawScoreFont(engine, playerID, -1, 11, "1P: " + String.valueOf(score[0]), EventReceiver.COLOR_RED);
			receiver.drawScoreFont(engine, playerID, -1, 12, "2P: " + String.valueOf(score[1]), EventReceiver.COLOR_BLUE);

			receiver.drawScoreFont(engine, playerID, -1, 14, "TIME", EventReceiver.COLOR_GREEN);
			receiver.drawScoreFont(engine, playerID, -1, 15, GeneralUtil.getTime(engine.statistics.time));
			
			if (inFever[0] || inFever[1])
			{
				receiver.drawScoreFont(engine, playerID, -1, 17, "FEVER OJAMA", EventReceiver.COLOR_PURPLE);
				String ojamaFeverStr1P = String.valueOf(ojamaFever[0]);
				if (ojamaAdd[0] > 0 && inFever[0] && ojamaAddToFever[0])
					ojamaFeverStr1P = ojamaFeverStr1P + "(+" + String.valueOf(ojamaAdd[0]) + ")";
				String ojamaFeverStr2P = String.valueOf(ojamaFever[1]);
				if (ojamaAdd[1] > 0 && inFever[1] && ojamaAddToFever[1])
					ojamaFeverStr2P = ojamaFeverStr2P + "(+" + String.valueOf(ojamaAdd[1]) + ")";
				receiver.drawScoreFont(engine, playerID, -1, 18, "1P:", EventReceiver.COLOR_RED);
				receiver.drawScoreFont(engine, playerID, 3, 18, ojamaFeverStr1P, (ojamaFever[0] > 0));
				receiver.drawScoreFont(engine, playerID, -1, 19, "2P:", EventReceiver.COLOR_BLUE);
				receiver.drawScoreFont(engine, playerID, 3, 19, ojamaFeverStr2P, (ojamaFever[1] > 0));
			}
		}
		
		if (!owner.engine[playerID].gameActive)
			return;
		int playerColor = (playerID == 0) ? EventReceiver.COLOR_RED : EventReceiver.COLOR_BLUE;
		if (feverThreshold[playerID] > 0)
		{
			receiver.drawMenuFont(engine, playerID, 0, 17, "FEVER POINT", playerColor);
			receiver.drawMenuFont(engine, playerID, 0, 18, feverPoints[playerID] + " / " + feverThreshold[playerID], inFever[playerID]);
			receiver.drawMenuFont(engine, playerID, 0, 19, "FEVER TIME", playerColor);
			receiver.drawMenuFont(engine, playerID, 0, 20, GeneralUtil.getTime(feverTime[playerID]), inFever[playerID]);
		}
			
		if(zenKeshi[playerID] || zenKeshiDisplay[playerID] > 0)
			receiver.drawMenuFont(engine, playerID, 1, 21, "ZENKESHI!", EventReceiver.COLOR_YELLOW);
		if (ojamaHard[playerID] > 0 && engine.field != null)
			for (int x = 0; x < engine.field.getWidth(); x++)
				for (int y = 0; y < engine.field.getHeight(); y++)
				{
					int hard = engine.field.getBlock(x, y).hard;
					if (hard > 0)
						receiver.drawMenuFont(engine, playerID, x, y, String.valueOf(hard), EventReceiver.COLOR_YELLOW);
				}
	}

	/*
	 * スコア計算
	 */
	@Override
	public void calcScore(GameEngine engine, int playerID, int avalanche) {
		int enemyID = 0;
		if(playerID == 0) enemyID = 1;
		
		if (big[playerID])
			avalanche >>= 2;
		// ラインクリアボーナス
		int pts = avalanche*10;
		int ojamaNew = 0;
		if (avalanche > 0) {
			cleared[playerID] = true;
			if (zenKeshi[playerID] && zenKeshiType[playerID] == ZENKESHI_MODE_ON)
				ojamaNew += 30;
			if (engine.field.isEmpty()) {
				engine.playSE("bravo");
				zenKeshi[playerID] = true;
				engine.statistics.score += 2100;
				score[playerID] += 2100;
			}
			else
				zenKeshi[playerID] = false;

			int chain = engine.chain;
			engine.playSE("combo" + Math.min(chain, 20));
			if (chain == 1)
				ojamaAddToFever[enemyID] = inFever[enemyID];
			int multiplier = engine.field.colorClearExtraCount;
			if (big[playerID])
				multiplier >>= 2;
			if (engine.field.colorsCleared > 1)
				multiplier += (engine.field.colorsCleared-1)*2;
			/*
			if (multiplier < 0)
				multiplier = 0;
			if (chain == 0)
				firstExtra = avalanche > engine.colorClearSize;
			*/
			if (chain == 2)
				multiplier += 8;
			else if (chain == 3)
				multiplier += 16;
			else if (chain >= 4)
				multiplier += 32*(chain-3);
			/*
			if (firstExtra)
				multiplier++;
			*/
			
			if (multiplier > 999)
				multiplier = 999;
			if (multiplier < 1)
				multiplier = 1;
			
			lastscore[playerID] = pts;
			lastmultiplier[playerID] = multiplier;
			scgettime[playerID] = 120;
			int ptsTotal = pts*multiplier;
			score[playerID] += ptsTotal;
			
			if (hurryupSeconds[playerID] > 0 && engine.statistics.time > hurryupSeconds[playerID])
				ptsTotal <<= engine.statistics.time / (hurryupSeconds[playerID] * 60);

			ojamaNew += (ptsTotal+ojamaRate[playerID]-1)/ojamaRate[playerID];
			if (chain >= rensaShibari[playerID])
			{
				ojamaSent[playerID] += ojamaNew;
				if (ojamaCounterMode[playerID] != OJAMA_COUNTER_OFF)
				{
					boolean countered = false;
					if (inFever[playerID])
					{
						if (ojamaFever[playerID] > 0 && ojamaNew > 0)
						{
							int delta = Math.min(ojamaFever[playerID], ojamaNew);
							ojamaFever[playerID] -= delta;
							ojamaNew -= delta;
							countered = true;
						}
						if (ojamaAdd[playerID] > 0 && ojamaNew > 0)
						{
							int delta = Math.min(ojamaAdd[playerID], ojamaNew);
							ojamaAdd[playerID] -= delta;
							ojamaNew -= delta;
							countered = true;
						}
					}
					if (ojama[playerID] > 0 && ojamaNew > 0)
					{
						int delta = Math.min(ojama[playerID], ojamaNew);
						ojama[playerID] -= delta;
						ojamaNew -= delta;
						countered = true;
					}
					if (ojamaAdd[playerID] > 0 && ojamaNew > 0)
					{
						int delta = Math.min(ojamaAdd[playerID], ojamaNew);
						ojamaAdd[playerID] -= delta;
						ojamaNew -= delta;
						countered = true;
					}
					if (countered)
					{
						if (feverThreshold[playerID] > 0 && feverThreshold[playerID] > feverPoints[playerID])
							feverPoints[playerID]++;
						if (feverThreshold[enemyID] > 0 && !inFever[enemyID])
							feverTime[enemyID] = Math.min(feverTime[enemyID]+60,feverTimeMax[enemyID]*60);
					}
				}
				if (ojamaNew > 0)
					ojamaAdd[enemyID] += ojamaNew;
			}
		}
		else if (!engine.field.canCascade())
			cleared[playerID] = false;
	}

	public boolean lineClearEnd(GameEngine engine, int playerID) {
		int enemyID = 0;
		if(playerID == 0) enemyID = 1;
		if (ojamaAdd[enemyID] > 0)
		{
			if (ojamaAddToFever[enemyID] && inFever[enemyID])
				ojamaFever[enemyID] += ojamaAdd[enemyID];
			else
				ojama[enemyID] += ojamaAdd[enemyID];
			ojamaAdd[enemyID] = 0;
		}
		if (zenKeshi[playerID] && zenKeshiType[playerID] == ZENKESHI_MODE_FEVER)
		{
			if (feverTime[playerID] > 0)
				feverTime[playerID] += 300;
			if (inFever[playerID] || feverPoints[playerID] >= feverThreshold[playerID])
			{
				feverChain[playerID] += 2;
				if (feverChain[playerID] > feverChainMax[playerID])
					feverChain[playerID] = feverChainMax[playerID];
			}
			else
				loadFeverMap(engine, playerID, 4);
			zenKeshi[playerID] = false;
			zenKeshiDisplay[playerID] = 120;
		}
		if (inFever[playerID] && cleared[playerID])
		{
			if (feverTime[playerID] > 0)
				feverTime[playerID] += (engine.chain-2)*30;
			int chainShort = feverChain[playerID] - engine.chain;
			if (chainShort <= 0 && feverChain[playerID] < feverChainMax[playerID])
				feverChain[playerID]++;
			else if(chainShort == 2)
				feverChain[playerID]--;
			else if (chainShort > 2)
				feverChain[playerID]-=2;
			if (feverChain[playerID] < feverChainMin[playerID])
				feverChain[playerID] = feverChainMin[playerID];
			loadFeverMap(engine, playerID, feverChain[playerID]);
		}
		//Check to end Fever Mode
		if (inFever[playerID] && feverTime[playerID] == 0)
		{
			inFever[playerID] = false;
			feverTime[playerID] = feverTimeMin[playerID] * 60;
			feverPoints[playerID] = 0;
			engine.field = feverBackupField[playerID];
			ojama[playerID] += ojamaFever[playerID];
			ojamaFever[playerID] = 0;
			ojamaAddToFever[playerID] = false;
		}
		//Drop garbage if needed.
		int ojamaNow = inFever[playerID] ? ojamaFever[playerID] : ojama[playerID];
		if (ojamaNow > 0 && !ojamaDrop[playerID] &&
				(!cleared[playerID] || ojamaCounterMode[playerID] != OJAMA_COUNTER_FEVER))
		{
			ojamaDrop[playerID] = true;
			int drop = Math.min(ojamaNow, maxAttack[playerID]);
			if (inFever[playerID])
				ojamaFever[playerID] -= drop;
			else
				ojama[playerID] -= drop;
			engine.field.garbageDrop(engine, drop, big[playerID], ojamaHard[playerID]);
			return true;
		}
		//Check to start Fever Mode
		if (!inFever[playerID] && feverPoints[playerID] >= feverThreshold[playerID] && feverThreshold[playerID] > 0)
		{
			inFever[playerID] = true;
			feverBackupField[playerID] = engine.field;
			engine.field = null;
			loadFeverMap(engine, playerID, feverChain[playerID]);
		}
		return false;
	}

	private void loadFeverMap(GameEngine engine, int playerID, int chain) {
		engine.createFieldIfNeeded();
		engine.field.reset();
		engine.field.stringToField(propFeverMap[playerID].getProperty(
				feverMapSubsets[playerID][engine.random.nextInt(feverMapSubsets[playerID].length)] +
				"." + numColors[playerID] + "colors." + chain + "chain"));
		engine.field.setAllAttribute(Block.BLOCK_ATTRIBUTE_CONNECT_LEFT, false);
		engine.field.setAllAttribute(Block.BLOCK_ATTRIBUTE_CONNECT_DOWN, false);
		engine.field.setAllAttribute(Block.BLOCK_ATTRIBUTE_CONNECT_UP, false);
		engine.field.setAllAttribute(Block.BLOCK_ATTRIBUTE_CONNECT_RIGHT, false);
		engine.field.setAllAttribute(Block.BLOCK_ATTRIBUTE_GARBAGE, false);
		engine.field.setAllAttribute(Block.BLOCK_ATTRIBUTE_ANTIGRAVITY, false);
		engine.field.setAllSkin(engine.getSkin());
		engine.field.shuffleColors(BLOCK_COLORS, numColors[playerID], engine.random);
	}

	/*
	 * 各フレームの最後の処理
	 */
	@Override
	public void onLast(GameEngine engine, int playerID) {
		scgettime[playerID]++;
		if (zenKeshiDisplay[playerID] > 0)
			zenKeshiDisplay[playerID]--;
		if (inFever[playerID] && feverTime[playerID] > 0)
		{
			if (feverTime[playerID] == 1)
				engine.playSE("levelstop");
			feverTime[playerID]--;
		}
		if (engine.stat == GameEngine.STAT_MOVE)
		{
			cleared[playerID] = false;
			ojamaDrop[playerID] = false;
		}
		int width = 1;
		if (engine.field != null)
			width = engine.field.getWidth();
		int blockHeight = receiver.getBlockGraphicsHeight(engine, playerID);
		// せり上がりメーター
		if(ojama[playerID] * blockHeight / width > engine.meterValue) {
			engine.meterValue++;
		} else if(ojama[playerID] * blockHeight / width < engine.meterValue) {
			engine.meterValue--;
		}
		if(ojama[playerID] >= 5*width) engine.meterColor = GameEngine.METER_COLOR_RED;
		else if(ojama[playerID] >= width) engine.meterColor = GameEngine.METER_COLOR_ORANGE;
		else if(ojama[playerID] >= 1) engine.meterColor = GameEngine.METER_COLOR_YELLOW;
		else engine.meterColor = GameEngine.METER_COLOR_GREEN;

		// 決着
		if((playerID == 1) && (owner.engine[0].gameActive)) {
			if((owner.engine[0].stat == GameEngine.STAT_GAMEOVER) && (owner.engine[1].stat == GameEngine.STAT_GAMEOVER)) {
				// 引き分け
				winnerID = -1;
				owner.engine[0].gameActive = false;
				owner.engine[1].gameActive = false;
				owner.bgmStatus.bgm = BGMStatus.BGM_NOTHING;
			} else if((owner.engine[0].stat != GameEngine.STAT_GAMEOVER) && (owner.engine[1].stat == GameEngine.STAT_GAMEOVER)) {
				// 1P勝利
				winnerID = 0;
				owner.engine[0].gameActive = false;
				owner.engine[1].gameActive = false;
				owner.engine[0].stat = GameEngine.STAT_EXCELLENT;
				owner.engine[0].resetStatc();
				owner.engine[0].statc[1] = 1;
				owner.bgmStatus.bgm = BGMStatus.BGM_NOTHING;
			} else if((owner.engine[0].stat == GameEngine.STAT_GAMEOVER) && (owner.engine[1].stat != GameEngine.STAT_GAMEOVER)) {
				// 2P勝利
				winnerID = 1;
				owner.engine[0].gameActive = false;
				owner.engine[1].gameActive = false;
				owner.engine[1].stat = GameEngine.STAT_EXCELLENT;
				owner.engine[1].resetStatc();
				owner.engine[1].statc[1] = 1;
				owner.bgmStatus.bgm = BGMStatus.BGM_NOTHING;
			}
		}
	}

	/*
	 * 結果画面の描画
	 */
	@Override
	public void renderResult(GameEngine engine, int playerID) {
		receiver.drawMenuFont(engine, playerID, 0, 1, "RESULT", EventReceiver.COLOR_ORANGE);
		if(winnerID == -1) {
			receiver.drawMenuFont(engine, playerID, 6, 2, "DRAW", EventReceiver.COLOR_GREEN);
		} else if(winnerID == playerID) {
			receiver.drawMenuFont(engine, playerID, 6, 2, "WIN!", EventReceiver.COLOR_YELLOW);
		} else {
			receiver.drawMenuFont(engine, playerID, 6, 2, "LOSE", EventReceiver.COLOR_WHITE);
		}

		receiver.drawMenuFont(engine, playerID, 0, 3, "ATTACK", EventReceiver.COLOR_ORANGE);
		String strScore = String.format("%10d", ojamaSent[playerID]);
		receiver.drawMenuFont(engine, playerID, 0, 4, strScore);

		receiver.drawMenuFont(engine, playerID, 0, 5, "LINE", EventReceiver.COLOR_ORANGE);
		String strLines = String.format("%10d", engine.statistics.lines);
		receiver.drawMenuFont(engine, playerID, 0, 6, strLines);

		receiver.drawMenuFont(engine, playerID, 0, 7, "PIECE", EventReceiver.COLOR_ORANGE);
		String strPiece = String.format("%10d", engine.statistics.totalPieceLocked);
		receiver.drawMenuFont(engine, playerID, 0, 8, strPiece);

		receiver.drawMenuFont(engine, playerID, 0, 9, "ATTACK/MIN", EventReceiver.COLOR_ORANGE);
		float apm = (float)(ojamaSent[playerID] * 3600) / (float)(engine.statistics.time);
		String strAPM = String.format("%10g", apm);
		receiver.drawMenuFont(engine, playerID, 0, 10, strAPM);

		receiver.drawMenuFont(engine, playerID, 0, 11, "LINE/MIN", EventReceiver.COLOR_ORANGE);
		String strLPM = String.format("%10g", engine.statistics.lpm);
		receiver.drawMenuFont(engine, playerID, 0, 12, strLPM);

		receiver.drawMenuFont(engine, playerID, 0, 13, "PIECE/SEC", EventReceiver.COLOR_ORANGE);
		String strPPS = String.format("%10g", engine.statistics.pps);
		receiver.drawMenuFont(engine, playerID, 0, 14, strPPS);

		receiver.drawMenuFont(engine, playerID, 0, 15, "TIME", EventReceiver.COLOR_ORANGE);
		String strTime = String.format("%10s", GeneralUtil.getTime(owner.engine[0].statistics.time));
		receiver.drawMenuFont(engine, playerID, 0, 16, strTime);
	}

	/*
	 * リプレイ保存時の処理
	 */
	@Override
	public void saveReplay(GameEngine engine, int playerID, CustomProperties prop) {
		saveOtherSetting(engine, owner.replayProp);
		savePreset(engine, owner.replayProp, -1 - playerID);

		if(useMap[playerID] && (fldBackup[playerID] != null)) {
			saveMap(fldBackup[playerID], owner.replayProp, playerID);
		}

		owner.replayProp.setProperty("avalanchevs.version", version);
	}
}
