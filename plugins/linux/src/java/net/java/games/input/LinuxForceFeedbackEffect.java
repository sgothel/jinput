/**
 * Copyright (C) 2003 Jeremy Booth (jeremy@newdawnsoftware.com)
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this 
 * list of conditions and the following disclaimer. Redistributions in binary 
 * form must reproduce the above copyright notice, this list of conditions and 
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. 
 * The name of the author may not be used to endorse or promote products derived
 * from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO 
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE
 */
package net.java.games.input;

import java.io.IOException;

/**
 * @author elias
 */
abstract class LinuxForceFeedbackEffect implements Rumbler {
	private final LinuxEventDevice device;
	private final int ff_id;
	
    public LinuxForceFeedbackEffect(LinuxEventDevice device) throws IOException {
		this.device = device;
		this.ff_id = upload(-1, 0);
    }

	protected abstract int upload(int id, float intensity) throws IOException;
	
	private final void write(int value) throws IOException {
		device.writeEvent(NativeDefinitions.EV_FF, ff_id, value);
	}

	protected final LinuxEventDevice getDevice() {
		return device;
	}
	
	public final void rumble(float intensity) {
		try {
			if (intensity > 0) {
				upload(ff_id, intensity);
				write(1);
			} else {
				write(0);
			}
		} catch (IOException e) {
			ControllerEnvironment.logln("Failed to rumble: " + e);
		}
	}

	public final void erase() throws IOException {
		device.eraseEffect(ff_id);
	}

	public final String getAxisName() {
		return null;
	}

	public final Component.Identifier getAxisIdentifier() {
		return null;
	}
}
