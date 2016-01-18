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
package com.ubu.miscompras.presenter;

import com.ubu.miscompras.model.Category;
import com.ubu.miscompras.model.ProductLine;
import com.ubu.miscompras.model.Ticket;

import java.util.List;

/**
 * Created by RobertoMiranda on 17/12/15.
 */
public interface IOnLoadComplete {


    public void showError();

    public void loadCompleteCategory(List<Category> items);

    public void loadCompleteLine(List<ProductLine> items);

    public void loadCompleteTicket(List<Ticket> items);

    public void getCategories();

    public void onResume();
}
