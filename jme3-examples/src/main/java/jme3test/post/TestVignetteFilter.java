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
package jme3test.post;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.VignetteFilter;
import com.jme3.scene.Spatial;

/**
 * Demonstrates {@link VignetteFilter}.
 *
 * <p>Controls:
 * <ul>
 *   <li>V – toggle vignette on/off</li>
 *   <li>[ – decrease intensity</li>
 *   <li>] – increase intensity</li>
 *   <li>R – reset to defaults</li>
 * </ul>
 */
public class TestVignetteFilter extends SimpleApplication {

    private VignetteFilter vignetteFilter;
    private boolean enabled = true;

    public static void main(String[] args) {
        TestVignetteFilter app = new TestVignetteFilter();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        // Load a scene to demonstrate the effect
        Spatial scene = assetManager.loadModel("Models/Town/main.scene");
        rootNode.attachChild(scene);

        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.5f, -1f, -0.5f).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);

        cam.setLocation(new Vector3f(0, 5, 20));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);

        // Set up vignette filter
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        vignetteFilter = new VignetteFilter(0.3f, 0.75f, 0.8f);
        fpp.addFilter(vignetteFilter);
        viewPort.addProcessor(fpp);

        // Key bindings
        inputManager.addMapping("Toggle",        new KeyTrigger(KeyInput.KEY_V));
        inputManager.addMapping("IntensityDown",  new KeyTrigger(KeyInput.KEY_LBRACKET));
        inputManager.addMapping("IntensityUp",    new KeyTrigger(KeyInput.KEY_RBRACKET));
        inputManager.addMapping("Reset",          new KeyTrigger(KeyInput.KEY_R));

        ActionListener listener = (name, isPressed, tpf) -> {
            if (!isPressed) return;
            switch (name) {
                case "Toggle":
                    enabled = !enabled;
                    vignetteFilter.setEnabled(enabled);
                    System.out.println("Vignette: " + (enabled ? "ON" : "OFF"));
                    break;
                case "IntensityDown":
                    vignetteFilter.setIntensity(
                            Math.max(0f, vignetteFilter.getIntensity() - 0.1f));
                    System.out.printf("Intensity: %.1f%n", vignetteFilter.getIntensity());
                    break;
                case "IntensityUp":
                    vignetteFilter.setIntensity(
                            Math.min(1f, vignetteFilter.getIntensity() + 0.1f));
                    System.out.printf("Intensity: %.1f%n", vignetteFilter.getIntensity());
                    break;
                case "Reset":
                    vignetteFilter.setInnerRadius(0.3f);
                    vignetteFilter.setOuterRadius(0.75f);
                    vignetteFilter.setIntensity(0.8f);
                    vignetteFilter.setColor(ColorRGBA.Black);
                    System.out.println("Vignette reset to defaults.");
                    break;
            }
        };
        inputManager.addListener(listener,
                "Toggle", "IntensityDown", "IntensityUp", "Reset");

        System.out.println("VignetteFilter demo loaded.");
        System.out.println("  V = toggle on/off");
        System.out.println("  [ / ] = decrease / increase intensity");
        System.out.println("  R = reset to defaults");
    }
}
