uniform vec2 u_size;
uniform sampler2D velTex;

vec2 velTex_normalization(vec2 pos){
    return (texture(velTex, pos / u_size).xy -0.5) * 2.0;
}
void main() {
    vec2 tempvel = velTex_normalization(gl_FragCoord.xy);
    tempvel.x += (texture(velTex, vec2((gl_FragCoord.x - 1.0) / u_size.x, gl_FragCoord.y / u_size.y)).z
               - texture(velTex, vec2((gl_FragCoord.x + 1.0) / u_size.x, gl_FragCoord.y / u_size.y)).z)*0.5;
    tempvel.y += (texture(velTex, vec2((gl_FragCoord.x) / u_size.x, (gl_FragCoord.y - 1.0) / u_size.y)).z
               - texture(velTex, vec2((gl_FragCoord.x) / u_size.x, (gl_FragCoord.y + 1.0) / u_size.y)).z)*0.5;
    gl_FragColor = texture(velTex, gl_FragCoord.xy / u_size);
    tempvel *= 0.98;
    tempvel = (tempvel*0.5)+0.5;
    gl_FragColor.xy = vec2(tempvel);
}
