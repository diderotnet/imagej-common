/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2009 - 2015 Board of Regents of the University of
 * Wisconsin-Madison, Broad Institute of MIT and Harvard, and Max Planck
 * Institute of Molecular Cell Biology and Genetics.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */
package net.imagej.converters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.imglib2.Dimensions;

import org.junit.Test;
import org.scijava.convert.Converter;

/**
 * Tests converters from native java arrays to FinalIntervals
 * 
 * @author Christian Dietz, University of Konstanz
 */
public class ConvertArrayToFinalIntervalTest {

	@Test
	public void convertLongArrayToDimensionsTest() {

		long[] dims = new long[] { 1, 2, 3 };

		final Converter<long[], ? extends Dimensions> converter = new ConvertLongArrayToFinalInterval();
		assertTrue(converter.canConvert(dims, Dimensions.class));

		final Dimensions dimensions = converter.convert(dims, Dimensions.class);

		assertTrue(dimensions != null);

		for (int d = 0; d < dimensions.numDimensions(); d++) {
			assertEquals(dims[d], dimensions.dimension(d));
		}

	}

	@Test
	public void convertIntArrayToDimensionsTest() {

		int[] dims = new int[] { 1, 2, 3 };

		final Converter<int[], ? extends Dimensions> converter = new ConvertIntArrayToFinalInterval();
		assertTrue(converter.canConvert(dims, Dimensions.class));

		final Dimensions dimensions = converter.convert(dims, Dimensions.class);

		assertTrue(dimensions != null);

		for (int d = 0; d < dimensions.numDimensions(); d++) {
			assertEquals(dims[d], dimensions.dimension(d));
		}

	}
}