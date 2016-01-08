package com.ubu.miscompras.model.database;

import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;
import com.ubu.miscompras.model.Categoria;
import com.ubu.miscompras.model.Producto;
import com.ubu.miscompras.model.Ticket;
import com.ubu.miscompras.model.LineaProducto;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by RobertoMiranda on 6/11/15.
 */
public class DataBaseConfig extends OrmLiteConfigUtil {

    private static final Class<?>[] classes = new Class[]{Ticket.class, Producto.class, Categoria.class, LineaProducto.class};

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
