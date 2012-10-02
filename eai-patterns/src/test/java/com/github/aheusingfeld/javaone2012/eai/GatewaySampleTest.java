package com.github.aheusingfeld.javaone2012.eai;

import com.github.aheusingfeld.javaone2012.eai.gateways.CustomGateway;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.internal.matchers.StringContains.containsString;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:spring/gateway-sample.xml")
public class GatewaySampleTest
{

	@Autowired
	private CustomGateway partnerGatewaySend;


	@Test
	public void matchingCall() throws Exception
	{
        final String message = "<ROWSET><ROW><DATA>my sample content</DATA></ROW></ROWSET>";

        System.out.println(System.currentTimeMillis() + " - Request started.");
        String response = partnerGatewaySend.sendMessage(message);
        System.out.println(System.currentTimeMillis() + " - Reply timeout reached.");

        // don't expect response as 'sample content' is sent
        assertThat(response, is(nullValue()));

	}

    @Test
    public void nonMatchingContent() throws Exception
    {
        final String message = "<ROWSET><ROW><USER_ORGANIZATION>1991919</USER_ORGANIZATION><USERTOKEN>nothing</USERTOKEN></ROW></ROWSET>";

        String response = partnerGatewaySend.sendMessage(message);
        // expect response as message doesn't match the filter
        assertThat(response, notNullValue());
        // make sure the response equals the message send
        assertThat(String.valueOf(response), equalTo(message));

    }
}
