package com.pubnub.api.encryption;

import com.pubnub.api.PubNubException;

public interface MessageCipher {
	
	boolean isActive();
	
	String encrypt(String channel, String message) throws PubNubException;
	
	String decrypt(String encryptedMessage) throws PubNubException;

}
