package com.github.aheusingfeld.javaone2012.eai.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.ConversionServiceFactory;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.expression.spel.support.StandardTypeConverter;
import org.springframework.integration.Message;
import org.springframework.util.StringUtils;

/**
 * Parses expressions using Spring Expression language.
 * @see {http://static.springsource.org/spring/docs/3.0.x/spring-framework-reference/htmlsingle/spring-framework-reference.html#expressions-language-ref}
 */
public class ExpressionUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ExpressionUtil.class);

    public static final String EXPRESSION_PREFIX = "%{";

    /**
	 * Hidden utility class constructor
	 */
	private ExpressionUtil() {
		// nothing
	}

    /**
     * Parses the specified parameter as a Spring Expression Language (SpEL) expression using the
     * message as the context. <b>IMPORTANT:</b> The expressionString has to match the pattern!
     * @param expressionString - the expression to be parsed via the pattern "%{.*}"
     * @param message - the integration message holding the values
     * @return the extracted value
     * @throws org.springframework.expression.EvaluationException - if there is a problem during evaluation
     * @see "http://static.springsource.org/spring/docs/3.0.x/spring-framework-reference/html/expressions.html"
     */
    public static String parse(final String expressionString, final Message<?> message) {
        final GenericConversionService conversionService = ConversionServiceFactory.createDefaultConversionService();
        // conversionService.addConverter(new UUIDToStringConverter());
        // conversionService.addConverter(new MessageHistoryToStringConverter());
        return parse(expressionString, message, conversionService, String.class);
    }

    /**
     * Parses the specified parameter as a Spring Expression Language (SpEL) expression using the
     * message as the context. <b>IMPORTANT:</b> The expressionString has to match the pattern!
     * @param expressionString - the expression to be parsed via the pattern "%{.*}"
     * @param message - the integration message holding the values
     * @param conversionService - service to use for entity conversions
     * @return the extracted value
     * @throws org.springframework.expression.EvaluationException - if there is a problem during evaluation
     * @see "http://static.springsource.org/spring/docs/3.0.x/spring-framework-reference/html/expressions.html"
     */
    public static String parse(final String expressionString, final Message<?> message, final ConversionService conversionService) {
        return parse(expressionString, message, conversionService, String.class);
    }

    /**
     * Parses the specified parameter as a Spring Expression Language (SpEL) expression using the
     * message as the context. <b>IMPORTANT:</b> The expressionString has to match the pattern!
     * @param expressionString - the expression to be parsed via the pattern "%{.*}"
     * @param message - the integration message holding the values
     * @param conversionService - service to use for entity conversions
     * @return the extracted value
     * @throws org.springframework.expression.EvaluationException - if there is a problem during evaluation
     * @see "http://static.springsource.org/spring/docs/3.0.x/spring-framework-reference/html/expressions.html"
     */
    public static <T> T parse(final String expressionString, final Message<?> message, final ConversionService conversionService, Class<T> desiredResultType) {
        StandardEvaluationContext context = new StandardEvaluationContext(message);
        context.setTypeConverter(new StandardTypeConverter(conversionService));


        final ExpressionParser parser = new SpelExpressionParser();
        final Expression expression = parser.parseExpression(expressionString, new TemplateParserContext(
                EXPRESSION_PREFIX, "}"));
        final T result = expression.getValue(context, desiredResultType);
        LOG.debug("Expression '{}' resolved to '{}'.", expressionString, result);
        return result;
    }

    /**
     * Checks whether the specified string contains the EXPRESSION_PREFIX.
     * @param expressionString the string to check
     * @return true if EXPRESSION_PREFIX is present
     */
    public static boolean containsExpression(final String expressionString)
    {
        return StringUtils.hasText(expressionString) && expressionString.contains(EXPRESSION_PREFIX);
    }
}
