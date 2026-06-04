#import "Common/ShaderLib/GLSLCompat.glsllib"
#import "Common/ShaderLib/MultiSample.glsllib"

uniform COLORTEXTURE m_Texture;
varying vec2 texCoord;

// 0 = GRAYSCALE, 1 = INVERT_COLORS
uniform int m_Mode;

void main() {
    vec4 color = getColor(m_Texture, texCoord);

    if (m_Mode == 0) {
        // Grayscale: BT.601 luminance-weighted dot product
        float gray = dot(color.rgb, vec3(0.299, 0.587, 0.114));
        color.rgb = vec3(gray);
    } else if (m_Mode == 1) {
        // Invert colors
        color.rgb = vec3(1.0) - color.rgb;
    }

    gl_FragColor = color;
}
