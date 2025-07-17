# Apache Beam

This project implements an **Apache Beam pipeline in Java** that processes `.csv` files distributed in date-based folders and removes lines whose content matches a set of keys from a source file.

## Process

- Reads a source file of keys (parameterizable filename), where each line represents a key to be removed.
- Searches for `.csv` files using a pattern (e.g., `destination/**/file.csv`).
- Iterates line by line through each destination file and **removes lines that exactly match any key from the source file**.
- **Preserves the number of lines** by writing an empty line instead of deleting it.
- Generates a temporary file and then replaces the original file with it.

## Project Structure

```text
com.lab/
├── MainPipeline.java # Main pipeline class
├── LabOptions.java # Custom pipeline options
├── ClaveUtils.java # Loads the key file as side input
└── ArchivoUtils.java # Functions for deleting and renaming files
```

## Expected Directory Structure
```text
source/
└── source.csv # File with keys (one per line)
```

```text
destination/
├── 2025-07-10/
│ └── name.csv
├── 2025-07-11/
│ └── name.csv
└── ...
```

## How to Run
```text
java -jar target/beam-lab-1.0-SNAPSHOT.jar --rutaSource=<source_file> --patternDestination=<destination_path>
```

- `--rutaSource=<source_file>` refers to the file containing the keys.
- `--patternDestination=<destination_path>` refers to the path where the destination files are located. The file pattern is parameterizable and can be adjusted.

### Example
```text
java -jar target/beam-lab-1.0-SNAPSHOT.jar --rutaSource=source/source_1.csv --patternDestination=destination/*/*.csv
```

Consider increasing memory with parameters like `-Xmx8G` if processing a large amount of data.

The "captura_uso_recursos" folder contains evidence of CPU usage with different file sizes.
