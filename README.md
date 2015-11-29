# WSOC - Web Service Office Converter

Bietet einen SOAP- Webservice um Dokumente umzuwandeln (zB .doc in .pdf).

Im Hintergrund wird für die Umwandlung ein StarOffice Abgleger benötigt.

- LibreOffice (getestet)
- OpenOffice
- FreeOffice
- StarOffice

Zum mergen von pdfs wird das Programm pdfsam benötigt.

## Bau der Anwendung

Zum erzeugen der Jar wird Maven verwendet.

`mvn install`

## Verwendung der SOAP- Service

Aufruf mittels `jar -jar <artefact>.jar [port]`.

Es gibt 2 öffentliche Methoden `convert` und `mergePdfs`

### convert

```java
public byte[] convert(byte[] fileBytes,String fromFormat, String toFormat)
```

- `fileBytes` ist ein Bytestream der die Datei enthält die umgewandelt werden soll.
- `fromFormat` Dateiendung der umzuwandelen Datei (zB doc, docx, odf, ppt, u.s.w.)
- `toFormat` Dateiendung der Ziels (zB pdf)
- Rückgabewert ist wieder ein Bytestream der Ausgabedatei oder null im Fehlerfall.

### mergePdfs

```java
public byte[] mergePdfs(List<byte[]> files)
```

- `files` ist eine Liste von Bytestreams mit PDFs die verbunden werden sollen.
- Rückgamewert ist die erstellte PDF oder null im Fehlerfall.