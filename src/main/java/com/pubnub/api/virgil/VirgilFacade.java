package com.pubnub.api.virgil;

import java.util.ArrayList;
import java.util.List;

import com.virgilsecurity.sdk.cards.Card;
import com.virgilsecurity.sdk.crypto.PublicKey;
import com.virgilsecurity.sdk.crypto.VirgilPrivateKey;
import com.virgilsecurity.sdk.crypto.VirgilPublicKey;
import com.virgilsecurity.sdk.crypto.exceptions.CryptoException;
import com.virgilsecurity.sdk.utils.Base64Url;
import com.virgilsecurity.sdk.utils.ConvertionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VirgilFacade {

	private VirgilConfig config;

	public VirgilFacade(VirgilConfig config) {
		super();
		this.config = config;
	}

	public String encrypt(String message, String channel) throws CryptoException {
		log.debug("Encrypt message for channel {}", channel);

		List<String> members = this.config.getChannelInfoProvider().getChannelMembers(channel);
		if (members.isEmpty()) {
			log.debug("Channel {} is empty. Message not encrypted", channel);
			return message;
		}
		List<PublicKey> recipients = new ArrayList<>(members.size());
		for (String member : members) {
			Card card = this.config.getCardsProvider().getCard(member);
			if (card != null) {
				recipients.add(card.getPublicKey());
				if (card.getPublicKey() instanceof VirgilPublicKey) {
					log.debug("Encrypt message for channel {} and recipient {}", channel,
							ConvertionUtils.toHex(((VirgilPublicKey) card.getPublicKey()).getIdentifier()));
				}
			}
		}
		byte[] messageData = ConvertionUtils.toBytes(message);
		byte[] decryptedMessageData = this.config.getCrypto().encrypt(messageData, recipients);
		return Base64Url.encode(decryptedMessageData);
	}

	public String decrypt(String message) throws CryptoException {
		if (this.config.getPrivateKey() instanceof VirgilPrivateKey) {
			log.debug("Decrypt message {} with key {}", message,
					ConvertionUtils.toHex(((VirgilPrivateKey) this.config.getPrivateKey()).getIdentifier()));
		} else {
			log.debug("Decrypt message {}", message);
		}
		byte[] encryptedMessageData = Base64Url.decodeToBytes(message);
		byte[] decryptedMessage = this.config.getCrypto().decrypt(encryptedMessageData, this.config.getPrivateKey());
		return ConvertionUtils.toString(decryptedMessage);
	}

}
