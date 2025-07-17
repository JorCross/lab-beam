package com.lab;

import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.Validation;

public interface LabOptions extends PipelineOptions {

    @Description("Ruta del archivo source")
    @Validation.Required
    String getRutaSource();
    void setRutaSource(String value);

    @Description("Patrón de archivos destination")
    @Validation.Required
    String getPatternDestination();
    void setPatternDestination(String value);
}
