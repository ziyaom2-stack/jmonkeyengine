#import "Common/ShaderLib/GLSLCompat.glsllib"
#import "Common/ShaderLib/MultiSample.glsllib"

uniform COLORTEXTURE m_Texture;
varying vec2 texCoord;

// Distance from center at which vignette begins (0.0-1.0)
uniform float m_InnerRadius;
// Distance from center at which vignette is fully opaque (0.0-1.0)
uniform float m_OuterRadius;
// Strength of the effect (0.0 = none, 1.0 = full)
uniform float m_Intensity;
// Color of the vignette overlay (typically black)
uniform vec4 m_Color;

void main() {
    vec4 color = getColor(m_Texture, texCoord);

    // Distance from screen center
    vec2 offset = texCoord - vec2(0.5);
    float dist = length(offset);

    // Smooth ramp: 0 inside innerRadius, 1 outside outerRadius
    float vignette = smoothstep(m_InnerRadius, m_OuterRadius, dist);

    // Blend scene toward vignette color
    color.rgb = mix(color.rgb, m_Color.rgb, vignette * m_Intensity);

    gl_FragColor = color;
}
