package aaextras.rizzo.gabriele;/*
 * Copyright (C) 2015. Jared Rummler <jared.rummler@gmail.com>
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import android.animation.TimeAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.Random;

public class Nyandroid extends Activity {

  public static class Board extends FrameLayout {

    private static final Random RANDOM = new Random();

    static float lerp(float a, float b, float f) {
      return (b - a) * f + a;
    }

    static float randfrange(float a, float b) {
      return lerp(a, b, RANDOM.nextFloat());
    }

    public class FlyingCat extends ImageView {

      public static final float VMAX = 1000.0f;
      public static final float VMIN = 100.0f;

      public float v;

      public float dist;
      public float z;

      public FlyingCat(Context context, AttributeSet as) {
        super(context, as);
        setImageResource(R.drawable.nyandroid_anim); // @@@
      }

      public void reset() {
        final float scale = lerp(0.1f, 2f, z);
        setScaleX(scale);
        setScaleY(scale);

        setX(-scale * getWidth() + 1);
        setY(randfrange(0, Board.this.getHeight() - scale * getHeight()));
        v = lerp(VMIN, VMAX, z);

        dist = 0;
      }

      public void update(float dt) {
        dist += v * dt;
        setX(getX() + v * dt);
      }
    }

    TimeAnimator mAnim;

    public Board(Context context, AttributeSet as) {
      super(context, as);

      setLayerType(View.LAYER_TYPE_HARDWARE, null);
      setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
      setBackgroundColor(0xFF003366);
    }

    @SuppressLint("NewApi")
    private void reset() {
      removeAllViews();

      final ViewGroup.LayoutParams wrap = new ViewGroup.LayoutParams(
          ViewGroup.LayoutParams.WRAP_CONTENT,
          ViewGroup.LayoutParams.WRAP_CONTENT);

      for (int i = 0; i < 20; i++) {
        ImageView fixedStar = new ImageView(getContext(), null);
        fixedStar.setImageResource(R.drawable.star_anim); // @@@
        addView(fixedStar, wrap);
        final float scale = randfrange(0.1f, 1f);
        fixedStar.setScaleX(scale);
        fixedStar.setScaleY(scale);
        fixedStar.setX(randfrange(0, getWidth()));
        fixedStar.setY(randfrange(0, getHeight()));
        final AnimationDrawable anim = (AnimationDrawable) fixedStar.getDrawable();
        postDelayed(new Runnable() {

          public void run() {
            anim.start();
          }
        }, (int) randfrange(0, 1000));
      }

      for (int i = 0; i < 20; i++) {
        FlyingCat nv = new FlyingCat(getContext(), null);
        addView(nv, wrap);
        nv.z = ((float) i / 20);
        nv.z *= nv.z;
        nv.reset();
        nv.setX(randfrange(0, Board.this.getWidth()));
        final AnimationDrawable anim = (AnimationDrawable) nv.getDrawable();
        postDelayed(new Runnable() {

          public void run() {
            anim.start();
          }
        }, (int) randfrange(0, 1000));
      }

      if (mAnim != null) {
        mAnim.cancel();
      }
      mAnim = new TimeAnimator();
      mAnim.setTimeListener(new TimeAnimator.TimeListener() {

        @Override public void onTimeUpdate(TimeAnimator animation, long totalTime, long deltaTime) {

          for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            if (!(v instanceof FlyingCat)) continue;
            FlyingCat nv = (FlyingCat) v;
            nv.update(deltaTime / 1000f);
            final float catWidth = nv.getWidth() * nv.getScaleX();
            final float catHeight = nv.getHeight() * nv.getScaleY();
            if (nv.getX() + catWidth < -2
                || nv.getX() > getWidth() + 2
                || nv.getY() + catHeight < -2
                || nv.getY() > getHeight() + 2) {
              nv.reset();
            }
          }
        }
      });
    }

    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
      super.onSizeChanged(w, h, oldw, oldh);
      post(new Runnable() {

        @SuppressLint("NewApi")
        @Override public void run() {
          reset();
          mAnim.start();
        }
      });
    }

    @Override protected void onDetachedFromWindow() {
      super.onDetachedFromWindow();
      mAnim.cancel();
    }

    @Override public boolean isOpaque() {
      return true;
    }
  }

  @Override public void onStart() {
    super.onStart();

    getWindow().addFlags(
        WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
            | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
    );
  }

  @Override public void onResume() {
    super.onResume();
    Board board = new Board(this, null);
    setContentView(board);

    board.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

      @Override public void onSystemUiVisibilityChange(int vis) {
        if (0 == (vis & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)) {
          finish();
        }
      }
    });
  }

  @Override public void onUserInteraction() {
    finish();
  }

}