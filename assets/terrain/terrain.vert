uniform mat4 g_WorldViewProjectionMatrix;
uniform mat4 g_WorldViewMatrix;
uniform mat3 g_NormalMatrix;
uniform mat4 g_ViewMatrix;
uniform vec4 g_LightColor;
uniform vec4 g_LightPosition;
uniform bool m_mapping;

attribute vec3 inPosition;
attribute vec2 inTexCoord;
attribute vec3 inNormal;
attribute vec3 inTangent;

varying vec2 texCoord;
varying vec3 position;
varying vec4 AmbientSum;
varying vec4 DiffuseSum;
varying vec4 SpecularSum;
varying vec3 vPosition;
varying vec3 vViewDir;
varying vec4 vLightDir;

void main() {
	gl_Position = g_WorldViewProjectionMatrix * vec4(inPosition, 1.0);
	texCoord = inTexCoord;
	position = inPosition;

	if (m_mapping) {
		vec3 wvPosition = (g_WorldViewMatrix * vec4(inPosition, 1.0)).xyz;
		vec3 wvNormal = normalize(g_NormalMatrix * inNormal);
		vec3 viewDir = normalize(-wvPosition);
		vec4 wvLightPos = (g_ViewMatrix * vec4(g_LightPosition.xyz, g_LightColor.w));
		wvLightPos.w = g_LightPosition.w;
		vec4 lightColor = g_LightColor;

		vec3 wvTangent = normalize(g_NormalMatrix * inTangent);
		vec3 wvBinormal = cross(wvNormal, wvTangent);
		mat3 tbnMat = mat3(wvTangent, wvBinormal, wvNormal);

		vPosition = wvPosition * tbnMat;
		vViewDir = viewDir * tbnMat;

		float posLight = step(0.5, lightColor.w);
		vec3 tempVec = wvLightPos.xyz * sign(posLight - 0.5) - (wvPosition * posLight);
		vLightDir = vec4(normalize(tempVec), 1.0);
		vLightDir.xyz = (vLightDir.xyz * tbnMat).xyz;

		AmbientSum = vec4(0.0);
		DiffuseSum = lightColor;
		SpecularSum = lightColor;
	}
}
