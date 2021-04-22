/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.codec.net;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.Test;

/**
 * RFC 1522 compliant codec test cases
 *
 */
public class RFC1522CodecTest {

	public static RFC1522Codec mockRFC1522Codec1() throws DecoderException, EncoderException {
		RFC1522Codec mockInstance = spy(RFC1522Codec.class);
		doAnswer((stubInvo) -> {
			byte[] bytes = stubInvo.getArgument(0);
			return bytes;
		}).when(mockInstance).doEncoding(any(byte[].class));
		doAnswer((stubInvo) -> {
			byte[] bytes = stubInvo.getArgument(0);
			return bytes;
		}).when(mockInstance).doDecoding(any(byte[].class));
		doReturn("T").when(mockInstance).getEncoding();
		return mockInstance;
	}

	@Test
	public void testNullInput() throws Exception, DecoderException, EncoderException {
		final RFC1522Codec testcodec = RFC1522CodecTest.mockRFC1522Codec1();
		assertNull(testcodec.decodeText(null));
		assertNull(testcodec.encodeText(null, CharEncoding.UTF_8));
	}

	private void assertExpectedDecoderException(final String s) throws Exception, DecoderException, EncoderException {
		final RFC1522Codec testcodec = RFC1522CodecTest.mockRFC1522Codec1();
		try {
			testcodec.decodeText(s);
			fail("DecoderException should have been thrown");
		} catch (final DecoderException e) {
			// Expected.
		}
	}

	@Test
	public void testDecodeInvalid() throws Exception {
		assertExpectedDecoderException("whatever");
		assertExpectedDecoderException("=?");
		assertExpectedDecoderException("?=");
		assertExpectedDecoderException("==");
		assertExpectedDecoderException("=??=");
		assertExpectedDecoderException("=?stuff?=");
		assertExpectedDecoderException("=?UTF-8??=");
		assertExpectedDecoderException("=?UTF-8?stuff?=");
		assertExpectedDecoderException("=?UTF-8?T?stuff");
		assertExpectedDecoderException("=??T?stuff?=");
		assertExpectedDecoderException("=?UTF-8??stuff?=");
		assertExpectedDecoderException("=?UTF-8?W?stuff?=");
	}

}
