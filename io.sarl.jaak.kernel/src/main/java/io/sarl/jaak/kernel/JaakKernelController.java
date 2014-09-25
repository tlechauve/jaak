/*
 * $Id$
 *
 * Jaak environment model is an open-source multiagent library.
 * More details on http://www.sarl.io
 *
 * Copyright (C) 2014 Stéphane GALLAND.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.sarl.jaak.kernel;

import io.sarl.jaak.envinterface.time.TimeManager;
import io.sarl.lang.core.Address;
import io.sarl.lang.core.EventSpace;

import java.lang.ref.WeakReference;


/** Standard implementation of a JaakController.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class JaakKernelController implements JaakController {

	private State state = State.NEVER_STARTED;
	
	private WeakReference<EventSpace> space = null;
	private WeakReference<TimeManager> timeManager = null;
	private Address address = null;
	
	/**
	 */
	public JaakKernelController() {
		//
	}
	
	/** Set the communication space used by the controller.
	 * 
	 * @param space the space.
	 * @param address the address.
	 * @param timeManager the time manager.
	 */
	synchronized void initialize(EventSpace space, Address address, TimeManager timeManager) {
		this.space = (space == null) ? null : new WeakReference<>(space);
		this.address = address;
	}
	
	private EventSpace getSpace() {
		return this.space == null ? null : this.space.get();
	}

	private TimeManager getTimeManager() {
		return this.timeManager == null ? null : this.timeManager.get();
	}

	public synchronized void wakeSimulator() {
		if (this.state == State.RUNNING) {
			EventSpace s = getSpace();
			if (s != null) {
				ExecuteSimulationStep event = new ExecuteSimulationStep();
				event.setSource(this.address);
				s.emit(event);
			}
		}
	}
	
	@Override
	public synchronized void startSimulation() {
		if (this.state == State.NEVER_STARTED) {
			EventSpace s = getSpace();
			TimeManager tm = getTimeManager();
			if (s != null && tm != null) {
				this.state = State.RUNNING;
				SimulationStarted startEvent = new SimulationStarted(
						tm.getCurrentTime(),
						tm.getLastStepDuration());
				startEvent.setSource(this.address);
				s.emit(startEvent);
			}
		} else if (this.state == State.PAUSED) {
			EventSpace s = getSpace();
			TimeManager tm = getTimeManager();
			if (s != null && tm != null) {
				this.state = State.RUNNING;
				wakeSimulator();
			}
		}
	}

	@Override
	public synchronized void pauseSimulation() {
		if (this.state == State.RUNNING) {
			this.state = State.PAUSED;
		}
	}

	@Override
	public synchronized void stopSimulation() {
		if (this.state == State.RUNNING || this.state == State.PAUSED) {
			EventSpace s = getSpace();
			TimeManager tm = getTimeManager();
			if (s != null && tm != null) {
				this.state = State.STOPPED;
				SimulationStopped stopEvent = new SimulationStopped(
						tm.getCurrentTime(),
						tm.getLastStepDuration());
				stopEvent.setSource(this.address);
				s.emit(stopEvent);
			}
		}
	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private enum State {
		NEVER_STARTED,
		RUNNING,
		PAUSED,
		STOPPED;
	}

}