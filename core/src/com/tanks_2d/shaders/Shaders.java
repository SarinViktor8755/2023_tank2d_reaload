package com.tanks_2d.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Shaders {

    private float timer;
    private SpriteBatch spriteBatch;
    private ShaderProgram shader;

    public Shaders(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
        ShaderProgram.pedantic = false;
//Загрузка из файлов
        shader = new ShaderProgram(Gdx.files.internal("shaders/default.vert"), Gdx.files.internal("shaders/default.frag"));
//Загрузка из строк vertexShader и fragmentShader это String в котором хранится код шейдеров

        if (!shader.isCompiled()) {
            System.err.println(shader.getLog());
            System.exit(0);
        }

        this.spriteBatch.setShader(shader);
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public static String nigFR = "//#ifdef позволяет коду работать на слабых телефонах, и мощных пк.Если шейдер используется на телефоне(GL_ES) то  \n" +
            "//используется низкая разрядность (точность) данных.(highp – высокая точность; mediump – средняя точность; lowp – низкая точность)\n" +
            "#ifdef GL_ES   \n" +
            "    #define LOWP lowp\n" +
            "    precision mediump float;\n" +
            "#else\n" +
            "    #define LOWP\n" +
            "#endif\n" +
            "varying LOWP vec4 v_color;\n" +
            "varying vec2 v_texCoords;\n" +
            "// sampler2D это специальный формат данных в  glsl для доступа к текстуре\n" +
            "uniform sampler2D u_texture;\n" +
            "void main(){\n" +
            "    gl_FragColor = v_color * texture2D(u_texture, v_texCoords);// итоговый цвет пикселя\n" +
            "}";

    public static String nigVR = "attribute vec4 a_position; //позиция вершины\n" +
            "attribute vec4 a_color; //цвет вершины\n" +
            "attribute vec2 a_texCoord0; //координаты текстуры\n" +
            "uniform mat4 u_projTrans;  //матрица, которая содержим данные для преобразования проекции и вида\n" +
            "varying vec4 v_color;  //цвет который будет передан в фрагментный шейдер\n" +
            "varying vec2 v_texCoords;  //координаты текстуры\n" +
            "void main(){\n" +
            "    v_color=a_color;\n" +
            "    // При передаче цвет из SpriteBatch в шейдер, происходит преобразование из ABGR int цвета в float. \n" +
            "    // что-бы избежать NAN  при преобразование, доступен не весь диапазон для альфы, а только значения от (0-254)\n" +
            "    //чтобы полностью передать непрозрачность цвета, когда альфа во float равна 1, то всю альфу приходится умножать.\n" +
            "    //это специфика libgdx и о ней надо помнить при переопределение  вершинного шейдера.\n" +
            "    v_color.a = v_color.a * (255.0/254.0);\n" +
            "    v_texCoords = a_texCoord0;\n" +
            "    //применяем преобразование вида и проекции, можно не забивать себе этим голову\n" +
            "    // тут происходят математические преобразование что-бы правильно учесть параметры камеры\n" +
            "    // gl_Position это окончательная позиция вершины \n" +
            "    gl_Position =  u_projTrans * a_position; \n" +
            "}";


    public ShaderProgram getShader() {
        return shader;
    }

    public void updateShader() {
        
        getShader().setUniformf("colors", timer);
        timer += Gdx.graphics.getDeltaTime();
       // timer = (float) MathUtils.clamp(timer, -0.5, 0);
        if(timer > 0) timer = 0;
       // if(timer < -3) timer = -3;
     //   System.out.println(timer);
    }

    public void minus(float p3) {
        this.timer =this.timer -(p3/50);
        if(this.timer < -1.5) this.timer = -1.5f;
    }

}
