package com.pubnub.api.virgil.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.encryption.MessageCipher;
import com.pubnub.api.virgil.VirgilFacade;
import com.virgilsecurity.sdk.crypto.exceptions.CryptoException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VirgilMessageCipher implements MessageCipher {

	private VirgilFacade virgilFacade;
	private ExecutorService executorService;

	public VirgilMessageCipher(VirgilFacade virgilFacade) {
		this.virgilFacade = virgilFacade;
		this.executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>());
	}

	@Override
	public String encrypt(final String channel, final String message) throws PubNubException {
		try {
            Callable<String> callable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    try {
                        String encryptedMessage = virgilFacade.encrypt(message, channel);
                        return encryptedMessage;
                    }
                    catch (CryptoException e) {
                        log.error("Encryption failed", e);
                        throw new ExecutionException(e);
                    }
                }
            };
            Future<String> future = this.executorService.submit(callable);
            return future.get();
        } catch (InterruptedException  | ExecutionException  e) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.createCryptoError(11, e.toString()))
                    .errormsg(e.getMessage()).build();
        }
	}

	@Override
	public boolean isActive() {
		return virgilFacade != null;
	}

	@Override
	public String decrypt(final String encryptedMessage) throws PubNubException {
		try {
            Callable<String> callable = new Callable<String>() {
                @Override
                public String call() throws Exception {
                    try {
                    String message = virgilFacade.decrypt(encryptedMessage);
                    return message;
                    }
                    catch (CryptoException e) {
                        log.error("Decryption failed", e);
                        throw new ExecutionException(e);
                    }
                }
            };
            Future<String> future = this.executorService.submit(callable);
            return future.get();
        } catch (InterruptedException  | ExecutionException  e) {
            throw PubNubException.builder().errormsg(e.toString()).build();
        }
	}

}
