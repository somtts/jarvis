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

package org.jarvis.main.engine.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.jarvis.main.engine.IAimlCoreEngine;
import org.jarvis.main.engine.IAimlCoreTransactionMonitor;
import org.jarvis.main.engine.impl.transform.AimlScoreImpl;
import org.jarvis.main.engine.impl.transform.AimlTranformImpl;
import org.jarvis.main.engine.transform.IAimlScore;
import org.jarvis.main.engine.transform.IAimlTransform;
import org.jarvis.main.exception.AimlParsingError;
import org.jarvis.main.model.impl.parser.AimlRepository;
import org.jarvis.main.model.impl.parser.AimlResult;
import org.jarvis.main.model.impl.parser.history.AimlHistoryImpl;
import org.jarvis.main.model.impl.transform.DebugTableImpl;
import org.jarvis.main.model.parser.IAimlCategory;
import org.jarvis.main.model.parser.IAimlRepository;
import org.jarvis.main.model.parser.IAimlResult;
import org.jarvis.main.model.parser.IAimlTopic;
import org.jarvis.main.model.parser.history.IAimlHistory;
import org.jarvis.main.model.transform.IDebugTable;
import org.jarvis.main.model.transform.ITransformedItem;
import org.jarvis.main.parser.AimlParserImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @core engine
 *
 */
public class AimlCoreEngineImpl implements IAimlCoreEngine {

	protected Logger logger = LoggerFactory.getLogger(AimlCoreEngineImpl.class);

	private final IAimlRepository aiml = new AimlRepository();

	private final IAimlTransform transformer = new AimlTranformImpl();
	private final List<File> resources = new ArrayList<File>();
	private final Stack<IAimlCategory> stack = new Stack<IAimlCategory>();

	/**
	 * local properties
	 */
	private final Map<String, Object> bot = new HashMap<String, Object>();
	private final Map<String, Object> properties = new HashMap<String, Object>();

	/**
	 * user inputs logs
	 */
	private final Stack<List<IAimlHistory>> history = new Stack<List<IAimlHistory>>();

	/**
	 * default constructor
	 */
	public AimlCoreEngineImpl() {
	}

	/**
	 * standard constructor to build from resource list a new engine
	 * 
	 * @param resources
	 * @throws IOException
	 * @throws AimlParsingError
	 */
	public AimlCoreEngineImpl(List<String> resources) throws IOException,
			AimlParsingError {
		for (String resource : resources) {
			register(resource);
		}
		parse();
	}

	@Override
	public Stack<List<IAimlHistory>> getHistory() {
		return history;
	}

	@Override
	public IAimlHistory getThatHistory() {
		List<IAimlHistory> result = getThatsHistory();
		if (result == null)
			return null;
		if (result.size() == 0)
			return null;
		return result.get(result.size() - 1);
	}

	@Override
	public List<IAimlHistory> getThatsHistory() {
		if (history.size() == 0)
			return null;
		return history.get(history.size() - 1);
	}

	@Override
	public Object getBot(String key) {
		return bot.get(key);
	}

	@Override
	public Object setBot(String key, Object value) {
		logger.info("set (bot) " + key + " to " + value);
		return bot.put(key, value);
	}

	@Override
	public Object get(String key) {
		return properties.get(key);
	}

	@Override
	public Object set(String key, Object value) {
		logger.info("set (object) " + key + " to " + value);
		if (value != null)
			return properties.put(key, value);
		else
			return properties.put(key, "");
	}

	@Override
	public void register(File resource) {
		resources.add(resource);
	}

	@Override
	public void register(String resource) throws IOException {
		InputStream local = this.getClass().getClassLoader()
				.getResourceAsStream(resource);
		if (local == null) {
			logger.warn("Unable to find resource " + resource);
			return;
		}
		File data = File
				.createTempFile((new File(resource)).getName(), "-aiml");
		data.deleteOnExit();
		logger.info("Register " + data.getAbsolutePath());
		OutputStream out = new FileOutputStream(data);
		byte[] b = new byte[1024];
		for (int length = local.read(b); length > 0; length = local.read(b)) {
			out.write(b, 0, length);
		}
		out.close();
		local.close();
		register(data);
	}

	@Override
	public void parse() throws AimlParsingError {
		IAimlRepository result = null;
		for (File file : resources) {
			result = AimlParserImpl.parse(file);
			/**
			 * add all resources to current repository
			 */
			for (IAimlTopic topic : result.getTopics()) {
				aiml.addTopic(topic);
			}
			logger.info("Succesfull insert of " + result.getTopics().size()
					+ " topic(s)");
			for (IAimlCategory cat : result.getCategories()) {
				aiml.addCategory(cat);
			}
			logger.info("Succesfull insert of " + result.getCategories().size()
					+ " categorie(s)");
		}

		/**
		 * transformation on all category for optimisation during runtime
		 */
		for (IAimlCategory cat : aiml.getCategories()) {
			IAimlHistory history = new AimlHistoryImpl();
			cat.setHistory(history);

			/**
			 * optimisation : transformation on categories only deal with the
			 * first sentence
			 */
			history.setTransformedPattern(transformer.transform(cat.getTopic(),
					cat.getPattern().getElements()).get(0));
			if (cat.hasThat()) {
				history.setTransformedAnswer(transformer.transform(
						cat.getTopic(), cat.getThat().getElements()).get(0));
			}
		}
	}

	static IDebugTable debug = DebugTableImpl.instance;

	@Override
	public List<IAimlHistory> ask(String sentence) throws AimlParsingError {
		List<IAimlHistory> localHistory = new ArrayList<IAimlHistory>();

		/**
		 * transform user sentence into common tranform state
		 */
		List<ITransformedItem> tx = transformer.transform(sentence);
		for (ITransformedItem s : tx) {
			IAimlHistory log = new AimlHistoryImpl(s.getRaw(), ask(s),
					transformer);
			localHistory.add(log);
		}

		if (logger.isDebugEnabled()) {
			File output = null;
			try {
				output = debug.store();
				logger.info("Html debug stored in " + output.getAbsolutePath());
			} catch (IOException e) {
				logger.error("Error, while debugging {}", e);
			}
		}

		/**
		 * that become the last computed item
		 */
		getHistory().push(localHistory);
		if (logger.isDebugEnabled()) {
			logger.debug("getHistory: " + getHistory());
		}

		return localHistory;
	}

	@Override
	public List<IAimlCategory> getCategories() {
		return aiml.getCategories();
	}

	static int sessionId = 0;

	/**
	 * internal use
	 * 
	 * @param table
	 * @param availableCategories
	 * @param sentence
	 */
	private void addCategories(IDebugTable table,
			List<IAimlCategory> availableCategories, ITransformedItem sentence) {
		/**
		 * for debugging purpose
		 */
		if (table != null) {
			table.addCategories(availableCategories, sentence,
					(String) get("topic"));
		}
	}

	/**
	 * internal use
	 * 
	 * @param table
	 * @param sessiondIdText
	 * @param catXmlValue
	 * @param score
	 * @param toScore
	 * @param found
	 * @param old
	 */
	private void addFound(IDebugTable table, String sessiondIdText,
			String catXmlValue, int score, ITransformedItem toScore,
			IAimlScore found, IAimlScore old) {
		/**
		 * for debugging purpose
		 */
		if (table != null) {
			table.addFound(sessiondIdText, catXmlValue, score, toScore, found,
					old);
		}
	}

	/**
	 * ask to this bot something
	 * 
	 * @param sentence
	 * @return
	 * @throws AimlParsingError
	 */
	private IAimlResult ask(ITransformedItem sentence) throws AimlParsingError {
		IAimlScore found = null;
		String topic = (String) get("topic");
		if (topic == null)
			topic = "";
		/**
		 * find the category with pattern match algorithm based on scoring
		 */
		List<IAimlCategory> availableCategories = aiml
				.getCategoriesFilteredByTopic(topic);

		Thread.currentThread().getId();

		/**
		 * for debugging purpose
		 */
		addCategories(debug, availableCategories, sentence);

		for (int iCat = 0; iCat < availableCategories.size(); iCat++) {
			IAimlCategory cat = availableCategories.get(iCat);
			String sessiondIdText = "[" + sessionId + "/" + iCat + "]";
			String catXmlValue = sessiondIdText
					+ cat.toAiml(new StringBuilder()).toString()
							.replace("\n", "").replace("\r", "");

			/**
			 * ignore stacked category to avoid recursion
			 */
			if (stack.contains(cat)) {
				logger.info(catXmlValue);
				logger.info(sessiondIdText + " => Ignore");
				addFound(debug, sessiondIdText + "#0", catXmlValue, -2, null,
						null, null);
				continue;
			}

			ITransformedItem toScore = cat.getHistory().getTransformedPattern();
			int score = sentence.score(toScore);

			addFound(debug, sessiondIdText + "#1", catXmlValue, score, toScore,
					null, null);

			/**
			 * handle that element - if score is ok - category have that element
			 * - current that is not null
			 */
			if (score >= 0) {

				/**
				 * that statement cound improve the current score
				 */
				if (cat.hasThat()) {
					if (getHistory().size() > 0 && getThatHistory() != null) {
						ITransformedItem transformed = getThatHistory()
								.getTransformedAnswer();
						/**
						 * transformed could be null if no viable history
						 */
						if (transformed != null) {
							int thatScore = transformed.score(cat.getHistory()
									.getTransformedAnswer());

							logger.info(sessiondIdText
									+ " => That improve score : " + thatScore);

							if (thatScore > 0) {
								score += score;
							}
						} else {
							logger.info("Null transformed history");
						}
					} else {
						/**
						 * that must be matched but, no history
						 */
						logger.info(sessiondIdText
								+ " => That needed but no history");
					}
				}
			}

			addFound(debug, sessiondIdText + "#2", catXmlValue, score, toScore,
					null, null);

			/**
			 * scored item, but more scored ?
			 */
			if (score >= 0) {
				logger.info(catXmlValue);
				logger.info(sessiondIdText + " => Score  : " + score);
				logger.info(sessiondIdText + " => Scored : " + cat.hasThat()
						+ "/" + getHistory().size());

				if (found == null) {
					found = new AimlScoreImpl(score, cat);
					addFound(debug, sessiondIdText + "#3", catXmlValue, score,
							toScore, found, null);
				} else if (found.getKey() < score) {
					logger.info(sessiondIdText + " => Replace  : "
							+ found.getKey() + " by " + score);
					IAimlScore old = found;
					found = new AimlScoreImpl(score, cat);
					addFound(debug, sessiondIdText + "#4", catXmlValue, score,
							toScore, found, old);
				}
			} else {
				addFound(debug, sessiondIdText + "#5", catXmlValue, score,
						toScore, null, null);
			}
		}

		if (found != null) {
			IAimlCategory category = found.getValue();
			/**
			 * identify star value
			 */
			stack.push(category);
			transactionMonitor.getTransaction().push(category);
			IAimlResult speech = new AimlResult();
			category.answer(this, sentence.star(category.getHistory()
					.getTransformedPattern(), new ArrayList<String>()),
					getThatHistory(), speech);
			transactionMonitor.getTransaction().pop(speech);
			stack.pop();

			transactionMonitor.info("Ask for : " + sentence + " => " + speech);

			return speech;
		} else {
			IAimlResult speech = new AimlResult();
			speech.append("?");
			return speech;
		}
	}

	@Override
	public void setLastAnswer(IAimlResult reply) throws AimlParsingError {
		IAimlHistory e = new AimlHistoryImpl("", reply, transformer);
		getThatsHistory().add(e);
	}

	@Override
	public IAimlRepository getAiml() {
		return aiml;
	}

	IAimlCoreTransactionMonitor transactionMonitor = new AimlCoreTransactionMonitorImpl();

	@Override
	public IAimlCoreTransactionMonitor getTransactionMonitor() {
		return transactionMonitor;
	}
}
