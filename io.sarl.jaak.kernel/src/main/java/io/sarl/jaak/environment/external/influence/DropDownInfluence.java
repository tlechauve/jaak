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
package io.sarl.jaak.environment.external.influence;

import io.sarl.jaak.environment.external.body.TurtleObject;
import io.sarl.jaak.environment.external.perception.EnvironmentalObject;

/** This class defines an influence to drop off an object
 * inside the environment.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DropDownInfluence extends Influence {

	private final EnvironmentalObject object;

	/**
	 * @param emitter is the emitter of the influence.
	 * @param object is the environmental object to drop off.
	 */
	public DropDownInfluence(TurtleObject emitter, EnvironmentalObject object) {
		super(emitter);
		this.object = object;
	}

	/** Replies the environmental object which may be dropped off.
	 *
	 * @return the environmental object which may be dropped off.
	 */
	public EnvironmentalObject getDropOffObject() {
		return this.object;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(getEmitter().getTurtleId().toString());
		buffer.append(": drop "); //$NON-NLS-1$
		buffer.append(this.object == null ? null : this.object.toString());
		return buffer.toString();
	}

}
