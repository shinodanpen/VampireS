package org.luca.VampireS.customResourcePack;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import org.luca.VampireS.MainClass;

public class LocalServer {
    private final CustomResourcePack addon;

    private final MainClass plugin;

    public ResourcePackReject(CustomResourcePack addon, MainClass plugin) {
        this.addon = addon;
        this.plugin = plugin;
    }

    public int port;
    public String ip;
    private HttpServer httpServer;

    public LocalServer(CustomResourcePack addon) {
        this.addon = addon;

        File file = new File(this.getWebDirectory(), "resourcepack.zip");
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                addon.getLogger().severe(file.getParentFile().getName() + File.separator + file.getName());
                InputStream is = addon.getClass().getClassLoader().getResourceAsStream(file.getParentFile().getName() + "/" + file.getName());
                Files.copy(is, file.toPath());
            } catch (IOException e) {
                addon.getLogger().info("Error on file copying (" + file.getName() + ") > " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void start() {
        ip = "2.224.170.54";
        port = 41000; //addon.getConfig().getInt("port");
        addon.getScheduler().executeAsync(() -> {
            try {
                httpServer = Vertx.vertx().createHttpServer();
                httpServer.requestHandler(httpServerRequest -> {
                    addon.getLogger().debug("| HTTP Request:");
                    if (httpServerRequest.uri().contains("/favicon.ico")) {
                        addon.getLogger().debug("| Rejected: Favicon sending");
                        httpServerRequest.response().setStatusCode(401);
                        httpServerRequest.response().end();
                    } else {
                        if (!httpServerRequest.uri().contains("/")) {
                            addon.getLogger().debug("| Rejected: Invalid URL");
                            httpServerRequest.response().setStatusCode(401);
                            httpServerRequest.response().end();
                        } else {
                            String[] parts = httpServerRequest.uri().split("/");
                            if (parts.length != 2) {
                                addon.getLogger().debug("| Rejected: No token");
                                httpServerRequest.response().setStatusCode(401);
                                httpServerRequest.response().end();
                            } else {
                                String token = parts[1];
                                if (UtilToken.isValidToken(token)) {
                                    addon.getLogger().debug("| Sending file...");
                                    httpServerRequest.response().sendFile(getFileLocation());
                                } else {
                                    addon.getLogger().debug("| Rejected: Invalid token");
                                    httpServerRequest.response().setStatusCode(401);
                                    httpServerRequest.response().end();
                                }
                            }
                        }
                    }
                });
                httpServer.listen(port);
                addon.getLogger().debug("| Started internal webserver");
            } catch (Exception ex) {
                addon.getLogger().severe("Unable to bind to port " + port + ". Please assign the plugin to a different port!");
                ex.printStackTrace();
            }
        });
    }

    public void stop() {
        httpServer.close();
    }

    public String getFileLocation() {
        return addon.getDataFolder().getPath() + File.separator + "web" + File.separator + "resourcepack.zip";
    }

    public File getWebDirectory() {
        return new File(addon.getDataFolder(), "web");
    }

}

