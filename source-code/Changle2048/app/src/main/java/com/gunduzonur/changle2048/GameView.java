package com.gunduzonur.changle2048;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

public class GameView extends GridLayout {
    private Box[][] map = new Box[4][4];
    private List<Point> free = new ArrayList<Point>();

    public GameView(Context context) {
        super(context);
        start();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        start();
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        start();
    }

    public void start() {
        setColumnCount(4);
        setBackgroundColor(Color.parseColor("#006994"));

        setOnTouchListener(new View.OnTouchListener() {

            private float startX, startY;
            private float dx, dy;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        dx = event.getX() - startX;
                        dy = event.getY() - startY;

                        if (Math.abs(dx) > Math.abs(dy)) {
                            if (dx < -5) {
                                left();
                            } else if (dx > 5) {
                                right();
                            }
                        } else {
                            if (dy < -5) {
                                up();
                            } else if (dy > 5) {
                                down();
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int boxW = (Math.min(w, h) - 10) / 4;

        spawnBoxes(boxW, boxW);

        resume();
    }

    private void spawnBoxes(int boxW, int boxH) {
        Box box;

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                box = new Box(getContext());
                box.setValue(2);
                addView(box, boxW, boxH);

                map[x][y] = box;
            }
        }
    }

    private void resume() {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                map[x][y].setValue(0);
            }
        }

        spawnRandomValue(2);
    }

    private void spawnRandomValue(int n) {
        for (int i = 0; i < n; i++) {
            free.clear();

            for (int x = 0; x < 4; x++) {
                for (int y = 0; y < 4; y++) {
                    if (map[x][y].getValue() < 2) {
                        free.add(new Point(x, y));
                    }
                }
            }

            Point randomFreePoint = free.remove((int) (Math.random() * free.size()));

            map[randomFreePoint.x][randomFreePoint.y].setValue(Math.random() > 0.2 ? 2 : 4); // 20% chance of spawning a 4
        }
    }

    private void left() {
        Log.i("swipe", "left");

        boolean merge = false;

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                for (int xx = x + 1; xx < 4; xx++) {
                    if (map[xx][y].getValue() > 0) {
                        if (map[x][y].getValue() < 2) {
                            map[x][y].setValue(map[xx][y].getValue());
                            map[xx][y].setValue(0);
                            x--;
                            merge = true;
                        } else if (map[x][y].equals(map[xx][y])) {
                            map[x][y].setValue(map[x][y].getValue() * 2);
                            map[xx][y].setValue(0);
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }

        if (merge) {
            spawnRandomValue(1);
            winCheck();
        }
    }

    private void right() {
        Log.i("swipe", "right");

        boolean merge = false;

        for (int x = 3; x >= 0; x--) {
            for (int y = 0; y < 4; y++) {
                for (int xx = x + 1; xx < 4; xx++) {
                    if (map[xx][y].getValue() > 0) {
                        if (map[x][y].getValue() < 2) {
                            map[x][y].setValue(map[xx][y].getValue());
                            map[xx][y].setValue(0);
                            x--;
                            merge = true;
                        } else if (map[x][y].equals(map[xx][y])) {
                            map[x][y].setValue(map[x][y].getValue() * 2);
                            map[xx][y].setValue(0);
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }
        if (merge) {
            spawnRandomValue(1);
            winCheck();
        }
    }

    private void up() {
        Log.i("swipe", "up");

        boolean merge = false;

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                for (int yy = y - 1; yy >= 0; yy--) {
                    if (map[x][yy].getValue() > 0) {
                        if (map[x][y].getValue() < 2) {
                            map[x][y].setValue(map[x][yy].getValue());
                            map[x][yy].setValue(0);
                            y++;
                            merge = true;
                        } else if (map[x][y].equals(map[x][yy])) {
                            map[x][y].setValue(map[x][y].getValue() * 2);
                            map[x][yy].setValue(0);
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }
        if (merge) {
            spawnRandomValue(1);
            winCheck();
        }
    }

    private void down() {
        Log.i("swipe", "down");

        boolean merge = false;

        for (int x = 0; x < 4; x++) {
            for (int y = 3; y >= 0; y--) {
                for (int yy = y - 1; yy >= 0; yy--) {
                    if (map[x][yy].getValue() > 0) {
                        if (map[x][y].getValue() < 2) {
                            map[x][y].setValue(map[x][yy].getValue());
                            map[x][yy].setValue(0);
                            y++;
                            merge = true;
                        } else if (map[x][y].equals(map[x][yy])) {
                            map[x][y].setValue(map[x][y].getValue() * 2);
                            map[x][yy].setValue(0);
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }
        if (merge) {
            spawnRandomValue(1);
            winCheck();
        }
    }

    private void winCheck() {
        boolean done = true;

        ALL:
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if (map[x][y].getValue() == 0 ||
                        (x > 0 && map[x][y].equals(map[x - 1][y])) ||
                        (x < 3 && map[x][y].equals(map[x + 1][y])) ||
                        (y > 0 && map[x][y].equals(map[x][y - 1])) ||
                        (y < 3 && map[x][y].equals(map[x][y + 1]))) {
                    done = false;
                    break ALL;
                }
            }
        }

        if (done) {
            new AlertDialog.Builder(getContext()).setTitle("Done").setMessage("You're done kid").setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    resume();
                }
            }).show();
        }
    }
}

