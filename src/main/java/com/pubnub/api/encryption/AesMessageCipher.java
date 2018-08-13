package com.pubnub.api.encryption;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNubException;
import com.pubnub.api.vendor.Crypto;

public class AesMessageCipher implements MessageCipher {

	private PNConfiguration config;

	public AesMessageCipher(PNConfiguration config) {
		this.config = config;
	}

	@Override
	public String encrypt(String channel, String message) throws PubNubException {
		if (config.getCipherKey() != null) {
			Crypto crypto = new Crypto(config.getCipherKey());
			String encryptedMessage = crypto.encrypt(message).replace("\n", "");
			return encryptedMessage;
		}
		return message;
	}

	@Override
	public boolean isActive() {
		return config.getCipherKey() != null;
	}

	@Override
	public String decrypt(String encryptedMessage) throws PubNubException {
		Crypto crypto = new Crypto(config.getCipherKey());
		return crypto.decrypt(encryptedMessage);
	}

}
