package com.pubnub.api.virgil;

import com.virgilsecurity.sdk.crypto.PrivateKey;

public class VirgilConfig {

	private Crypto crypto;
	private VirgilCardsProvider cardsProvider;
	private ChannelInfoProvider channelInfoProvider;
	private PrivateKey privateKey;

	public VirgilConfig(Crypto crypto, VirgilCardsProvider cardsProvider, ChannelInfoProvider channelInfoProvider,
			PrivateKey privateKey) {
		this.crypto = crypto;
		this.cardsProvider = cardsProvider;
		this.channelInfoProvider = channelInfoProvider;
		this.privateKey = privateKey;
	}

	public Crypto getCrypto() {
		return crypto;
	}

	public VirgilCardsProvider getCardsProvider() {
		return cardsProvider;
	}

	public ChannelInfoProvider getChannelInfoProvider() {
		return channelInfoProvider;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

}
