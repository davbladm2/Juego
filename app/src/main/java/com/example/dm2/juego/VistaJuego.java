package com.example.dm2.juego;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class VistaJuego extends SurfaceView {

    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    private List<Sprite> sprites=new ArrayList<Sprite>();
    private List<TempSprite> temps=new ArrayList<TempSprite>();
    private long lastClick;
    private Bitmap bmpBlood;

    public VistaJuego(Context context) {
        super(context);
        gameLoopThread = new GameLoopThread(this);
        getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                crearSprites();
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
        bmpBlood=BitmapFactory.decodeResource(getResources(),R.drawable.estallar);

    }

    private void crearSprites(){
        sprites.add(crearSprite(R.drawable.descargas));
        sprites.add(crearSprite(R.drawable.orco));
        sprites.add(crearSprite(R.drawable.orco));
        sprites.add(crearSprite(R.drawable.orco));
        sprites.add(crearSprite(R.drawable.orco));
        sprites.add(crearSprite(R.drawable.orco));
        sprites.add(crearSprite(R.drawable.orco));
        sprites.add(crearSprite(R.drawable.esqueleto));
        sprites.add(crearSprite(R.drawable.esqueleto));
        sprites.add(crearSprite(R.drawable.esqueleto));
        sprites.add(crearSprite(R.drawable.esqueleto));
        sprites.add(crearSprite(R.drawable.esqueleto));
        sprites.add(crearSprite(R.drawable.esqueleto));
    }

    private Sprite crearSprite(int resouce){
        Bitmap bmp=BitmapFactory.decodeResource(getResources(),resouce);
        return new Sprite(this,bmp);
    }
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(Color.DKGRAY);
       for(Sprite sprite:sprites) {
           sprite.onDraw(canvas);
       }
        for(int i=temps.size()-1;i>=0;i--){
            temps.get(i).onDraw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (System.currentTimeMillis() - lastClick > 300) {
            lastClick = System.currentTimeMillis();
            float x=event.getX();
            float y=event.getY();
            synchronized (getHolder()) {
                for (int i = sprites.size() - 1; i >= 0; i--) {
                    Sprite sprite = sprites.get(i);
                    if (sprite.isCollition(x,y)) {
                        if(sprite==sprites.get(0)){
                            gameLoopThread.setRunning(false);
                            Toast.makeText(this.getContext(),"SE TERMINÓ EL JUEGO", Toast.LENGTH_LONG).show();
                        }else{
                            sprites.remove(sprite);
                            temps.add(new TempSprite(temps,this,x,y,bmpBlood));
                        }
                        break;
                    }
                }
            }
        }
        return true;
    }
}


