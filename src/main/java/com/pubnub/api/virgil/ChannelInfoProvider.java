package com.pubnub.api.virgil;

import java.util.List;

public interface ChannelInfoProvider {
	
	List<String> getChannelMembers(String channel);

}
