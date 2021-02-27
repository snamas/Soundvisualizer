
uniform vec2 u_size;
uniform sampler2D mainTex;
void main() {
    vec2 heightvec = vec2(texture(mainTex, vec2((gl_FragCoord.x+1.0) / u_size.x,gl_FragCoord.y / u_size.y)).r-
                          texture(mainTex, vec2(gl_FragCoord.xy / u_size)).r,
                          texture(mainTex, vec2(gl_FragCoord.x / u_size.x,(gl_FragCoord.y + 1.0) / u_size.y)).r-
                          texture(mainTex, vec2(gl_FragCoord.xy / u_size)).r);
    vec3 normal = normalize(cross(vec3(1.0/u_size.x,0.0,heightvec.x),vec3(0.0,1.0/u_size.y,heightvec.y)));
    vec3 light = normalize(vec3(1.0,1.0,1.0));
    float NdotL = clamp(dot(normal,light),0.0,1.0);
    gl_FragColor = vec4(1.0);
    gl_FragColor.xyz = mix(vec3(0.3,0.35,0.4),vec3(0.7,0.8,0.95),NdotL);
}
