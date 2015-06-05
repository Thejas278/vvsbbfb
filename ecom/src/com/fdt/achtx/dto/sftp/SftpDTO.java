package com.fdt.achtx.dto.sftp;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;

public class SftpDTO {

	private ChannelSftp channelSftp = null;
	
	private Channel channel = null;
	
	private Session session = null;

	public ChannelSftp getChannelSftp() {
		return channelSftp;
	}

	public void setChannelSftp(ChannelSftp channelSftp) {
		this.channelSftp = channelSftp;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}	
	
}
