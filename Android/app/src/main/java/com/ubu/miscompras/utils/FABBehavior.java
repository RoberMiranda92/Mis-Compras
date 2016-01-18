/*
*   Copyright (C) 2015 Roberto Miranda.
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*/
package com.ubu.miscompras.utils;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Method;

/**
 * Clase que controla la animación de ocultación de un FabBUtton dentro de un CoordinatorLayout.
 *
 * @author <a href="mailto:rmp0046@gmail.com">Roberto Miranda Pérez</a>
 */
public class FABBehavior extends FloatingActionButton.Behavior {

    public FABBehavior(Context context, AttributeSet attributeSet) {

    }

    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        if (dependency instanceof Snackbar.SnackbarLayout) {
            return super.onDependentViewChanged(parent, child, dependency);
        } else if (dependency instanceof AppBarLayout) {
            this.updateFabVisibility(parent, (AppBarLayout) dependency, child);
        }
        return false;
    }

    private boolean updateFabVisibility(CoordinatorLayout parent, AppBarLayout appBarLayout, FloatingActionButton child) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if (lp.getAnchorId() != appBarLayout.getId()) {
            return false;
        } else {

            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
            int point = child.getTop() - params.topMargin;
            try {
                Method method = AppBarLayout.class.getDeclaredMethod("getMinimumHeightForVisibleOverlappingContent");
                method.setAccessible(true);

                if (point <= (int) method.invoke(appBarLayout)) {
                    child.hide();
                } else {
                    child.show();
                }
                return true;
            } catch (Exception e) {
                return true;
            }
        }
    }
}
