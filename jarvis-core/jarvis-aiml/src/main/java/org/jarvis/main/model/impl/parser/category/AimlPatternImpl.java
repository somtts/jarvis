/**
 *  Copyright 2012 Yannick Roffin
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.jarvis.main.model.impl.parser.category;

import org.jarvis.main.model.impl.parser.AimlElementContainer;
import org.jarvis.main.model.parser.IAimlTopic;
import org.jarvis.main.model.parser.category.IAimlPattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AimlPatternImpl extends AimlElementContainer implements
		IAimlPattern {
	protected Logger logger = LoggerFactory.getLogger(AimlPatternImpl.class);

	private IAimlTopic topic;

	/**
	 * A pattern is an element whose content is a mixed pattern expression.
	 * Exactly one pattern must appear in each category. The pattern must always
	 * be the first child element of the category. A pattern does not have any
	 * attributes.
	 */
	public AimlPatternImpl(IAimlTopic topic) {
		super("pattern");
		this.topic = topic;
	}

	@Override
	public IAimlTopic getTopic() {
		return topic;
	}

	@Override
	public void setTopic(IAimlTopic topic) {
		this.topic = topic;
	}

	@Override
	public String toString() {
		return "\n\t\t\tAimlPattern [elements=" + elements + "]";
	}
}
