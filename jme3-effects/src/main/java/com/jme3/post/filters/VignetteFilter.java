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
import com.jme3.math.ColorRGBA;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import java.io.IOException;

/**
 * A screen-space post-processing filter that darkens (or tints) the edges of
 * the rendered scene, drawing the viewer's eye toward the center of the screen.
 *
 * <p>The effect is controlled by three parameters:
 * <ul>
 *   <li><b>innerRadius</b> – normalized distance from the screen center at which
 *       the vignette begins to fade in (default 0.3). Values are in the range
 *       [0, 1] where 0 is the center and ~0.707 is the corner of a square
 *       viewport.</li>
 *   <li><b>outerRadius</b> – normalized distance at which the vignette reaches
 *       full intensity (default 0.75). Must be greater than innerRadius.</li>
 *   <li><b>intensity</b> – how strongly the vignette color is blended over the
 *       scene at the outermost region (default 0.8, range [0, 1]).</li>
 * </ul>
 *
 * <p>The vignette color defaults to opaque black ({@link ColorRGBA#Black}) but
 * can be set to any color to produce creative tinted-edge effects.
 *
 * <p>Example usage:
 * <pre>
 *   FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
 *   VignetteFilter vignette = new VignetteFilter();
 *   vignette.setInnerRadius(0.3f);
 *   vignette.setOuterRadius(0.75f);
 *   vignette.setIntensity(0.8f);
 *   fpp.addFilter(vignette);
 *   viewPort.addProcessor(fpp);
 * </pre>
 *
 * @author Ziyao Ma
 */
public class VignetteFilter extends Filter {

    private float innerRadius = 0.3f;
    private float outerRadius = 0.75f;
    private float intensity   = 0.8f;
    private ColorRGBA color   = ColorRGBA.Black.clone();

    /**
     * Creates a VignetteFilter with default parameters:
     * innerRadius=0.3, outerRadius=0.75, intensity=0.8, color=Black.
     */
    public VignetteFilter() {
        super("VignetteFilter");
    }

    /**
     * Creates a VignetteFilter with the specified radii and intensity.
     *
     * @param innerRadius normalized distance where the vignette starts (0–1)
     * @param outerRadius normalized distance where the vignette is fully opaque (0–1)
     * @param intensity   blend strength at the outer edge (0–1)
     */
    public VignetteFilter(float innerRadius, float outerRadius, float intensity) {
        super("VignetteFilter");
        this.innerRadius = innerRadius;
        this.outerRadius = outerRadius;
        this.intensity   = intensity;
    }

    @Override
    protected void initFilter(AssetManager manager,
            RenderManager renderManager, ViewPort vp, int w, int h) {
        material = new Material(manager, "Common/MatDefs/Post/Vignette.j3md");
        material.setFloat("InnerRadius", innerRadius);
        material.setFloat("OuterRadius", outerRadius);
        material.setFloat("Intensity",   intensity);
        material.setColor("Color",       color);
    }

    @Override
    protected Material getMaterial() {
        return material;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    /**
     * Returns the inner radius of the vignette.
     *
     * @return inner radius (normalized, 0–1)
     */
    public float getInnerRadius() {
        return innerRadius;
    }

    /**
     * Sets the inner radius: the normalized distance from the screen center at
     * which the vignette begins to fade in.
     *
     * @param innerRadius inner radius (0–1, must be less than outerRadius)
     */
    public void setInnerRadius(float innerRadius) {
        this.innerRadius = innerRadius;
        if (material != null) {
            material.setFloat("InnerRadius", innerRadius);
        }
    }

    /**
     * Returns the outer radius of the vignette.
     *
     * @return outer radius (normalized, 0–1)
     */
    public float getOuterRadius() {
        return outerRadius;
    }

    /**
     * Sets the outer radius: the normalized distance at which the vignette
     * reaches full intensity.
     *
     * @param outerRadius outer radius (0–1, must be greater than innerRadius)
     */
    public void setOuterRadius(float outerRadius) {
        this.outerRadius = outerRadius;
        if (material != null) {
            material.setFloat("OuterRadius", outerRadius);
        }
    }

    /**
     * Returns the intensity of the vignette.
     *
     * @return intensity (0–1)
     */
    public float getIntensity() {
        return intensity;
    }

    /**
     * Sets the blend intensity: how strongly the vignette color is applied at
     * the outer edge.
     *
     * @param intensity blend factor (0 = invisible, 1 = fully opaque)
     */
    public void setIntensity(float intensity) {
        this.intensity = intensity;
        if (material != null) {
            material.setFloat("Intensity", intensity);
        }
    }

    /**
     * Returns the vignette overlay color.
     *
     * @return the color (not null)
     */
    public ColorRGBA getColor() {
        return color;
    }

    /**
     * Sets the vignette overlay color. Default is opaque black.
     *
     * @param color the desired color (not null)
     */
    public void setColor(ColorRGBA color) {
        this.color = color.clone();
        if (material != null) {
            material.setColor("Color", this.color);
        }
    }

    // -------------------------------------------------------------------------
    // Serialization
    // -------------------------------------------------------------------------

    @Override
    public void read(JmeImporter importer) throws IOException {
        super.read(importer);
        InputCapsule capsule = importer.getCapsule(this);
        innerRadius = capsule.readFloat("innerRadius", 0.3f);
        outerRadius = capsule.readFloat("outerRadius", 0.75f);
        intensity   = capsule.readFloat("intensity",   0.8f);
        color = (ColorRGBA) capsule.readSavable("color", ColorRGBA.Black.clone());
    }

    @Override
    public void write(JmeExporter exporter) throws IOException {
        super.write(exporter);
        OutputCapsule capsule = exporter.getCapsule(this);
        capsule.write(innerRadius, "innerRadius", 0.3f);
        capsule.write(outerRadius, "outerRadius", 0.75f);
        capsule.write(intensity,   "intensity",   0.8f);
        capsule.write(color,       "color",       ColorRGBA.Black.clone());
    }
}
