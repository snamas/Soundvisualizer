package Processing;

import controlP5.ControlP5;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.event.MouseEvent;
import processing.opengl.PShader;
import processing.sound.Amplitude;
import processing.sound.*;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;


public class Main extends PApplet {
    PApplet core = this;
    DropTarget dropTarget;
    Component component;
    ControlP5 cp5;
    ArrayList<coustomSoundClass> soundClassArrayList = new ArrayList<>();
    coustomSoundClass SliderActivatedcoustomSoundClass;
    Amplitude amp;
    int sppedValue;
    int lowPassValue;
    int reburbdampValue;
    float knobRadius = 40;
    float dia;
    boolean toggleValue;
    Runnable runnable;
    boolean runnableActive;

    class coustomSoundClass {
        SoundFile soundFile;
        Amplitude amp;
        String name;
        float dia = 0;
        int color;
        int speed = 100;
        LowPass lowPass;
        Reverb reverb;
        int lowPassFreq = 22050;
        float reburbdamp = 0;

        public coustomSoundClass(SoundFile soundFile, Amplitude amp, String name, int color) {
            this.soundFile = soundFile;
            this.soundFile.loop();
            this.reverb = new Reverb(core);
            this.reverb.process(this.soundFile);
            this.lowPass = new LowPass(core);
            this.lowPass.process(this.soundFile);
            this.amp = amp;
            this.name = name;
            this.color = color;
            cp5.get("sppedValue").setValue(100);
            cp5.get("lowPassValue").setValue(22050);
            cp5.get("reburbdampValue").setValue(0);
            SliderActivatedcoustomSoundClass = this;
        }
    }

    void dragDropFile() {
        component = (Component) this.surface.getNative();

        dropTarget = new DropTarget(component, new DropTargetListener() {
            public void dragEnter(DropTargetDragEvent dtde) {
            }

            public void dragOver(DropTargetDragEvent dtde) {
            }

            public void dropActionChanged(DropTargetDragEvent dtde) {
            }

            public void dragExit(DropTargetEvent dte) {
            }

            public void drop(DropTargetDropEvent dtde) {
                try {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                    Transferable trans = dtde.getTransferable();
                    List<File> fileNameList = null;
                    if (trans.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        try {
                            fileNameList = (List<File>)
                                    trans.getTransferData(DataFlavor.javaFileListFlavor);
                        } catch (UnsupportedFlavorException | IOException ex) {
                            println(ex);
                        }
                    }
                    if (fileNameList == null || fileNameList.isEmpty()) return;

                    for (File f : fileNameList) {
                        runnable = () ->{
                            String name = f.getAbsolutePath();
                            SoundFile soundFile = new SoundFile(core, name);
                            Amplitude amp = new Amplitude(core);
                            amp.input(soundFile);
                            colorMode(HSB, 360, 100, 100 );
                            int color = color(random(0, 360), 100, 100);
                            colorMode(RGB);
                            cp5.addKnob(name)
                                    .setRange(0, 100)
                                    .setValue(120)
                                    .setPosition(width/2f+random(-10,10), height/2f+random(-100,100))
                                    .setSize(50, 50)
                                    .setRadius(knobRadius)
                                    .setColorBackground(color)
                                    .setNumberOfTickMarks(50);
                            soundClassArrayList.add(new coustomSoundClass(soundFile, amp, name, color));
                            println(name);
                            runnableActive = false;
                        };
                        runnableActive = true;
                    }
                } catch (ConcurrentModificationException e) {
                    System.out.println("えらった");
                }
            }
        }
        );
    }

    @Override
    public void settings() {
        size(800, 800);

    }

    @Override
    public void setup() {
        background(200);
        String[] args = {"SecondApplet"};
        SecondApp sa = new SecondApp();
        PApplet.runSketch(args, sa);
        dragDropFile();
        cp5 = new ControlP5(this);
        cp5.addToggle("toggleValue")
                .setPosition(0, 0)
                .setValue(true)
                .setSize(40, 20);

        cp5.addSlider("sppedValue")
                .setLabel("Speed")
                .setRange(0, 200)//0~100の間
                .setValue(99)//初期値
                .setPosition(50, 40)//位置
                .setSize(200, 20)
                .setNumberOfTickMarks(100);//大きさ
        cp5.addSlider("lowPassValue")
                .setLabel("lowPass")
                .setRange(0, 22050)//0~100の間
                .setValue(22050)//初期値
                .setPosition(50, 80)//位置
                .setSize(200, 20)
                .setNumberOfTickMarks(100);//大きさ
        cp5.addSlider("reburbdampValue")
                .setLabel("reverb")
                .setRange(0, 100)//0~100の間
                .setValue(0)//初期値
                .setPosition(50, 120)//位置
                .setSize(200, 20)
                .setNumberOfTickMarks(100);//大きさ
    }

    @Override
    public void draw() {
        background(200);
        int a = 1;
        for (coustomSoundClass e : soundClassArrayList) {
            e.soundFile.amp(cp5.get(e.name).getValue() / 100f);
            e.dia = map(e.amp.analyze(), 0f, 1f, 0f, 100);
            e.soundFile.pan((cp5.get(e.name).getPosition()[0]/width)*2f-1f);
        }
        if(SliderActivatedcoustomSoundClass != null){
            SliderActivatedcoustomSoundClass.soundFile.rate(sppedValue /100f);
            SliderActivatedcoustomSoundClass.speed = sppedValue;
            SliderActivatedcoustomSoundClass.lowPass.freq(lowPassValue);
            SliderActivatedcoustomSoundClass.lowPassFreq = lowPassValue;
            SliderActivatedcoustomSoundClass.reverb.damp(reburbdampValue/100f);
            SliderActivatedcoustomSoundClass.reburbdamp = reburbdampValue;
        }
        if(runnableActive){
            runnable.run();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        for (coustomSoundClass csc : soundClassArrayList) {
            if (cp5.get(csc.name).isMouseOver() && mouseButton == RIGHT) {
                cp5.get(csc.name).setPosition(mouseX - 25, mouseY - 25);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        for (coustomSoundClass csc : soundClassArrayList) {
            if (cp5.get(csc.name).isMouseOver() && mouseButton == CENTER) {
                if (csc.soundFile.isPlaying()) {
                    csc.soundFile.pause();
                    cp5.get(csc.name).setColorBackground(color(255, 0, 0));
                } else {
                    csc.soundFile.loop();
                    cp5.get(csc.name).setColorBackground(csc.color);
                }
            }else if(cp5.get(csc.name).isMouseOver() && mouseButton == LEFT){
                cp5.get("sppedValue").setValue(csc.speed);
                cp5.get("lowPassValue").setValue(csc.lowPassFreq);
                cp5.get("reburbdampValue").setValue(csc.reburbdamp);
                SliderActivatedcoustomSoundClass = csc;
            }
        }

    }

    public static void main(String[] args) {
        // write your code here
        PApplet.main("Processing.Main");
    }

    public class SecondApp extends PApplet {
        PGraphics pgExTex;
        PGraphics pgCurrentTex;
        PShader waterbase;
        PShader watershader;
        PGraphics pghidden;
        PShader colshader;
        PShader pulshader;
        PShader velshader;
        PGraphics pghide;
        PGraphics pgvel;
        PGraphics pgUV;

        @Override
        public void settings() {
            size(800, 800, P2D);
        }

        @Override
        public void setup() {
            background(255);
            waterbase = loadShader("waterbase.frag");
            watershader = loadShader("water.frag");
            pgExTex = createGraphics(width, height, P2D);
            pgCurrentTex = createGraphics(width, height, P2D);
            pghidden = createGraphics(width, height, P2D);
            pgExTex.beginDraw();
            pgExTex.background(0);
            pgExTex.endDraw();
            pgCurrentTex.beginDraw();
            pgCurrentTex.background(0);
            pgCurrentTex.endDraw();
            waterbase.set("u_size", (float) width, (float) height);
            waterbase.set("ExTex", pgExTex);
            waterbase.set("CurrentTex", pgCurrentTex);
            watershader.set("u_size", (float) width, (float) height);

            pghide = createGraphics(width, height, P2D);
            pgvel = createGraphics(width, height, P2D);
            pgvel.beginDraw();
            pgvel.background(128, 128, 0);
            pgvel.endDraw();
            colshader = loadShader("ps.frag");
            colshader.set("u_size", (float) width, (float) height);
            colshader.set("mainTex", get());
            colshader.set("velTex", pgvel);
            pulshader = loadShader("pre.frag");
            pulshader.set("u_size", (float) width, (float) height);
            pulshader.set("velTex", pgvel);
            velshader = loadShader("vel.frag");
            velshader.set("u_size", (float) width, (float) height);
            velshader.set("velTex", pgvel);
            PShader uvshader = loadShader("uv.frag");
            uvshader.set("u_size", (float) width, (float) height);
            pgUV = createGraphics(width, height, P2D);
            pgUV.beginDraw();
            pgUV.shader(uvshader);
            pgUV.rect(0, 0, width, height);
            pgUV.resetShader();
            pgUV.endDraw();
        }

        @Override
        public void draw() {
            if (toggleValue) {
                noStroke();
                for (coustomSoundClass e : soundClassArrayList) {
                    fill(e.color-40);
                    ellipse(cp5.get(e.name).getPosition()[0], cp5.get(e.name).getPosition()[1], e.dia * 4, e.dia * 4);
                    fill(e.color);
                    ellipse(cp5.get(e.name).getPosition()[0], cp5.get(e.name).getPosition()[1], e.dia * 3, e.dia * 3);
                    pgvel.beginDraw();
                    pgvel.noStroke();
                    pgvel.tint(255, 255, 0, 255);
                    pgvel.image(pgUV, cp5.get(e.name).getPosition()[0] - e.dia * 1f, cp5.get(e.name).getPosition()[1] - e.dia * 1f, e.dia * 2f, e.dia * 2f);
                    pgvel.noTint();
                    pgvel.endDraw();
                }

                for (int i = 0; i < 10; i++) {
                    pgvel.loadPixels();
                    pulshader.set("velTex", pgvel);
                    pgvel.beginDraw();
                    pgvel.shader(pulshader);
                    pgvel.rect(0, 0, width, height);
                    pgvel.resetShader();
                    pgvel.endDraw();
                }

                pgvel.loadPixels();
                velshader.set("velTex", pgvel);
                pgvel.beginDraw();
                pgvel.shader(velshader);
                pgvel.rect(0, 0, width, height);
                pgvel.resetShader();
                pgvel.endDraw();
                pgvel.loadPixels();
                loadPixels();
                pghide.beginDraw();
                pghide.image(this.get(), 0, 0);
                pghide.endDraw();
                colshader.set("mainTex", pghide);
                colshader.set("velTex", pgvel);
                shader(colshader);
                rect(0, 0, width, height);
                resetShader();

            } else {
                pgCurrentTex.beginDraw();
                pgCurrentTex.noStroke();
                pgCurrentTex.fill(255, 0, 0);
                for (coustomSoundClass e : soundClassArrayList) {
                    pgCurrentTex.ellipse(cp5.get(e.name).getPosition()[0], cp5.get(e.name).getPosition()[1], e.dia, e.dia);
                }
                pgCurrentTex.endDraw();
                pgExTex.loadPixels();
                pgCurrentTex.loadPixels();
                waterbase.set("ExTex", pgExTex);
                waterbase.set("CurrentTex", pgCurrentTex);
                pghidden.beginDraw();
                pghidden.shader(waterbase);
                pghidden.rect(0, 0, width, height);
                pghidden.resetShader();
                pghidden.endDraw();

                watershader.set("mainTex", pghidden);
                shader(watershader);
                rect(0, 0, width, height);
                resetShader();
                pghidden.loadPixels();
                pgExTex.beginDraw();
                pgExTex.image(pgCurrentTex.copy(), 0, 0);
                pgExTex.endDraw();
                pgCurrentTex.beginDraw();
                pgCurrentTex.image(pghidden.copy(), 0, 0);
                pgCurrentTex.endDraw();
            }
        }
    }
}