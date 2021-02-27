
uniform vec2 u_size;
uniform sampler2D velTex;
uniform sampler2D mainTex;

vec2 velTex_normalization(vec2 pos){
    return (texture(velTex, pos / u_size).xy -0.5) * 2.0;
}

void main()
{
    vec2 vel = velTex_normalization(gl_FragCoord.xy);
    vec4 cal = texture(mainTex, vec2(gl_FragCoord.xy / u_size - vel*0.9));
    cal.g = texture(mainTex, vec2(gl_FragCoord.xy / u_size - vel*0.8)).g;
    cal.b = texture(mainTex, vec2(gl_FragCoord.xy / u_size - vel*0.7)).b;
    gl_FragColor = vec4(cal.xyz,cal.a * 0.95);
}