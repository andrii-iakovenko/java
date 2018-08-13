package com.pubnub.api.virgil;

import java.util.List;

import com.virgilsecurity.sdk.crypto.PrivateKey;
import com.virgilsecurity.sdk.crypto.PublicKey;
import com.virgilsecurity.sdk.crypto.exceptions.CryptoException;

public interface Crypto {
	
	byte[] encrypt(byte[] data, List<PublicKey> recipients) throws CryptoException;
	
	byte[] decrypt(byte[] data, PrivateKey privateKey) throws CryptoException;

}
