uniform sampler2D m_fDifMap;
uniform sampler2D m_fNorMap;
uniform sampler2D m_fParMap;
uniform sampler2D m_mDifMap;
uniform sampler2D m_mNorMap;
uniform sampler2D m_mParMap;
uniform float m_shininess;
uniform float m_scale;
uniform bool m_mapping;

varying vec2 texCoord;
varying vec3 position;
varying vec4 AmbientSum;
varying vec4 DiffuseSum;
varying vec4 SpecularSum;
varying vec3 vPosition;
varying vec3 vViewDir;
varying vec4 vLightDir;

void main() {
	vec4 fColor = texture2D(m_fDifMap, texCoord * m_scale);
	vec4 mColor = texture2D(m_mDifMap, texCoord * m_scale);
	vec4 color = fColor;

	if (m_mapping) {
		vec2 newTexCoord; // * m_scale ?!
		float h;
		h = texture2D(m_fParMap, texCoord).r;
		float heightScale = 0.05;
		float heightBias = heightScale * -0.5;
		vec3 normView = normalize(vViewDir);
		h = (h * heightScale + heightBias) * normView.z;
		newTexCoord = texCoord + (h * -normView.xy);

		vec4 normalHeight = texture2D(m_fNorMap, newTexCoord);
		vec3 normal = (normalHeight.xyz * vec3(2.0) - vec3(1.0));
		normal.y = -normal.y;

		vec4 diffuseColor = texture2D(m_fDifMap, newTexCoord);
		vec4 specularColor = vec4(1.0);

		vec4 lightDir = vLightDir;
		lightDir.xyz = normalize(lightDir.xyz);

		float diffuseFactor = max(0.0, dot(normal, lightDir.xyz));
		float specularFactor = pow(max(dot(reflect(-lightDir.xyz, normal), vViewDir.xyz), 0.0), m_shininess);
		vec2 light = vec2(diffuseFactor, specularFactor) * vec2(vLightDir.w);

		color = (AmbientSum + DiffuseSum * light.x) * diffuseColor
				+ SpecularSum * light.y * specularColor;
	}
	float mixscale = position.y - 30; 
	color = mix(color, mColor, (mixscale / 15.0));

	gl_FragColor = color;
}
