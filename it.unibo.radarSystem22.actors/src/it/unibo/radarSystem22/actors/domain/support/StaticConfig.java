package it.unibo.radarSystem22.actors.domain.support;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import it.unibo.actorComm.utils.ColorsOut;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class StaticConfig {
    public static void setTheConfiguration(Class clazz, String resourceName) {
        //Nella distribuzione resourceName è in una dir che include la bin
        try {
            ColorsOut.out("%%% setTheConfiguration from file:" + resourceName);

            FileReader reader = new FileReader(resourceName);
            FileWriter writer = null;
            try {
                JSONTokener tokener = new JSONTokener(reader);
                JSONObject object = new JSONObject(tokener);

                boolean changed = setFields(clazz, object);
                if (changed) {
                    writer = new FileWriter(resourceName);
                    saveConfigFile(object, writer);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (FileNotFoundException e) {
            ColorsOut.outappl("Config file not found, saving default config file to " + resourceName, ColorsOut.YELLOW);
            saveConfigFile(createJSONObject(clazz), resourceName);
        }
    }

    // Per testing, se usato con FileWriter sovrascriverà il file di configurazione
    // prima della lettura
    public static void setTheConfiguration(Class clazz, Reader reader, Writer writer) {
        //Nella distribuzione resourceName è in una dir che include la bin
        try {
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject object = new JSONObject(tokener);

            boolean changed = setFields(clazz, object);
            if (changed) {
                saveConfigFile(object, writer);
            }

        } catch (JSONException e) {
            ColorsOut.outerr("setTheConfiguration ERROR " + e.getMessage() );
        }
    }

    public static void saveConfigFile(JSONObject object, String resourceName) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(resourceName);
            saveConfigFile(object, fw);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void saveConfigFile(JSONObject object, Writer writer) {
        try {
            writer.write(object.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static JSONObject createJSONObject(Class clazz) {
        try {
            JSONObject object = new JSONObject();
            for (Field field : getPublicStaticFields(clazz)) {
                object.put(field.getName(), field.get(null));
            }
            return object;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.err.println("Illegal access exception when saving JSON object, shouldn't happen");
            return null;
        }
    }

    /**
     * @return true if the object was changed due to missing fields
     */
    private static boolean setFields(Class clazz, JSONObject loadedObject) {
        boolean changed = false;
        for (Field field : getPublicStaticFields(clazz)) {
            String name = field.getName();
            Object val = loadedObject.opt(name);
            if (val != null) {
                try {
                    field.set(null, val);
                } catch (IllegalAccessException e) {
                    e.printStackTrace(); // shouldn't happen, but jic
                }
            } else {
                try {
                    loadedObject.put(name, field.get(null));
                    changed = true;
                    ColorsOut.outappl("Field " + name + " not present in config, using default", ColorsOut.ANSI_YELLOW);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace(); // shouldn't happen, but jic
                }
            }
        }
        return changed;
    }

    private static List<Field> getPublicStaticFields(Class clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        List<Field> staticFields = new ArrayList<Field>();
        for (Field field : declaredFields) {
            if (Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers())) {
                staticFields.add(field);
            }
        }
        return staticFields;
    }
}

/*
public static void setTheConfiguration(  ) {
setTheConfiguration("../DomainSystemConfig.json");
}

public static void setTheConfiguration( String resourceName ) {
StaticConfig.setTheConfiguration(DomainSystemConfig.class, resourceName);
}
*/