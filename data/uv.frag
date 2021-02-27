uniform vec2 u_size;
void main() {
    gl_FragColor = vec4(1.0);
    gl_FragColor.xy = gl_FragCoord.xy/u_size;
    if(length(gl_FragCoord.xy/u_size-0.5)>0.5){
        gl_FragColor.xy = vec2(0.5);
    }
    gl_FragColor.z = 0.0;
}
