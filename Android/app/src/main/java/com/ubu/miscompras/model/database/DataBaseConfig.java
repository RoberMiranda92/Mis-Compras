package com.ubu.miscompras.model.database;

import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;
import com.ubu.miscompras.model.Category;
import com.ubu.miscompras.model.Product;
import com.ubu.miscompras.model.ProductLine;
import com.ubu.miscompras.model.Ticket;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

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
public class DataBaseConfig extends OrmLiteConfigUtil {

    private static final Class<?>[] classes = new Class[]{Ticket.class, Product.class, Category.class, ProductLine.class};

    public static void main(String[] args) throws IOException, SQLException {

        String currDirectory = "user.dir";

        String configPath = "/src/main/res/raw/ormlite_config.txt";

        /**
         * Gets the project root directory
         */
        String projectRoot = System.getProperty(currDirectory);

        /**
         * Full configuration path includes the project root path, and the location
         * of the ormlite_config.txt file appended to it
         */
        String fullConfigPath = projectRoot + configPath;

        File configFile = new File(fullConfigPath);

        /**
         * In the a scenario where we run this program serveral times, it will recreate the
         * configuration file each time with the updated configurations.
         */
        if (configFile.exists()) {
            configFile.delete();
            configFile = new File(fullConfigPath);
        }

        /**
         * writeConfigFile is a util method used to write the necessary configurations
         * to the ormlite_config.txt file.
         */
        try {
            writeConfigFile(configFile, classes);
        } catch (IOException e) {
            Log.d("Exception", e.getMessage());
        }
    }
}
