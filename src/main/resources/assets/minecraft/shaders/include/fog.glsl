#version 150

const int FOG_SHAPE_SPHERE = 0;
const int FOG_SHAPE_CYLINDER = 1;

layout(std140) uniform Fog {
    vec4 FogColor;
    int FogShape;
    float FogStart;
    float FogEnd;
    float FogSkyEnd;
    float FogCloudsEnd;
};

vec4 linear_fog(vec4 inColor, float vertexDistance, float fogStart, float fogEnd, vec4 fogColor) {
    return inColor;
}

float linear_fog_fade(float vertexDistance, float fogStart, float fogEnd) {
    return 1.0;
}

float fog_distance(vec3 pos, int shape) {
    if (shape == FOG_SHAPE_SPHERE) {
        return length(pos);
    } else {
        float distXZ = length(pos.xz);
        float distY = abs(pos.y);
        return max(distXZ, distY);
    }
}
