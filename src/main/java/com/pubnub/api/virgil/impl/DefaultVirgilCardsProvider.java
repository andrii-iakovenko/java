package com.pubnub.api.virgil.impl;

import java.util.List;

import com.pubnub.api.virgil.VirgilCardsProvider;
import com.virgilsecurity.sdk.cards.Card;
import com.virgilsecurity.sdk.cards.CardManager;
import com.virgilsecurity.sdk.cards.validation.VirgilCardVerifier;
import com.virgilsecurity.sdk.client.CardClient;
import com.virgilsecurity.sdk.client.exceptions.VirgilServiceException;
import com.virgilsecurity.sdk.crypto.CardCrypto;
import com.virgilsecurity.sdk.crypto.exceptions.CryptoException;
import com.virgilsecurity.sdk.jwt.contract.AccessTokenProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultVirgilCardsProvider implements VirgilCardsProvider {

	private CardManager cardManager;

	public DefaultVirgilCardsProvider(CardCrypto cardCrypto, AccessTokenProvider accessTokenProvider) {
		this.cardManager = new CardManager(cardCrypto, accessTokenProvider, new VirgilCardVerifier(cardCrypto),
				new CardClient());
	}

	@Override
	public Card getCard(String identity) {
		List<Card> cards;
		try {
			cards = this.cardManager.searchCards(identity);
		} catch (CryptoException | VirgilServiceException e) {
			log.error("Card not found for {}", identity, e);
			return null;
		}
		if (cards.isEmpty()) {
			return null;
		}
		return cards.get(0);
	}

}
