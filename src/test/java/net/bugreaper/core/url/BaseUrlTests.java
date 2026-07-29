package net.bugreaper.core.url;

import net.bugreaper.core.exceptions.BaseUrlException;
import net.bugreaper.core.filereaders.ResourcesFileReader;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;


import static net.bugreaper.core.url.BaseUrl.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

class BaseUrlTests {

    private static ClientAndServer mockServer;
    private static MockServerClient client;

    @BeforeAll
    static void startServer() {
        mockServer = ClientAndServer.startClientAndServer(1080);
        client = new MockServerClient("localhost", 1080);
    }

    @BeforeEach
    void cleanServer() {
        client.reset();
    }

    @AfterAll
    static void stopServer() {
        mockServer.stop();
    }

    @Test
    void testReadFromUrl() {

        String body = "test message";

        client.when(
                request()
                        .withMethod("GET")
                        .withPath("/download/1")
        ).respond(
                response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/octet-stream")
                        .withHeader("Content-Disposition", "attachment; filename=\"test.txt\"")
                        .withBody(body)
        );

        assertEquals(body, readBody("http://localhost:1080/download/1"));

    }

    @Test
    void testReadFromUrlWithLines() {

        String body = """
        test message
        
        with lines
        """;

        client.when(
                request()
                        .withMethod("GET")
                        .withPath("/download/1")
        ).respond(
                response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/octet-stream")
                        .withHeader("Content-Disposition", "attachment; filename=\"test.txt\"")
                        .withBody(body)
        );

        assertEquals(body, readBody("http://localhost:1080/download/1"));

    }

    //download

    // custom file name

    @Test
    void testSaveFromUrl() {

        String body = "test message2";
        String filePath = "temp/my_download.txt";


        beforeTestDelete(filePath);


        client.when(
                request()
                        .withMethod("GET")
                        .withPath("/download/2")
        ).respond(
                response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/octet-stream")
                        .withHeader("Content-Disposition", "attachment; filename=\"test.txt\"")
                        .withBody(body)
        );

        downloadFile("http://localhost:1080/download/2", filePath);

        assertEquals(
                body,
                ResourcesFileReader.readResourceFile(filePath));
    }

    // content file name

    @Test
    void testSaveFromUrlDownloadFileWithContentName() {

        String body = "test message2";
        String filePath = "temp/"; //with slash

        beforeTestDelete(filePath + "test2.txt");

        client.when(
                request()
                        .withMethod("GET")
                        .withPath("/download/2")
        ).respond(
                response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/octet-stream")
                        .withHeader("Content-Disposition", "attachment; filename=\"test.txt\"")
                        .withBody(body)
        );

        downloadFileWithContentName("http://localhost:1080/download/2", filePath);

        assertEquals(
                body,
                ResourcesFileReader.readResourceFile(filePath + "test.txt"));
    }

    @Test
    void testSaveFromUrlDownloadFileWithContentName2() {

        String body = "test message22222";
        String filePath = "temp"; //without slash

        beforeTestDelete(filePath + "/test2.txt");

        client.when(
                request()
                        .withMethod("GET")
                        .withPath("/download/2")
        ).respond(
                response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/octet-stream")
                        .withHeader("Content-Disposition", "attachment; filename=\"test2.txt\"")
                        .withBody(body)
        );

        downloadFileWithContentName("http://localhost:1080/download/2", filePath);

        assertEquals(
                body,
                ResourcesFileReader.readResourceFile(filePath + "/test2.txt"));
    }

    @Test
    void testSaveFromUrlDownloadFileWithContentNameAbsentHeader() {

        String body = "test message2";
        String filePath = "temp/";

        beforeTestDelete(filePath + "downloaded-file");

        client.when(
                request()
                        .withMethod("GET")
                        .withPath("/download/2")
        ).respond(
                response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/octet-stream")
                        .withBody(body)
        );

        downloadFileWithContentName("http://localhost:1080/download/2", filePath);

        assertEquals(
                body,
                ResourcesFileReader.readResourceFile(filePath + "downloaded-file"));
    }

    @Test
    void testSaveFromUrlDownloadFileWithContentNameEmptyHeader() {

        String body = "test message2";
        String filePath = "temp/";

        beforeTestDelete(filePath + "downloaded-file");

        client.when(
                request()
                        .withMethod("GET")
                        .withPath("/download/2")
        ).respond(
                response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/octet-stream")
                        .withHeader("Content-Disposition", "attachment; filename=")
                        .withBody(body)
        );

        downloadFileWithContentName("http://localhost:1080/download/2", filePath);

        assertEquals(
                body,
                ResourcesFileReader.readResourceFile(filePath + "downloaded-file"));
    }

    //  negative

    @Test
    void testReadFromUrl404() {

        client.when(
                request()
                        .withMethod("GET")
                        .withPath("/download/error")
        ).respond(
                response()
                        .withStatusCode(404)
                        .withBody("error message")
        );

        Throwable exception = assertThrows(BaseUrlException.class, () ->
                readBody("http://localhost:1080/download/error"));

        MatcherAssert.assertThat(
                exception.getMessage(),
                StringContains.containsString("""
                        HTTP error 404:
                        error message"""));

    }

    @Test
    void testWrongUrlRead() {
        Throwable ex = assertThrows(BaseUrlException.class, () ->
                readBody("dummy")
        );

        assertTrue(ex.getMessage().contains("BaseUrl error:"));
    }

    @Test
    void testReadEmptyBody200() {
        client.when(
                request()
                        .withMethod("GET")
                        .withPath("/download/empty")
        ).respond(
                response()
                        .withStatusCode(200)
        );

        assertEquals(
                "",
                readBody("http://localhost:1080/download/empty"));
    }

    @Test
    void testWrongFilePath() {
        String invalidPath = "?:/invalid-path/test.txt"; // invalid on Windows/Linux

        Throwable ex = assertThrows(BaseUrlException.class, () ->
                downloadFile("http://example.com", invalidPath)
        );

        assertTrue(ex.getMessage().contains("File download error"));
    }


    @Test
    void testSaveFromUrlDownloadFileWithContentName400() {

        String body = "error";
        String filePath = "temp/";

        beforeTestDelete(filePath + "downloaded-file");

        client.when(
                request()
                        .withMethod("GET")
                        .withPath("/download/2")
        ).respond(
                response()
                        .withStatusCode(400)
                        .withBody(body)
        );

        Throwable ex = assertThrows(BaseUrlException.class, () ->
                downloadFileWithContentName("http://localhost:1080/download/2", filePath)
        );

        assertEquals(
                "HTTP error: 400",
                ex.getMessage());
    }


    private void beforeTestDelete(String filePath) {
        try {
            ResourcesFileReader.deleteResourceFile(filePath);
        } catch (Exception e) {
            System.out.println("skip");
        }
    }

}
