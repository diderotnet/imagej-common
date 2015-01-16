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

package net.imagej.display;

import net.imagej.ChannelCollection;
import net.imagej.Dataset;
import net.imagej.Position;
import net.imagej.options.OptionsChannels;
import net.imagej.overlay.Overlay;

import org.scijava.Priority;
import org.scijava.module.Module;
import org.scijava.module.ModuleItem;
import org.scijava.module.ModuleService;
import org.scijava.module.process.AbstractPreprocessorPlugin;
import org.scijava.module.process.PreprocessorPlugin;
import org.scijava.options.OptionsService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Assigns the active {@link ImageDisplay} when there is one single unresolved
 * {@link ImageDisplay} parameter. Hence, rather than a dialog prompting the
 * user to choose a {@link ImageDisplay}, the active {@link ImageDisplay} is
 * used automatically.
 * <p>
 * In the case of more than one {@link ImageDisplay} parameter, the active
 * {@link ImageDisplay} is not used and instead the user must select. This
 * behavior is consistent with ImageJ v1.x.
 * </p>
 * <p>
 * The same process is applied for {@link DataView} and {@link Dataset}
 * parameters, using the active {@link ImageDisplay}'s active {@link DataView}
 * and {@link Dataset}, respectively.
 * </p>
 * 
 * @author Curtis Rueden
 */
@Plugin(type = PreprocessorPlugin.class, priority = Priority.VERY_HIGH_PRIORITY)
public class ActiveImagePreprocessor extends AbstractPreprocessorPlugin {

	@Parameter(required = false)
	private ModuleService moduleService;

	@Parameter(required = false)
	private ImageDisplayService imageDisplayService;

	@Parameter(required = false)
	private OverlayService overlayService;

	@Parameter(required = false)
	private OptionsService optionsService;

	// -- ModuleProcessor methods --

	@Override
	public void process(final Module module) {
		if (imageDisplayService == null) return;
		final ImageDisplay activeDisplay =
			imageDisplayService.getActiveImageDisplay();
		if (activeDisplay == null) return;

		// assign active display to single ImageDisplay input
		final String displayInput = getSingleInput(module, ImageDisplay.class);
		if (displayInput != null) {
			module.setInput(displayInput, activeDisplay);
			module.setResolved(displayInput, true);
		}

		// assign active dataset view to single DatasetView input
		final String datasetViewInput = getSingleInput(module, DatasetView.class);
		final DatasetView activeDatasetView =
			imageDisplayService.getActiveDatasetView();
		if (datasetViewInput != null && activeDatasetView != null) {
			module.setInput(datasetViewInput, activeDatasetView);
			module.setResolved(datasetViewInput, true);
		}

		// assign active display view to single DataView input
		final String dataViewInput = getSingleInput(module, DataView.class);
		final DataView activeDataView = activeDisplay.getActiveView();
		if (dataViewInput != null && activeDataView != null) {
			module.setInput(dataViewInput, activeDataView);
			module.setResolved(dataViewInput, true);
		}

		// assign active dataset to single Dataset input
		final String datasetInput = getSingleInput(module, Dataset.class);
		final Dataset activeDataset = imageDisplayService.getActiveDataset();
		if (datasetInput != null && activeDataset != null) {
			module.setInput(datasetInput, activeDataset);
			module.setResolved(datasetInput, true);
		}

		// assign active dataset view's position to single Position input
		final String positionInput = getSingleInput(module, Position.class);
		if (positionInput != null) {
			final Position activePosition =
				imageDisplayService.getActivePosition(activeDisplay);
			if (activePosition != null) {
				module.setInput(positionInput, activePosition);
				module.setResolved(positionInput, true);
			}
		}

		// assign active overlay to single Overlay input
		if (overlayService != null) {
			final String overlayInput = getSingleInput(module, Overlay.class);
			final Overlay activeOverlay =
				overlayService.getActiveOverlay(activeDisplay);
			if (overlayInput != null && activeOverlay != null) {
				module.setInput(overlayInput, activeOverlay);
				module.setResolved(overlayInput, true);
			}
		}

		// TODO: Split this into its own OptionsPreprocessor,
		// which extends a common AbstractSingleInputPreprocessor.
		if (optionsService != null) {
			final String channelCollectionInput =
				getSingleInput(module, ChannelCollection.class);
			final OptionsChannels optionsChannels =
				optionsService.getOptions(OptionsChannels.class);
			ChannelCollection defaultColor = null;
			if (optionsChannels != null) {
				defaultColor = optionsChannels.getFgValues();
			}
			if (channelCollectionInput != null && defaultColor != null) {
				module.setInput(channelCollectionInput, defaultColor);
				module.setResolved(channelCollectionInput, true);
			}
		}
	}

	// -- Helper methods --

	private String getSingleInput(final Module module, final Class<?> type) {
		if (moduleService == null) return null;
		final ModuleItem<?> item = moduleService.getSingleInput(module, type);
		if (item == null || !item.isAutoFill()) return null;
		return item.getName();
	}

}
