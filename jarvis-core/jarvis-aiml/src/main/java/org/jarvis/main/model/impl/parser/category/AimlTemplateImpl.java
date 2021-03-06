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
import org.jarvis.main.model.parser.category.IAimlTemplate;

public class AimlTemplateImpl extends AimlElementContainer implements IAimlTemplate {
	
	IAimlTopic topic;

	/**
	 * The pattern-side that element is a special type of pattern element used
	 * for context matching. The pattern-side that is optional in a category,
	 * but if it occurs it must occur no more than once, and must immediately
	 * follow the pattern and immediately precede the template. A pattern-side
	 * that element contains a simple pattern expression.
	 */
	public AimlTemplateImpl(IAimlTopic topic) {
		super("template");
		this.topic = topic;
	}

	@Override
	public IAimlTopic getTopic() {
		return topic;
	}

	@Override
	public String toString() {
		return "\n\t\t\tAimlTemplate [elements=" + elements + "]";
	}
}
