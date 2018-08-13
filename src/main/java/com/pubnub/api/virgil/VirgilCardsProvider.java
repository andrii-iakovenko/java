package com.pubnub.api.virgil;

import com.virgilsecurity.sdk.cards.Card;

public interface VirgilCardsProvider {
	
	Card getCard(String identity);

}
