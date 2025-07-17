# Apache Beam

Este proyecto implementa un pipeline de **Apache Beam en Java** que procesa archivos `.csv` distribuidos en carpetas por fecha, y elimina líneas cuyo contenido coincida con un conjunto de claves provenientes de un archivo source.

## Proceso

- Lee un archivo de claves source (nombre parametrizable), cada línea representa una clave a eliminar.
- Busca archivos `.csv` según un patrón (por ejemplo, `destination/**/file.csv`).
- Recorre línea por línea cada archivo destino, y **elimina las líneas que coincidan exactamente con alguna clave del source**.
- **Preserva la cantidad de líneas**, escribiendo una línea vacía en lugar de eliminar la línea.
- Genera un archivo temporal, y luego reemplaza el archivo original por este.

## Estructura del proyecto
```text
com.lab/
├── MainPipeline.java # Clase principal del pipeline
├── LabOptions.java # Opciones personalizadas para el pipeline
├── ClaveUtils.java # Carga el archivo de claves como side input
└── ArchivoUtils.java # Funciones para borrar y renombrar archivos
```

## Ejemplo de estructura esperada
```text
source/
└── source.csv         # Archivo con claves (una por línea)
```
```text
destination/
├── 2025-07-10/
│   └── name.csv
├── 2025-07-11/
│   └── name.csv
└── ...
```
## Como se ejecuta
```text
java -jar target/beam-lab-1.0-SNAPSHOT.jar --rutaSource=<source_file> --patternDestination=<destination_path>
```
--rutaSource=<source_file> corresponde al archivo que contiene las claves.
--patternDestination=<destination_path> corresponde a la ruta donde se encuentran los archivos destino. El patrón de los archivos es parametrizable así que puede cambiarse.

Ejemplo de uso:
```text
java -jar target/beam-lab-1.0-SNAPSHOT.jar --rutaSource=source/source_1.csv --patternDestination=destination/*/*.csv
```
Considerar el uso de aumento de memoria con parámetros como -Xmx8G si la cantidad de datos es muy grande.
