package ch.kos.goat.services;

import ch.kos.goat.entities.Moment;
import ch.kos.goat.repositories.MomentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.io.File;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class YtDlpService {

    private final MomentRepository momentRepository;

    @Async
    public void downloadVideo(Long momentId, String videoUrl) {
        try {
            String outputTemplate = Paths.get("uploads", "moment-" + momentId + ".%(ext)s").toString();

            ProcessBuilder pb = new ProcessBuilder(
                    "yt-dlp",
                    "-f", "bestvideo[ext=mp4]+bestaudio[ext=m4a]/best[ext=mp4]/best",
                    "-o", outputTemplate,
                    "--no-playlist",
                    videoUrl
            );

            pb.inheritIO();

            Process process = pb.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                updateMomentWithFile(momentId);
            } else {
                System.err.println("yt-dlp failed with code: " + exitCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateMomentWithFile(Long momentId) {
        File uploadDir = new File("uploads");
        File[] files = uploadDir.listFiles((dir, name) -> name.startsWith("moment-" + momentId + "."));

        if (files != null && files.length > 0) {
            File bestMatch = null;
            for (File f : files) {
                if (!f.getName().endsWith(".part")) {
                    bestMatch = f;
                    break;
                }
            }

            if (bestMatch != null) {
                Moment moment = momentRepository.findById(momentId).orElseThrow();
                moment.setLocalPath("uploads/" + bestMatch.getName());
                momentRepository.save(moment);
                System.out.println("ARCHIVE SUCCESS: " + moment.getTitle());
            }
        }
    }
}