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

/**
 * Created by RobertoMiranda on 6/11/15.
 */
public class Constans {


    public static final String PACKAGE_NAME = "com.ubu.miscompras";

    public static final String DATABASE_NAME = "db.sqlite";

    public static final String PROTOCOL = "http://";

    public static final String PORT = ":8080";

    public static final String URL_FILE_UPLOAD = "/misCompras/rest/file/upload";
    public static final String URL_DICCIONARIO_UPLOAD = "/misCompras/rest/file/diccionario";

    public static int CONNECTION_TIMEOUT = 15000;
    public static int SOCKET_TIMEOUT = 30000;

    public static int RESUMEN_POSITION_FRAGMENT = 0;
    public static int PODUCTS_POSITION_FRAGMENT = 1;
    public static int HISTORIAL_POSITION_FRAGMENT = 2;
}
