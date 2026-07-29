package net.bugreaper.core.url;

import net.bugreaper.core.exceptions.BaseUrlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


import static net.bugreaper.core.filereaders.pathfinder.ProjectPaths.getTestResourcesPath;

public class BaseUrl {

    private BaseUrl() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger logger = LoggerFactory.getLogger(BaseUrl.class);

    /**
     * Default timeout for http connection/read
     */
    private static final int TIMEOUT = 5000;


    /**
     * Executes an HTTP GET request to the given URL and returns the response body as a string.
     *
     * <p>This method uses {@link HttpURLConnection} and automatically handles both successful
     * (2xx) and error (4xx/5xx) HTTP responses:
     * <ul>
     *     <li>For 2xx responses, the response body is read from {@code getInputStream()}</li>
     *     <li>For non-2xx responses, the response body is read from {@code getErrorStream()}</li>
     * </ul>
     *
     * <p>If the HTTP status code is not in the 2xx range, a {@link RuntimeException} is thrown
     * containing the status code and response body.</p>
     *
     * <p>This method is platform-independent and does not rely on external tools.</p>
     *
     * @param urlStr the target URL to send the GET request to
     * @return the response body as a string
     * @throws BaseUrlException if:
     *                          <ul>
     *                              <li>an I/O error occurs during the request</li>
     *                              <li>the HTTP response status is not 2xx</li>
     *                          </ul>
     */
    public static String readBody(String urlStr) {

        HttpURLConnection conn = null;

        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(TIMEOUT);
            conn.setReadTimeout(TIMEOUT);

            int status = conn.getResponseCode();

            InputStream stream = (status >= 200 && status < 300)
                    ? conn.getInputStream()
                    : conn.getErrorStream();

            if (stream == null) {
                throw new BaseUrlException("No response body, HTTP status: " + status);
            }

            try (InputStream in = stream) {

                String response = new String(in.readAllBytes(), StandardCharsets.UTF_8);

                if (status < 200 || status >= 300) {
                    throw new BaseUrlException("HTTP error " + status + ":\n" + response);
                }

                logger.debug("Request URL: {}", urlStr);
                logger.debug("Response Body:\n{}", response);

                return response;
            }

        } catch (IOException e) {
            throw new BaseUrlException("BaseUrl error:\n" + e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * Downloads a file from the given URL and saves it to the specified local file path <b>in test resources</b>.
     *
     * <p>This method opens a stream to the remote resource and writes its contents
     * to a file using a buffered copy. It is suitable for downloading binary or text files
     * of any size.</p>
     *
     * <p>The caller is responsible for providing a valid and writable file path.
     * Existing files at the target location will be overwritten.</p>
     *
     * <p>This implementation is platform-independent and does not require external tools.</p>
     *
     * @param urlStr   the URL of the file to download
     * @param filePath file path (including file name) in test-resources (example: "temp/my_file.txt")
     * @throws BaseUrlException if:
     *                          <ul>
     *                              <li>an I/O error occurs during download</li>
     *                              <li>the connection fails or the URL is invalid</li>
     *                              <li>the file cannot be created or written</li>
     *                          </ul>
     */
    public static void downloadFile(String urlStr, String filePath) {

        downloadFileWithContentName(urlStr, filePath, true);
    }

    /**
     * Downloads a file from the given URL and saves it to the specified directory <b>in test resources</b>.
     *
     * <p>The file name is resolved from the HTTP response header
     * {@code Content-Disposition}, for example:
     * <pre>
     * Content-Disposition: attachment; filename="test.txt"
     * </pre>
     *
     * <p>If the {@code Content-Disposition} header is missing or does not contain a filename,
     * a default name ({@code downloaded-file}) will be used.</p>
     *
     * <p>This method:
     * <ul>
     *     <li>Uses {@link HttpURLConnection} to perform the HTTP GET request</li>
     *     <li>Validates HTTP status (must be 2xx)</li>
     *     <li>Streams the response body to a file using a buffer</li>
     *     <li>Closes all I/O resources using try-with-resources</li>
     * </ul>
     *
     * @param urlStr  the URL to download the file from
     * @param dirPath the target directory path (relative to test resources), without file name (example: "temp/")
     * @throws BaseUrlException if:
     *                          <ul>
     *                              <li>the HTTP response status is not 2xx</li>
     *                              <li>the response stream is empty or unavailable</li>
     *                              <li>an I/O error occurs during download or file writing</li>
     *                          </ul>
     * <p><b>NOTE:</b> This method relies on the server providing a valid
     * {@code Content-Disposition} header for correct file naming.
     * If absent, a default file name is used (downloaded-file).
     */
    public static void downloadFileWithContentName(String urlStr, String dirPath) {
        downloadFileWithContentName(urlStr, dirPath, false);
    }


    private static void downloadFileWithContentName(String urlStr, String path, boolean customFileName) {

        HttpURLConnection conn = null;

        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(TIMEOUT);
            conn.setReadTimeout(TIMEOUT);

            int status = conn.getResponseCode();

            if (status < 200 || status >= 300) {
                throw new BaseUrlException("HTTP error: " + status);
            }

            String fullPath;

            if (customFileName) {

                fullPath = getTestResourcesPath() + path;

            } else {
                // Extract filename from header
                String disposition = conn.getHeaderField("Content-Disposition");
                String fileName = extractFileName(disposition);

                // default value
                if (fileName == null || fileName.isBlank()) {
                    fileName = "downloaded-file";
                }

                fullPath = getTestResourcesPath() + path + File.separator + fileName;

            }


            InputStream rawStream = conn.getInputStream();

            if (rawStream == null) {
                throw new BaseUrlException("Empty response stream");
            }

            // try-with-resources for streams
            try (InputStream in = rawStream;
                 FileOutputStream out = new FileOutputStream(fullPath)) {

                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            logger.info("Downloaded file from {} to {}", urlStr, fullPath);

        } catch (IOException e) {
            throw new BaseUrlException("File download error:\n" + e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private static String extractFileName(String disposition) {
        if (disposition == null) return null;

        for (String part : disposition.split(";")) {
            part = part.trim();
            if (part.startsWith("filename=")) {
                return part.substring("filename=".length())
                        .replace("\"", "")
                        .trim();
            }
        }
        return null;
    }

}
