package com.ojassoft.astrosage.varta.aichat;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Persists a rolling AI chat trace so intermittent streaming issues can be inspected after repro.
 *
 * The logger overwrites the trace at the start of each new AI question and then appends compact
 * server and UI state transitions to a small app-private file.
 */
public final class AIChatTraceLogger {

    private static final String TAG = "AIChunkTrace";
    private static final String TRACE_FILE_NAME = "ai_chat_chunk_trace.log";
    private static final int MAX_TRACE_BYTES = 64 * 1024;
    private static final int PREVIEW_LIMIT = 120;
    private static final Object TRACE_LOCK = new Object();
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    /**
     * Prevents instantiation because this helper exposes stateless static APIs only.
     */
    private AIChatTraceLogger() {
    }

    /**
     * Starts a new trace for the latest user question by replacing any older persisted content.
     *
     * @param context application or activity context used to access app-private storage
     * @param question latest user question that triggered the server request
     * @param channelId active chat channel id, when available
     */
    public static void startRequestTrace(@NonNull Context context, String question, String channelId) {
        StringBuilder builder = new StringBuilder();
        builder.append(formatPrefix("REQUEST_START"));
        builder.append("channelId=").append(safeValue(channelId));
        builder.append(", questionLen=").append(lengthOf(question));
        builder.append(", questionPreview=").append(preview(question));
        builder.append('\n');
        writeTrace(context, builder.toString(), false);
    }

    /**
     * Appends one compact trace line to the latest request log.
     *
     * @param context application or activity context used to access app-private storage
     * @param stage short stage identifier such as SERVER_PARSE or UI_FINISH
     * @param details stage-specific key/value details
     */
    public static void append(@NonNull Context context, @NonNull String stage, String details) {
        StringBuilder builder = new StringBuilder();
        builder.append(formatPrefix(stage));
        builder.append(safeValue(details));
        builder.append('\n');
        writeTrace(context, builder.toString(), true);
    }

    /**
     * Returns the absolute app-private path where the latest AI trace is stored.
     *
     * @param context application or activity context used to resolve the file path
     * @return absolute trace file path
     */
    public static String getTraceFilePath(@NonNull Context context) {
        return getTraceFile(context).getAbsolutePath();
    }

    /**
     * Builds a short single-line preview suitable for persisted debug traces.
     *
     * @param value raw text that may contain line breaks or large payloads
     * @return compact preview trimmed to a safe debug length
     */
    public static String preview(String value) {
        if (TextUtils.isEmpty(value)) {
            return "\"\"";
        }
        String normalized = value
                .replace("\r", " ")
                .replace("\n", "\\n")
                .replace("<br><br>", "<br><br>")
                .replace("<br>", "<br>")
                .trim();
        if (normalized.length() > PREVIEW_LIMIT) {
            normalized = normalized.substring(0, PREVIEW_LIMIT) + "...";
        }
        return normalized;
    }

    /**
     * Writes trace data to the rolling app-private file while capping file size.
     *
     * @param context application or activity context used to access app-private storage
     * @param entry content to store
     * @param append whether the entry should be appended to the current trace
     */
    private static void writeTrace(@NonNull Context context, @NonNull String entry, boolean append) {
        synchronized (TRACE_LOCK) {
            File traceFile = getTraceFile(context);
            try {
                String traceContent = entry;
                if (append && traceFile.exists()) {
                    traceContent = readFile(traceFile) + entry;
                }
                if (traceContent.length() > MAX_TRACE_BYTES) {
                    traceContent = traceContent.substring(traceContent.length() - MAX_TRACE_BYTES);
                }
                FileOutputStream outputStream = new FileOutputStream(traceFile, false);
                outputStream.write(traceContent.getBytes(UTF_8));
                outputStream.flush();
                outputStream.close();
                Log.d(TAG, entry.trim());
            } catch (Exception e) {
                Log.e(TAG, "Failed to persist AI trace", e);
            }
        }
    }

    /**
     * Returns the app-private file that stores the latest AI trace.
     *
     * @param context application or activity context used to resolve the trace file
     * @return trace file handle
     */
    private static File getTraceFile(@NonNull Context context) {
        return new File(context.getFilesDir(), TRACE_FILE_NAME);
    }

    /**
     * Reads the full contents of the current trace file.
     *
     * @param file trace file to read
     * @return file contents as UTF-8 text
     */
    private static String readFile(@NonNull File file) throws Exception {
        FileInputStream inputStream = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        int readBytes = inputStream.read(data);
        inputStream.close();
        if (readBytes <= 0) {
            return "";
        }
        return new String(data, 0, readBytes, UTF_8);
    }

    /**
     * Builds the shared prefix used on every persisted trace line.
     *
     * @param stage short stage identifier for the current trace event
     * @return formatted trace prefix with timestamp and thread name
     */
    private static String formatPrefix(@NonNull String stage) {
        String timestamp = new SimpleDateFormat("HH:mm:ss.SSS", Locale.US).format(new Date());
        return "[" + timestamp + "][" + Thread.currentThread().getName() + "][" + stage + "] ";
    }

    /**
     * Returns a safe printable value for nullable trace fields.
     *
     * @param value nullable string value
     * @return printable placeholder when the value is null
     */
    private static String safeValue(String value) {
        return value == null ? "null" : value;
    }

    /**
     * Returns the character length for nullable strings without extra allocations.
     *
     * @param value nullable string value
     * @return string length or zero when the value is null
     */
    private static int lengthOf(String value) {
        return value == null ? 0 : value.length();
    }
}
