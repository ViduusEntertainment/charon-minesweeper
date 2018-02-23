/**
 * Copyright 2018, Viduus Entertainment LLC, All rights reserved.
 * 
 * Created on Feb 16, 2018 by Ethan Toney
 */
package org.viduus.charon.minesweeper;

import org.viduus.charon.global.AbstractGameSystems;
import org.viduus.charon.global.GameEngine;
import org.viduus.charon.global.player.Account;

/**
 * @author ethan
 *
 */
public class GameSystems extends AbstractGameSystems {

	public GameSystems(GameEngine ... engines){
		super(engines);
	}
	
	/* (non-Javadoc)
	 * @see org.viduus.charon.global.AbstractGameSystems#loadAccount()
	 */
	@Override
	protected Account loadAccount() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.viduus.charon.global.AbstractGameSystems#saveAccount(org.viduus.charon.global.player.Account)
	 */
	@Override
	protected void saveAccount(Account account) {
		// TODO Auto-generated method stub
		
	}

}
