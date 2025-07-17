package com.lab;

import java.io.IOException;
import java.util.Collections;

import org.apache.beam.sdk.io.FileSystems;
import org.apache.beam.sdk.io.fs.ResourceId;
import org.apache.beam.sdk.io.fs.MoveOptions;

public class ArchivoUtils {

    /**
     * Borra un archivo dado su ResourceId
     */
    public static void borrarArchivo(ResourceId archivo) throws IOException {
        if (FileSystems.matchNewResource(archivo.toString(), false) != null) {
            FileSystems.delete(Collections.singletonList(archivo));
            System.out.println("Archivo borrado: " + archivo.toString());
        } else {
            System.out.println("Archivo no existe para borrar: " + archivo.toString());
        }
    }

    /**
     * Renombra (mueve) un archivo temporal a archivo final
     */
    public static void renombrarArchivo(ResourceId archivoOrigen, ResourceId archivoDestino) throws IOException {
        FileSystems.rename(
            Collections.singletonList(archivoOrigen),
            Collections.singletonList(archivoDestino),
            MoveOptions.StandardMoveOptions.IGNORE_MISSING_FILES
        );
        System.out.println("Archivo renombrado de " + archivoOrigen.toString() + " a " + archivoDestino.toString());
    }
}
