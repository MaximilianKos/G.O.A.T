package ch.kos.goat.services;

import ch.kos.goat.dto.og.OgMetadata;
import ch.kos.goat.dto.og.OgResponse;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.*;

@Service
public class OgService {

    private static final String USER_AGENT = "Mozilla/5.0 (compatible; SimpleOgScraper/1.0)";
    private static final int TIMEOUT_MS = 8000;

    public OgResponse scrapeOne(String inputUrl) {
        String url = ensureHttpScheme(inputUrl);
        try {
            Connection.Response resp = Jsoup.connect(url)
                    .userAgent(USER_AGENT)
                    .timeout(TIMEOUT_MS)
                    .followRedirects(true)
                    .execute();

            Document doc = resp.parse();
            String finalUrl = resp.url().toString();
            int status = resp.statusCode();

            String title = firstOg(doc, "og:title");
            String description = firstOg(doc, "og:description");
            String type = firstOg(doc, "og:type");
            String image = toAbsolute(finalUrl, firstOg(doc, "og:image"));
            String ogUrl = toAbsolute(finalUrl, firstOg(doc, "og:url"));

            OgMetadata og = new OgMetadata(title, description, type, image, ogUrl != null ? ogUrl : finalUrl);
            return new OgResponse(inputUrl, finalUrl, status, og, null);

        } catch (HttpStatusException e) {
            return new OgResponse(inputUrl, e.getUrl(), e.getStatusCode(), null, e.getMessage());
        } catch (IllegalArgumentException | IOException e) {
            return new OgResponse(inputUrl, url, 0, null, e.getMessage());
        }
    }

    private static String firstOg(Document doc, String property) {
        Element el = doc.selectFirst("meta[property=" + property + "]");
        if (el == null) return null;
        String content = el.attr("content");
        return (content == null || content.isBlank()) ? null : content.trim();
    }

    private static String ensureHttpScheme(String url) {
        if (url == null) throw new IllegalArgumentException("URL is null");
        String trimmed = url.trim();
        if (trimmed.isEmpty()) throw new IllegalArgumentException("URL is empty");
        if (!trimmed.startsWith("http://") && !trimmed.startsWith("https://")) {
            return "https://" + trimmed;
        }
        return trimmed;
    }

    private static String toAbsolute(String baseUrl, String value) {
        if (value == null || value.isBlank()) return null;
        try {
            URL base = new URL(baseUrl);
            if (value.startsWith("//")) {
                return base.getProtocol() + ":" + value;
            }
            return new URL(base, value).toString();
        } catch (Exception e) {
            return value;
        }
    }
}
