package com.lab;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.transforms.View;
import org.apache.beam.sdk.values.PCollectionView;
import java.util.List;

public class ClaveUtils {
    public static PCollectionView<List<String>> cargarClaves(Pipeline pipeline, String rutaSource) {
        return pipeline
            .apply("Leer claves", TextIO.read().from(rutaSource))
            .apply("A vista", View.asList());
    }
}
