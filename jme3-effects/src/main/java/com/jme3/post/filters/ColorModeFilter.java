/*
 * Copyright (c) 2009-2024 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jme3.post.filters;

import com.jme3.asset.AssetManager;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.material.Material;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import java.io.IOException;

/**
 * A screen-space post-processing filter that applies a color mode transformation
 * to the entire rendered scene.
 *
 * <p>Supported modes:
 * <ul>
 *   <li>{@link Mode#GRAYSCALE} - converts the image to grayscale using a
 *       luminance-weighted dot product (BT.601 coefficients)</li>
 *   <li>{@link Mode#INVERT_COLORS} - inverts every color channel
 *       ({@code rgb = vec3(1.0) - rgb})</li>
 * </ul>
 *
 * <p>Example usage:
 * <pre>
 *   FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
 *   ColorModeFilter filter = new ColorModeFilter();
 *   filter.setMode(ColorModeFilter.Mode.GRAYSCALE);
 *   fpp.addFilter(filter);
 *   viewPort.addProcessor(fpp);
 * </pre>
 *
 * @author Ziyao Ma
 */
public class ColorModeFilter extends Filter {

    /**
     * The color transformation mode applied by this filter.
     */
    public enum Mode {
        /** Convert the scene to grayscale. */
        GRAYSCALE,
        /** Invert all color channels. */
        INVERT_COLORS
    }

    private Mode mode = Mode.GRAYSCALE;

    /**
     * Creates a ColorModeFilter with the default mode ({@link Mode#GRAYSCALE}).
     */
    public ColorModeFilter() {
        super("ColorModeFilter");
    }

    /**
     * Creates a ColorModeFilter with the specified mode.
     *
     * @param mode the initial color mode (not null)
     */
    public ColorModeFilter(Mode mode) {
        super("ColorModeFilter");
        this.mode = mode;
    }

    @Override
    protected void initFilter(AssetManager manager,
            RenderManager renderManager, ViewPort vp, int w, int h) {
        material = new Material(manager, "Common/MatDefs/Post/ColorMode.j3md");
        material.setInt("Mode", mode.ordinal());
    }

    @Override
    protected Material getMaterial() {
        return material;
    }

    /**
     * Returns the current color mode.
     *
     * @return the current {@link Mode}
     */
    public Mode getMode() {
        return mode;
    }

    /**
     * Sets the color mode applied by this filter.
     * Can be called before or after the filter is initialized.
     *
     * @param mode the desired color mode (not null)
     */
    public void setMode(Mode mode) {
        this.mode = mode;
        if (material != null) {
            material.setInt("Mode", mode.ordinal());
        }
    }

    /**
     * Load properties when the filter is de-serialized, for example when
     * loading from a J3O file.
     *
     * @param importer the importer to use (not null)
     * @throws IOException from the importer
     */
    @Override
    public void read(JmeImporter importer) throws IOException {
        super.read(importer);
        InputCapsule capsule = importer.getCapsule(this);
        String modeName = capsule.readString("mode", Mode.GRAYSCALE.name());
        this.mode = Mode.valueOf(modeName);
    }

    /**
     * Save properties when the filter is serialized, for example when saving
     * to a J3O file.
     *
     * @param exporter the exporter to use (not null)
     * @throws IOException from the exporter
     */
    @Override
    public void write(JmeExporter exporter) throws IOException {
        super.write(exporter);
        OutputCapsule capsule = exporter.getCapsule(this);
        capsule.write(mode.name(), "mode", Mode.GRAYSCALE.name());
    }
}
