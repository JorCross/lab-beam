package com.lab;

import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.FileIO;
import org.apache.beam.sdk.io.FileSystems;
import org.apache.beam.sdk.io.fs.ResourceId;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.util.MimeTypes;
import org.apache.beam.sdk.values.PCollectionView;

public class MainPipeline {

    public static void main(String[] args) {

        LabOptions options = PipelineOptionsFactory
            .fromArgs(args)
            .withValidation()
            .as(LabOptions.class);

        Pipeline pipeline = Pipeline.create(options);

        PCollectionView<List<String>> clavesView = ClaveUtils.cargarClaves(pipeline, options.getRutaSource());

        pipeline
            .apply("Buscar archivos", FileIO.match().filepattern(options.getPatternDestination()))
            .apply("Leer archivos", FileIO.readMatches())
            .apply("Procesar y escribir archivos", ParDo.of(new DoFn<FileIO.ReadableFile, Void>() {

                @ProcessElement
                public void processElement(ProcessContext c) {
                    try {
                        FileIO.ReadableFile archivo = c.element();
                        String rutaOriginal = archivo.getMetadata().resourceId().toString();
                        String rutaTemporal = rutaOriginal + ".tmp";

                        List<String> claves = c.sideInput(clavesView);
                        Set<String> setClaves = new HashSet<>(claves);

                        // Crear el archivo temporal usando Filesystems
                        ResourceId tempPath = FileSystems.matchNewResource(rutaTemporal, false);
                        try (
                            BufferedWriter writer = new BufferedWriter(Channels.newWriter(
                                FileSystems.create(tempPath, MimeTypes.TEXT), StandardCharsets.UTF_8.name()))
                        ) {
                            try (
                                BufferedReader reader = new BufferedReader(Channels.newReader(
                                    archivo.openSeekable(), StandardCharsets.UTF_8.name()))
                            ) {
                                String linea;
                                while ((linea = reader.readLine()) != null) {
                                    writer.write(setClaves.contains(linea) ? "" : linea);
                                    writer.newLine(); // mantiene espacios vacíos
                                }
                            }
                        }

                        // Reemplazar archivo original por el temporal
                        ResourceId originalPath = FileSystems.matchNewResource(rutaOriginal, false);
                        ArchivoUtils.borrarArchivo(originalPath);
                        ArchivoUtils.renombrarArchivo(tempPath, originalPath);

                        System.out.println("Archivo procesado: " + rutaOriginal);

                    } catch (Exception e) {
                        System.err.println("Error procesando archivo: " + e.getMessage());
                        e.printStackTrace();
                    }
                }


            }).withSideInputs(clavesView));

        pipeline.run().waitUntilFinish();
    }
}
