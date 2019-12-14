package org.luca.VampireS.resourcepack;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import org.luca.VampireS.VampireSPlugin;

public class LocalServer {
    private final CustomResourcePack addon;

    private VampireSPlugin plugin;

    public int port;
    public String ip;
    private HttpServer httpServer;

    public LocalServer(CustomResourcePack addon, VampireSPlugin plugin) {
        this.addon = addon;
        this.plugin = plugin;

        File file = new File(this.getWebDirectory(), "resourcepack.zip");
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                plugin.getLogger().info("| Loading resource pack at: " + file.getParentFile().getName() + File.separator + file.getName());
                InputStream is = addon.getClass().getClassLoader().getResourceAsStream(file.getParentFile().getName() + "/" + file.getName());
                Files.copy(is, file.toPath());
            } catch (IOException e) {
                plugin.getLogger().info("Error on file copying (" + file.getName() + ") > " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void start() {
        ip = plugin.getConfig().getResourcepackServerIp();
        port = plugin.getConfig().getResourcepackServerPort();
        plugin.getScheduler().runTaskAsynchronously(() -> {
            try {
                httpServer = Vertx.vertx().createHttpServer();
                httpServer.requestHandler(httpServerRequest -> {
                    plugin.getLogger().info("| HTTP Request:");
                    if (httpServerRequest.uri().contains("/favicon.ico")) {
                        plugin.getLogger().info("| Rejected: Favicon sending");
                        httpServerRequest.response().setStatusCode(401);
                        httpServerRequest.response().end();
                    } else {
                        if (!httpServerRequest.uri().contains("/")) {
                            plugin.getLogger().info("| Rejected: Invalid URL");
                            httpServerRequest.response().setStatusCode(401);
                            httpServerRequest.response().end();
                        } else {
                            String[] parts = httpServerRequest.uri().split("/");
                            if (parts.length != 2) {
                                plugin.getLogger().info("| Rejected: No token");
                                httpServerRequest.response().setStatusCode(401);
                                httpServerRequest.response().end();
                            } else {
                                String token = parts[1];
                                if (UtilToken.isValidToken(token)) {
                                    plugin.getLogger().info("| Sending file...");
                                    httpServerRequest.response().sendFile(getFileLocation());
                                } else {
                                    plugin.getLogger().info("| Rejected: Invalid token");
                                    httpServerRequest.response().setStatusCode(401);
                                    httpServerRequest.response().end();
                                }
                            }
                        }
                    }
                });
                httpServer.listen(port);
                plugin.getLogger().info("| Started internal webserver");
            } catch (Exception ex) {
                plugin.getLogger().severe("Unable to bind to port " + port + ". Please assign the plugin to a different port!");
                ex.printStackTrace();
            }
        });
    }

    public void stop() {
        httpServer.close();
    }

    public String getFileLocation() {
        return plugin.getDataFolder().getPath() + File.separator + "web" + File.separator + "resourcepack.zip";
    }

    public File getWebDirectory() {
        return new File(plugin.getDataFolder(), "web");
    }

}

