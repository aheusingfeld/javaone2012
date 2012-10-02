/**
 *
 */
package com.github.aheusingfeld.javaone2012.eai.interceptors;

import com.github.aheusingfeld.javaone2012.eai.util.ExpressionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.channel.interceptor.ChannelInterceptorAdapter;
import org.springframework.integration.history.MessageHistory;
import org.springframework.util.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Can be configured as an interceptor to any Spring Integration channel and logs information
 * on posted messages.
 */
public class LoggingChannelInterceptor extends ChannelInterceptorAdapter {

	private static final String LOG_PATTERN = "Channel '{}' got message '{}'";
	private Logger logger = LoggerFactory.getLogger(LoggingChannelInterceptor.class);
	private static enum Level { ERROR, WARN, INFO, DEBUG }
	/**
	 * The default expression which will be used if no expression is sepcified.
	 * Will log message_id and history.
	 */
	private String expression = "%{headers['" + MessageHeaders.ID + "']} - history: %{headers['" + MessageHistory.HEADER_NAME + "']}";
	private Level level = Level.INFO;

	/**
	 * @param expression the expression to parse the message with
	 */
	public void setExpression(String expression) {
		this.expression = expression;
	}

	/**
	 * @param level the log level the message shall be logged in
	 */
	public void setLevel(String level) {
		try {
			this.level = Level.valueOf(level.toUpperCase());
		}
		catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid log level '" + level +
					"'. The (case-insensitive) supported values are: " + StringUtils.arrayToCommaDelimitedString(Level.values()));
		}
	}

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		try {
			Object logMessage = ExpressionUtil.parse(expression, message);
			if (logMessage instanceof Throwable) {
				StringWriter stringWriter = new StringWriter();
				((Throwable) logMessage).printStackTrace(new PrintWriter(stringWriter, true));
				logMessage = stringWriter.toString();
			}
			switch (this.level) {
				case ERROR :
					logger.error(LOG_PATTERN , channel, logMessage);
					break;
				case WARN :
					logger.warn(LOG_PATTERN , channel, logMessage);
					break;
				case INFO :
					logger.info(LOG_PATTERN , channel, logMessage);
					break;
				case DEBUG :
					logger.debug(LOG_PATTERN , channel, logMessage);
					break;
			}
		} catch (Exception e) {
			logger.error("Could not log information for message '" + message + "'", e);
		}
		return message;
	}

}
