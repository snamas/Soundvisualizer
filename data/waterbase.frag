
uniform vec2 u_size;
uniform sampler2D CurrentTex;
uniform sampler2D ExTex;
void main() {
    float exCol = texture(ExTex, vec2(gl_FragCoord.xy / u_size)).r;
    float currentCol = texture(CurrentTex, vec2(gl_FragCoord.xy / u_size)).r;
    gl_FragColor = vec4(2.0*currentCol - exCol + 0.25*(texture(CurrentTex, vec2((gl_FragCoord.x+3.0) / u_size.x,gl_FragCoord.y / u_size.y)) +
                                                 texture(CurrentTex, vec2((gl_FragCoord.x-3.0) / u_size.x,gl_FragCoord.y / u_size.y)) +
                                                 texture(CurrentTex, vec2(gl_FragCoord.x / u_size.x,(gl_FragCoord.y + 3.0) / u_size.y)) +
                                                 texture(CurrentTex, vec2(gl_FragCoord.x / u_size.x,(gl_FragCoord.y - 3.0) / u_size.y)) -
                                                 4.0*currentCol).r,0,0,1);
}
