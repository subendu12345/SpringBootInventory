package com.prod.GreenValley.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;



@Service
public class DataBaseBackupService {
    @Value("${backup.mysqldump.path}")
    private String mysqldumpPath;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${backup.output.directory}")
    private String outputDirectory;

    @Value("${backup.filename}")
    private String backupFilename;

    /*/

    **
     * Creates a full database backup using the mysqldump utility.
     * The backup file is saved to the configured output directory.
     *
     * @return The absolute path to the generated backup file.
     * @throws IOException If a file system or process execution error occurs.
     * @throws InterruptedException If the process is interrupted.
     */
    public String createDatabaseBackup() throws IOException, InterruptedException {
        // Extract database name from the URL
        String dbName = dbUrl.substring(dbUrl.lastIndexOf('/') + 1);

        // Create the backup directory if it doesn't exist
        Path backupDir = Paths.get(outputDirectory);
        if (!Files.exists(backupDir)) {
            Files.createDirectories(backupDir);
        }

        // Generate a timestamped file name
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String timestamp = LocalDateTime.now().format(formatter);
        String finalBackupFilename = "backup_" + timestamp + "_" + dbName + ".sql";
        Path backupFilePath = backupDir.resolve(finalBackupFilename);

        // Build the mysqldump command
        ProcessBuilder processBuilder = new ProcessBuilder(
            mysqldumpPath,
            "-u", dbUsername,
            "-p" + dbPassword,
            "--databases", dbName
        );

        // Redirect the output to the backup file
        processBuilder.redirectOutput(backupFilePath.toFile());

        System.out.println("Starting database backup for " + dbName + "...");
        Process process = processBuilder.start();

        // Wait for the process to complete (with a timeout)
        boolean finished = process.waitFor(60, TimeUnit.SECONDS);

        if (!finished || process.exitValue() != 0) {
            throw new RuntimeException("Database backup failed or timed out.");
        }

        System.out.println("Database backup successful! File saved to: " + backupFilePath.toAbsolutePath());
        return backupFilePath.toAbsolutePath().toString();
    }
}
