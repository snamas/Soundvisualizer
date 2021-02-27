uniform vec2 u_size;
uniform sampler2D velTex;

vec2 velTex_normalization(vec2 pos){
    return (texture(velTex, pos / u_size).xy -0.5) * 2.0;
}

void main() {
    float tempdiv = velTex_normalization(vec2(gl_FragCoord.x + 1.0,gl_FragCoord.y)).x
                   -velTex_normalization(vec2(gl_FragCoord.x - 1.0,gl_FragCoord.y)).x
                   +velTex_normalization(vec2(gl_FragCoord.x,gl_FragCoord.y + 1.0)).y
                   -velTex_normalization(vec2(gl_FragCoord.x,gl_FragCoord.y - 1.0)).y;
    float temppre = (texture(velTex, vec2((gl_FragCoord.x + 1.) / u_size.x, gl_FragCoord.y / u_size.y)).z
                   + texture(velTex, vec2((gl_FragCoord.x - 1.) / u_size.x, gl_FragCoord.y / u_size.y)).z
                   + texture(velTex, vec2((gl_FragCoord.x) / u_size.x, (gl_FragCoord.y + 1.) / u_size.y)).z
                   + texture(velTex, vec2((gl_FragCoord.x) / u_size.x, (gl_FragCoord.y - 1.) / u_size.y)).z
                   - tempdiv)*0.25;
    gl_FragColor = texture(velTex, gl_FragCoord.xy / u_size);
    gl_FragColor.z = temppre;
}