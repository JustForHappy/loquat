package poca.cn.com.lib_cmtools.utils;


import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.math.BigInteger;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import poca.cn.com.lib_cmtools.utils.sio.FileExistsException;
import poca.cn.com.lib_cmtools.utils.sio.FilenameUtils;
import poca.cn.com.lib_cmtools.utils.sio.IOUtils;

/**
 * @author WilliamJiang
 * @date 16/6/13:上午11:54
 * @desc
 */
public class FileUtils {
    // 设置成可配置
    private static final String APP_DIR = "Poca";
    private static final String TEMP_DIR = "temp";
    private static final String USER_DIR = "usr";
    private static final String SEPARATOR = File.separator;
    /**
     * The number of bytes in a kilobyte.
     */
    public static final long ONE_KB = 1024;
    /**
     * The number of bytes in a megabyte.
     */
    public static final long ONE_MB = ONE_KB * ONE_KB;
    /**
     * The number of bytes in a gigabyte.
     */
    public static final long ONE_GB = ONE_KB * ONE_MB;
    /**
     * The number of bytes in a terabyte.
     */
    public static final long ONE_TB = ONE_KB * ONE_GB;
    /**
     * The number of bytes in a petabyte.
     */
    public static final long ONE_PB = ONE_KB * ONE_TB;
    /**
     * The number of bytes in an exabyte.
     */
    public static final long ONE_EB = ONE_KB * ONE_PB;
    /**
     * The file copy buffer size (30 MB)
     */
    private static final long FILE_COPY_BUFFER_SIZE = ONE_MB * 30;
    /**
     * An empty array of type <code>File</code>.
     */
    public static final File[] EMPTY_FILE_ARRAY = new File[0];
    /**
     * The UTF-8 character set, used to decode octets in URLs.
     */
    private static final Charset UTF8 = Charset.forName("UTF-8");

    // -----------------------------------------------------------------------
    /**
     * Construct a file from the set of name elements.
     *
     * @param directory
     *            the parent directory
     * @param names
     *            the name elements
     * @return the file
     *
     */
    public static File getFile(File directory, String... names) {
        if (directory == null) {
            throw new NullPointerException("directory must not be null");
        }
        if (names == null) {
            throw new NullPointerException("names must not be null");
        }
        File file = directory;
        for (String name : names) {
            file = new File(file, name);
        }
        return file;
    }

    /**
     * Construct a file from the set of name elements.
     *
     * @param names
     *            the name elements
     * @return the file
     *
     */
    public static File getFile(String... names) {
        if (names == null) {
            throw new NullPointerException("names must not be null");
        }
        File file = null;
        for (String name : names) {
            if (file == null) {
                file = new File(name);
            } else {
                file = new File(file, name);
            }
        }
        return file;
    }

    /**
     * Returns a {@link File} representing the user's home directory.
     *
     * @return the user's home directory.
     *
     */
    public static File getUserDirectory() {
        if (!SDCardUtils.isSDCardAvailable()) {
            return null;
        }

        return new File(getUserDirectoryPath());
    }

    /**
     * Returns the path to the system user's directory.
     * @return the path to the system user's directory.
     */
    public static String getUserDirectoryPath() {
       return SDCardUtils.getExternalStorage().getAbsolutePath() + SEPARATOR + APP_DIR + SEPARATOR + USER_DIR;
    }

    /**
     *  Returns a {@link File} representing the system temporary directory.
     *  the system temporary directory.
     * @return
     */
    public static File getTempDirectory() {
        if (!SDCardUtils.isSDCardAvailable()) {
            return null;
        }

        return new File(getTempDirectoryPath());
    }

    /**
     * Returns the path to the system temporary directory.
     *
     * @returnthe  the path to the system temporary directory.
     *
     */
    public static String getTempDirectoryPath() {
        return SDCardUtils.getExternalStorage().getAbsolutePath() + SEPARATOR + APP_DIR + SEPARATOR + TEMP_DIR;
    }
    // -----------------------------------------------------------------------

    /**
     * Opens a {@link FileInputStream} for the specified file, providing better
     * error messages than simply calling <code>new FileInputStream(file)</code>
     * .
     * <p>
     * At the end of the method either the stream will be successfully opened,
     * or an exception will have been thrown.
     * <p>
     * An exception is thrown if the file does not exist. An exception is thrown
     * if the file object exists but is a directory. An exception is thrown if
     * the file exists but cannot be read.
     *
     *
     * @param file
     *      the file to open for input, must not be {@code null}
     * @return a new {@link FileInputStream} for the specified file
     *
     * @throws FileNotFoundException
     *              if the file does not exist
     * @throws IOException
     *              if the file object is a directory
     * @throws IOException
     *              if the file cannot be read
     */
    public static FileInputStream openInputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (file.canRead() == false) {
                throw new IOException("File '" + file + "' cannot be read");
            }
        } else {
            throw new FileNotFoundException("File '" + file + "' does not exist");
        }
        return new FileInputStream(file);
    }
    // -----------------------------------------------------------------------

    public static FileOutputStream openOutputStream(File file) throws IOException {
        return openOutputStream(file, false);
    }

    public static FileOutputStream openOutputStream(File file, boolean append) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (file.canWrite() == false) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null) {
                if (!parent.mkdirs() && !parent.isDirectory()) {
                    throw new IOException("Directory '" + parent + "' could not be created");
                }
            }
        }
        return new FileOutputStream(file, append);
    }
    // -----------------------------------------------------------------------
    /**
     * Implements the same behaviour as the "touch" utility on Unix. It creates
     * a new file with size 0 or, if the file exists already, it is opened and
     * closed without modifying it, but updating the file date and time.
     * <p>
     * NOTE: As from v1.3, this method throws an IOException if the last
     * modified date of the file cannot be set. Also, as from v1.3 this method
     * creates parent directories if they do not exist.
     *
     * @param file
     *            the File to touch
     * @throws IOException
     *             If an I/O problem occurs
     */
    public static void touch(File file) throws IOException {
        if (!file.exists()) {
            OutputStream out = openOutputStream(file);
            IOUtils.closeQuietly(out);
        }
        boolean success = file.setLastModified(System.currentTimeMillis());
        if (!success) {
            throw new IOException("Unable to set the last modification time for " + file);
        }
    }

    // -----------------------------------------------------------------------
    /**
     * Compares the contents of two files to determine if they are equal or not.
     * <p>
     * This method checks to see if the two files are different lengths or if
     * they point to the same file, before resorting to byte-by-byte comparison
     * of the contents.
     * <p>
     *
     * @param file1
     *            the first file
     * @param file2
     *            the second file
     * @return true if the content of the files are equal or they both don't
     *         exist, false otherwise
     * @throws IOException
     *             in case of an I/O error
     */
    public static boolean contentEquals(File file1, File file2) throws IOException {
        boolean file1Exists = file1.exists();
        if (file1Exists != file2.exists()) {
            return false;
        }

        if (!file1Exists) {
            // two not existing files are equal
            return true;
        }

        if (file1.isDirectory() || file2.isDirectory()) {
            // don't want to compare directory contents
            throw new IOException("Can't compare directories, only files");
        }

        if (file1.length() != file2.length()) {
            // lengths differ, cannot be equal
            return false;
        }

        if (file1.getCanonicalFile().equals(file2.getCanonicalFile())) {
            // same file
            return true;
        }

        InputStream input1 = null;
        InputStream input2 = null;
        try {
            input1 = new FileInputStream(file1);
            input2 = new FileInputStream(file2);
            return IOUtils.contentEquals(input1, input2);

        } finally {
            IOUtils.closeQuietly(input1);
            IOUtils.closeQuietly(input2);
        }
    }

    // -----------------------------------------------------------------------
    /**
     * Compares the contents of two files to determine if they are equal or not.
     * <p>
     * This method checks to see if the two files point to the same file, before
     * resorting to line-by-line comparison of the contents.
     * <p>
     *
     * @param file1
     *            the first file
     * @param file2
     *            the second file
     * @param charsetName
     *            the character encoding to be used. May be null, in which case
     *            the platform default is used
     * @return true if the content of the files are equal or neither exists,
     *         false otherwise
     * @throws IOException
     *             in case of an I/O error
     * @since 2.2
     * @see IOUtils#contentEqualsIgnoreEOL(Reader, Reader)
     */
    public static boolean contentEqualsIgnoreEOL(File file1, File file2, String charsetName) throws IOException {
        boolean file1Exists = file1.exists();
        if (file1Exists != file2.exists()) {
            return false;
        }

        if (!file1Exists) {
            // two not existing files are equal
            return true;
        }

        if (file1.isDirectory() || file2.isDirectory()) {
            // don't want to compare directory contents
            throw new IOException("Can't compare directories, only files");
        }

        if (file1.getCanonicalFile().equals(file2.getCanonicalFile())) {
            // same file
            return true;
        }

        Reader input1 = null;
        Reader input2 = null;
        try {
            if (charsetName == null) {
                input1 = new InputStreamReader(new FileInputStream(file1));
                input2 = new InputStreamReader(new FileInputStream(file2));
            } else {
                input1 = new InputStreamReader(new FileInputStream(file1), charsetName);
                input2 = new InputStreamReader(new FileInputStream(file2), charsetName);
            }
            return IOUtils.contentEqualsIgnoreEOL(input1, input2);

        } finally {
            IOUtils.closeQuietly(input1);
            IOUtils.closeQuietly(input2);
        }
    }

    // -----------------------------------------------------------------------
    /**
     * Convert from a <code>URL</code> to a <code>File</code>.
     * <p>
     * From version 1.1 this method will decode the URL. Syntax such as
     * <code>file:///my%20docs/file.txt</code> will be correctly decoded to
     * <code>/my docs/file.txt</code>. Starting with version 1.5, this method
     * uses UTF-8 to decode percent-encoded octets to characters. Additionally,
     * malformed percent-encoded octets are handled leniently by passing them
     * through literally.
     *
     * @param url
     *            the file URL to convert, {@code null} returns {@code null}
     * @return the equivalent <code>File</code> object, or {@code null} if the
     *         URL's protocol is not <code>file</code>
     */
    public static File toFile(URL url) {
        if (url == null || !"file".equalsIgnoreCase(url.getProtocol())) {
            return null;
        } else {
            String filename = url.getFile().replace('/', File.separatorChar);
            filename = decodeUrl(filename);
            return new File(filename);
        }
    }

    /**
     * Decodes the specified URL as per RFC 3986, i.e. transforms
     * percent-encoded octets to characters by decoding with the UTF-8 character
     * set. This function is primarily intended for usage with
     * {@link java.net.URL} which unfortunately does not enforce proper URLs. As
     * such, this method will leniently accept invalid characters or malformed
     * percent-encoded octets and simply pass them literally through to the
     * result string. Except for rare edge cases, this will make unencoded URLs
     * pass through unaltered.
     *
     * @param url
     *            The URL to decode, may be {@code null}.
     * @return The decoded URL or {@code null} if the input was {@code null}.
     */
    static String decodeUrl(String url) {
        String decoded = url;
        if (url != null && url.indexOf('%') >= 0) {
            int n = url.length();
            StringBuffer buffer = new StringBuffer();
            ByteBuffer bytes = ByteBuffer.allocate(n);
            for (int i = 0; i < n;) {
                if (url.charAt(i) == '%') {
                    try {
                        do {
                            byte octet = (byte) Integer.parseInt(url.substring(i + 1, i + 3), 16);
                            bytes.put(octet);
                            i += 3;
                        } while (i < n && url.charAt(i) == '%');
                        continue;
                    } catch (RuntimeException e) {
                        // malformed percent-encoded octet, fall through and
                        // append characters literally
                    } finally {
                        if (bytes.position() > 0) {
                            bytes.flip();
                            buffer.append(UTF8.decode(bytes).toString());
                            bytes.clear();
                        }
                    }
                }
                buffer.append(url.charAt(i++));
            }
            decoded = buffer.toString();
        }
        return decoded;
    }

    /**
     * Converts each of an array of <code>URL</code> to a <code>File</code>.
     * <p>
     * Returns an array of the same size as the input. If the input is
     * {@code null}, an empty array is returned. If the input contains
     * {@code null}, the output array contains {@code null} at the same index.
     * <p>
     * This method will decode the URL. Syntax such as
     * <code>file:///my%20docs/file.txt</code> will be correctly decoded to
     * <code>/my docs/file.txt</code>.
     *
     * @param urls
     *            the file URLs to convert, {@code null} returns empty array
     * @return a non-{@code null} array of Files matching the input, with a
     *         {@code null} item if there was a {@code null} at that index in
     *         the input array
     * @throws IllegalArgumentException
     *             if any file is not a URL file
     * @throws IllegalArgumentException
     *             if any file is incorrectly encoded
     *
     */
    public static File[] toFiles(URL[] urls) throws IllegalArgumentException{
        if (urls == null || urls.length == 0) {
            return EMPTY_FILE_ARRAY;
        }
        File[] files = new File[urls.length];
        for (int i = 0; i < urls.length; i++) {
            URL url = urls[i];
            if (url != null) {
                if ("file".equals(url.getProtocol()) == false) {
                    throw new IllegalArgumentException("URL could not be converted to a File: " + url);
                }
                files[i] = toFile(url);
            }
        }

        return files;
    }

    /**
     * Converts each of an array of <code>File</code> to a <code>URL</code>.
     * <p>
     * Returns an array of the same size as the input.
     *
     * @param files
     *            the files to convert, must not be {@code null}
     * @return an array of URLs matching the input
     * @throws IOException
     *             if a file cannot be converted
     * @throws NullPointerException
     *             if the parameter is null
     */
    public static URL[] toURLs(File[] files) throws IOException {
        if (files == null) {
            throw new NullPointerException("files must not be null");
        }
        URL[] urls = new URL[files.length];

        for (int i = 0; i < urls.length; i++) {
            urls[i] = files[i].toURI().toURL();
        }
        return urls;
    }
    // -----------------------------------------------------------------------
    /**
     * Copies a file to a directory preserving the file date.
     * <p>
     * This method copies the contents of the specified source file to a file of
     * the same name in the specified destination directory. The destination
     * directory is created if it does not exist. If the destination file
     * exists, then this method will overwrite it.
     * <p>
     * <strong>Note:</strong> This method tries to preserve the file's last
     * modified date/times using {@link File#setLastModified(long)}, however it
     * is not guaranteed that the operation will succeed. If the modification
     * operation fails, no indication is provided.
     *
     * @param srcFile
     *            an existing file to copy, must not be {@code null}
     * @param destDir
     *            the directory to place the copy in, must not be {@code null}
     *
     * @throws NullPointerException
     *             if source or destination is null
     * @throws IllegalArgumentException
     *             if source or destination is invalid
     * @throws IOException
     *             if an IO error occurs during copying
     * @see #copyFile(File, File, boolean)
     */
    public static void copyFileToDirectory(File srcFile, File destDir) throws IOException {
        copyFileToDirectory(srcFile, destDir, true);
    }

    /**
     * Copies a file to a directory optionally preserving the file date.
     * <p>
     * This method copies the contents of the specified source file to a file of
     * the same name in the specified destination directory. The destination
     * directory is created if it does not exist. If the destination file
     * exists, then this method will overwrite it.
     * <p>
     * <strong>Note:</strong> Setting <code>preserveFileDate</code> to
     * {@code true} tries to preserve the file's last modified date/times using
     * {@link File#setLastModified(long)}, however it is not guaranteed that the
     * operation will succeed. If the modification operation fails, no
     * indication is provided.
     *
     * @param srcFile
     *            an existing file to copy, must not be {@code null}
     * @param destDir
     *            the directory to place the copy in, must not be {@code null}
     * @param preserveFileDate
     *            true if the file date of the copy should be the same as the
     *            original
     *
     * @throws NullPointerException
     *             if source or destination is {@code null}
     * @throws IllegalArgumentException
     *             if source or destination is invalid
     * @throws IOException
     *             if an IO error occurs during copying
     * @see #copyFile(File, File, boolean)
     * @since 1.3
     */
    public static void copyFileToDirectory(File srcFile, File destDir, boolean preserveFileDate) throws IOException {
        if (srcFile == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (destDir == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (destDir.exists() && destDir.isDirectory() == false) {
            throw new IllegalArgumentException("Destination '" + destDir + "' is not a directory");
        }
        File destFile = new File(destDir, srcFile.getName());
        copyFile(srcFile, destFile, preserveFileDate);
    }

    /**
     * Copies a file to a new location preserving the file date.
     * <p>
     * This method copies the contents of the specified source file to the
     * specified destination file. The directory holding the destination file is
     * created if it does not exist. If the destination file exists, then this
     * method will overwrite it.
     * <p>
     * <strong>Note:</strong> This method tries to preserve the file's last
     * modified date/times using {@link File#setLastModified(long)}, however it
     * is not guaranteed that the operation will succeed. If the modification
     * operation fails, no indication is provided.
     *
     * @param srcFile
     *            an existing file to copy, must not be {@code null}
     * @param destFile
     *            the new file, must not be {@code null}
     *
     * @throws NullPointerException
     *             if source or destination is {@code null}
     * @throws IllegalArgumentException
     *             if source or destination is invalid
     * @throws IOException
     *             if an IO error occurs during copying
     * @see #copyFileToDirectory(File, File)
     */
    public static void copyFile(File srcFile, File destFile) throws IOException {
        copyFile(srcFile, destFile, true);
    }

    /**
     * Copies a file to a new location.
     * <p>
     * This method copies the contents of the specified source file to the
     * specified destination file. The directory holding the destination file is
     * created if it does not exist. If the destination file exists, then this
     * method will overwrite it.
     * <p>
     * <strong>Note:</strong> Setting <code>preserveFileDate</code> to
     * {@code true} tries to preserve the file's last modified date/times using
     * {@link File#setLastModified(long)}, however it is not guaranteed that the
     * operation will succeed. If the modification operation fails, no
     * indication is provided.
     *
     * @param srcFile
     *            an existing file to copy, must not be {@code null}
     * @param destFile
     *            the new file, must not be {@code null}
     * @param preserveFileDate
     *            true if the file date of the copy should be the same as the
     *            original
     *
     * @throws NullPointerException
     *             if source or destination is {@code null}
     * @throws IllegalArgumentException
     *             if source or destination is invalid
     * @throws IOException
     *             if an IO error occurs during copying
     * @see #copyFileToDirectory(File, File, boolean)
     */
    public static void copyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
        if (srcFile == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (destFile == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (srcFile.exists() == false) {
            throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
        }
        if (srcFile.isDirectory()) {
            throw new IllegalArgumentException("Source '" + srcFile + "' exists but is a directory");
        }
        if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
            throw new IllegalArgumentException("Source '" + srcFile + "' and destination '" + destFile + "' are the same");
        }
        File parentFile = destFile.getParentFile();
        if (parentFile != null) {
            if (!parentFile.mkdirs() && !parentFile.isDirectory()) {
                throw new IllegalArgumentException("Destination '" + parentFile + "' directory cannot be created");
            }
        }
        if (destFile.exists() && destFile.canWrite() == false) {
            throw new IllegalArgumentException("Destination '" + destFile + "' exists but is read-only");
        }
        doCopyFile(srcFile, destFile, preserveFileDate);
    }
    /**
     * Copy bytes from a <code>File</code> to an <code>OutputStream</code>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     * </p>
     *
     * @param input
     *            the <code>File</code> to read from
     * @param output
     *            the <code>OutputStream</code> to write to
     * @return the number of bytes copied
     * @throws NullPointerException
     *             if the input or output is null
     * @throws IOException
     *             if an I/O error occurs
     * @since 2.1
     */
    public static long copyFile(File input, OutputStream output) throws IOException {
        final FileInputStream fis = new FileInputStream(input);
        try {
            return IOUtils.copyLarge(fis, output);
        } finally {
            fis.close();
        }
    }

    /**
     * Internal copy file method.
     *
     * @param srcFile
     *            the validated source file, must not be {@code null}
     * @param destFile
     *            the validated destination file, must not be {@code null}
     * @param preserveFileDate
     *            whether to preserve the file date
     * @throws IOException
     *             if an error occurs
     */
    private static void doCopyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
        if (destFile.exists() && destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' exists but is a directory");
        }

        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel input = null;
        FileChannel output = null;
        try {
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(destFile);
            input = fis.getChannel();
            output = fos.getChannel();
            long size = input.size();
            long pos = 0;
            long count = 0;
            while (pos < size) {
                count = size - pos > FILE_COPY_BUFFER_SIZE ? FILE_COPY_BUFFER_SIZE : size - pos;
                pos += output.transferFrom(input, pos, count);
            }
        } catch (Error e) {
            // LT18i 定制有问题，java sdk找不到方法，增加下保护。
            throw new IOException("Failed to copy full contents from '" + srcFile + "' to '" + destFile + "'");
        } finally {
            IOUtils.closeQuietly(output);
            IOUtils.closeQuietly(fos);
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(fis);
        }

        if (srcFile.length() != destFile.length()) {
            throw new IOException("Failed to copy full contents from '" + srcFile + "' to '" + destFile + "'");
        }
        if (preserveFileDate) {
            destFile.setLastModified(srcFile.lastModified());
        }
    }
    // -----------------------------------------------------------------------
    /**
     * Copies a directory to within another directory preserving the file dates.
     * <p>
     * This method copies the source directory and all its contents to a
     * directory of the same name in the specified destination directory.
     * <p>
     * The destination directory is created if it does not exist. If the
     * destination directory did exist, then this method merges the source with
     * the destination, with the source taking precedence.
     * <p>
     * <strong>Note:</strong> This method tries to preserve the files' last
     * modified date/times using {@link File#setLastModified(long)}, however it
     * is not guaranteed that those operations will succeed. If the modification
     * operation fails, no indication is provided.
     *
     * @param srcDir
     *            an existing directory to copy, must not be {@code null}
     * @param destDir
     *            the directory to place the copy in, must not be {@code null}
     *
     * @throws NullPointerException
     *             if source or destination is {@code null}
     * @throws IllegalArgumentException
     *             if source or destination is invalid
     * @throws IOException
     *             if an IO error occurs during copying
     * @since 1.2
     */
    public static void copyDirectoryToDirectory(File srcDir, File destDir) throws IOException {
        if (srcDir == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (srcDir.exists() && srcDir.isDirectory() == false) {
            throw new IllegalArgumentException("Source '" + destDir + "' is not a directory");
        }
        if (destDir == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (destDir.exists() && destDir.isDirectory() == false) {
            throw new IllegalArgumentException("Destination '" + destDir + "' is not a directory");
        }
        copyDirectory(srcDir, new File(destDir, srcDir.getName()), true);
    }

    /**
     * Copies a whole directory to a new location preserving the file dates.
     * <p>
     * This method copies the specified directory and all its child directories
     * and files to the specified destination. The destination is the new
     * location and name of the directory.
     * <p>
     * The destination directory is created if it does not exist. If the
     * destination directory did exist, then this method merges the source with
     * the destination, with the source taking precedence.
     * <p>
     * <strong>Note:</strong> This method tries to preserve the files' last
     * modified date/times using {@link File#setLastModified(long)}, however it
     * is not guaranteed that those operations will succeed. If the modification
     * operation fails, no indication is provided.
     *
     * @param srcDir
     *            an existing directory to copy, must not be {@code null}
     * @param destDir
     *            the new directory, must not be {@code null}
     *
     * @throws NullPointerException
     *             if source or destination is {@code null}
     * @throws IllegalArgumentException
     *             if source or destination is invalid
     * @throws IOException
     *             if an IO error occurs during copying
     * @since 1.1
     */
    public static void copyDirectory(File srcDir, File destDir) throws IOException {
        copyDirectory(srcDir, destDir, true);
    }

    /**
     * Copies a whole directory to a new location.
     * <p>
     * This method copies the contents of the specified source directory to
     * within the specified destination directory.
     * <p>
     * The destination directory is created if it does not exist. If the
     * destination directory did exist, then this method merges the source with
     * the destination, with the source taking precedence.
     * <p>
     * <strong>Note:</strong> Setting <code>preserveFileDate</code> to
     * {@code true} tries to preserve the files' last modified date/times using
     * {@link File#setLastModified(long)}, however it is not guaranteed that
     * those operations will succeed. If the modification operation fails, no
     * indication is provided.
     *
     * @param srcDir
     *            an existing directory to copy, must not be {@code null}
     * @param destDir
     *            the new directory, must not be {@code null}
     * @param preserveFileDate
     *            true if the file date of the copy should be the same as the
     *            original
     *
     * @throws NullPointerException
     *             if source or destination is {@code null}
     * @throws IllegalArgumentException
     *             if source or destination is invalid
     * @throws IOException
     *             if an IO error occurs during copying
     * @since 1.1
     */
    public static void copyDirectory(File srcDir, File destDir, boolean preserveFileDate) throws IOException {
        copyDirectory(srcDir, destDir, null, preserveFileDate);
    }

    /**
     * Copies a filtered directory to a new location preserving the file dates.
     * <p>
     * This method copies the contents of the specified source directory to
     * within the specified destination directory.
     * <p>
     * The destination directory is created if it does not exist. If the
     * destination directory did exist, then this method merges the source with
     * the destination, with the source taking precedence.
     * <p>
     * <strong>Note:</strong> This method tries to preserve the files' last
     * modified date/times using {@link File#setLastModified(long)}, however it
     * is not guaranteed that those operations will succeed. If the modification
     * operation fails, no indication is provided.
     *
     * <h4>Example: Copy directories only</h4>
     *
     * <pre>
     * // only copy the directory structure
     * FileUtils.copyDirectory(srcDir, destDir, DirectoryFileFilter.DIRECTORY);
     * </pre>
     *
     * <h4>Example: Copy directories and txt files</h4>
     *
     * <pre>
     * // Create a filter for &quot;.txt&quot; files
     * IOFileFilter txtSuffixFilter = FileFilterUtils.suffixFileFilter(&quot;.txt&quot;);
     * IOFileFilter txtFiles = FileFilterUtils.andFileFilter(FileFileFilter.FILE, txtSuffixFilter);
     *
     * // Create a filter for either directories or &quot;.txt&quot; files
     * FileFilter filter = FileFilterUtils.orFileFilter(DirectoryFileFilter.DIRECTORY, txtFiles);
     *
     * // Copy using the filter
     * FileUtils.copyDirectory(srcDir, destDir, filter);
     * </pre>
     *
     * @param srcDir
     *            an existing directory to copy, must not be {@code null}
     * @param destDir
     *            the new directory, must not be {@code null}
     * @param filter
     *            the filter to apply, null means copy all directories and files
     *            should be the same as the original
     *
     * @throws NullPointerException
     *             if source or destination is {@code null}
     * @throws IllegalArgumentException
     *             if source or destination is invalid
     * @throws IOException
     *             if an IO error occurs during copying
     * @since 1.4
     */
    public static void copyDirectory(File srcDir, File destDir, FileFilter filter) throws IOException {
        copyDirectory(srcDir, destDir, filter, true);
    }

    /**
     * Copies a filtered directory to a new location.
     * <p>
     * This method copies the contents of the specified source directory to
     * within the specified destination directory.
     * <p>
     * The destination directory is created if it does not exist. If the
     * destination directory did exist, then this method merges the source with
     * the destination, with the source taking precedence.
     * <p>
     * <strong>Note:</strong> Setting <code>preserveFileDate</code> to
     * {@code true} tries to preserve the files' last modified date/times using
     * {@link File#setLastModified(long)}, however it is not guaranteed that
     * those operations will succeed. If the modification operation fails, no
     * indication is provided.
     *
     * <h4>Example: Copy directories only</h4>
     *
     * <pre>
     * // only copy the directory structure
     * FileUtils.copyDirectory(srcDir, destDir, DirectoryFileFilter.DIRECTORY, false);
     * </pre>
     *
     * <h4>Example: Copy directories and txt files</h4>
     *
     * <pre>
     * // Create a filter for &quot;.txt&quot; files
     * IOFileFilter txtSuffixFilter = FileFilterUtils.suffixFileFilter(&quot;.txt&quot;);
     * IOFileFilter txtFiles = FileFilterUtils.andFileFilter(FileFileFilter.FILE, txtSuffixFilter);
     *
     * // Create a filter for either directories or &quot;.txt&quot; files
     * FileFilter filter = FileFilterUtils.orFileFilter(DirectoryFileFilter.DIRECTORY, txtFiles);
     *
     * // Copy using the filter
     * FileUtils.copyDirectory(srcDir, destDir, filter, false);
     * </pre>
     *
     * @param srcDir
     *            an existing directory to copy, must not be {@code null}
     * @param destDir
     *            the new directory, must not be {@code null}
     * @param filter
     *            the filter to apply, null means copy all directories and files
     * @param preserveFileDate
     *            true if the file date of the copy should be the same as the
     *            original
     *
     * @throws NullPointerException
     *             if source or destination is {@code null}
     * @throws IllegalArgumentException
     *             if source or destination is invalid
     * @throws IOException
     *             if an IO error occurs during copying
     * @since 1.4
     */
    public static void copyDirectory(File srcDir, File destDir, FileFilter filter, boolean preserveFileDate)
            throws IOException {
        if (srcDir == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (destDir == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (srcDir.exists() == false) {
            throw new FileNotFoundException("Source '" + srcDir + "' does not exist");
        }
        if (srcDir.isDirectory() == false) {
            throw new IllegalArgumentException("Source '" + srcDir + "' exists but is not a directory");
        }
        if (srcDir.getCanonicalPath().equals(destDir.getCanonicalPath())) {
            throw new IllegalArgumentException("Source '" + srcDir + "' and destination '" + destDir + "' are the same");
        }

        // Cater for destination being directory within the source directory
        // (see IO-141)
        List<String> exclusionList = null;
        if (destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath())) {
            File[] srcFiles = filter == null ? srcDir.listFiles() : srcDir.listFiles(filter);
            if (srcFiles != null && srcFiles.length > 0) {
                exclusionList = new ArrayList<String>(srcFiles.length);
                for (File srcFile : srcFiles) {
                    File copiedFile = new File(destDir, srcFile.getName());
                    exclusionList.add(copiedFile.getCanonicalPath());
                }
            }
        }
        doCopyDirectory(srcDir, destDir, filter, preserveFileDate, exclusionList);
    }

    /**
     * Internal copy directory method.
     *
     * @param srcDir
     *            the validated source directory, must not be {@code null}
     * @param destDir
     *            the validated destination directory, must not be {@code null}
     * @param filter
     *            the filter to apply, null means copy all directories and files
     * @param preserveFileDate
     *            whether to preserve the file date
     * @param exclusionList
     *            List of files and directories to exclude from the copy, may be
     *            null
     * @throws IOException
     *             if an error occurs
     * @since 1.1
     */
    private static void doCopyDirectory(File srcDir, File destDir, FileFilter filter, boolean preserveFileDate,
                                        List<String> exclusionList) throws IOException {
        // recurse
        File[] srcFiles = filter == null ? srcDir.listFiles() : srcDir.listFiles(filter);
        if (srcFiles == null) { // null if abstract pathname does not denote a
            // directory, or if an I/O error occurs
            throw new IOException("Failed to list contents of " + srcDir);
        }
        if (destDir.exists()) {
            if (destDir.isDirectory() == false) {
                throw new IOException("Destination '" + destDir + "' exists but is not a directory");
            }
        } else {
            if (!destDir.mkdirs() && !destDir.isDirectory()) {
                throw new IOException("Destination '" + destDir + "' directory cannot be created");
            }
        }
        if (destDir.canWrite() == false) {
            throw new IOException("Destination '" + destDir + "' cannot be written to");
        }
        for (File srcFile : srcFiles) {
            File dstFile = new File(destDir, srcFile.getName());
            if (exclusionList == null || !exclusionList.contains(srcFile.getCanonicalPath())) {
                if (srcFile.isDirectory()) {
                    doCopyDirectory(srcFile, dstFile, filter, preserveFileDate, exclusionList);
                } else {
                    doCopyFile(srcFile, dstFile, preserveFileDate);
                }
            }
        }

        // Do this last, as the above has probably affected directory metadata
        if (preserveFileDate) {
            destDir.setLastModified(srcDir.lastModified());
        }
    }
// -----------------------------------------------------------------------
    /**
     * Deletes a directory recursively.
     *
     * @param directory
     *            directory to delete, directory must not be {@code null}
     * @throws NullPointerException
     *             if directory  is {@code null}
     * @throws IllegalArgumentException
     *
     * @throws IOException
     *             in case deletion is unsuccessful
     */
    public static void deleteDirectory(File directory) throws IOException {
        if (directory == null) {
            throw new NullPointerException("directory must not be null");
        }

        if (!directory.exists()) {
            String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!isSymlink(directory)) {
            cleanDirectory(directory);
        }

        if (!directory.delete()) {
            String message = "Unable to delete directory " + directory + ".";
            throw new IOException(message);
        }
    }

    /**
     * Deletes a file, never throwing an exception. If file is a directory,
     * delete it and all sub-directories.
     * <p>
     * The difference between File.delete() and this method are:
     * <ul>
     * <li>A directory to be deleted does not have to be empty.</li>
     * <li>No exceptions are thrown when a file or directory cannot be deleted.</li>
     * </ul>
     *
     * @param file
     *            file or directory to delete, can be {@code null}
     * @return {@code true} if the file or directory was deleted, otherwise
     *         {@code false}
     *
     * @since 1.4
     */
    public static boolean deleteQuietly(File file) {
        if (file == null) {
            return false;
        }
        try {
            if (file.isDirectory()) {
                cleanDirectory(file);
            }
        } catch (Exception ignored) {
        }

        try {
            return file.delete();
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     * Cleans a directory without deleting it.
     *
     * @param directory
     *            directory to clean
     * @throws IOException
     *             in case cleaning is unsuccessful
     */
    public static void cleanDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()) {
            String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }

        File[] files = directory.listFiles();
        if (files == null) { // null if security restricted
            throw new IOException("Failed to list contents of " + directory);
        }

        IOException exception = null;
        for (File file : files) {
            try {
                forceDelete(file);
            } catch (IOException ioe) {
                exception = ioe;
            }
        }

        if (null != exception) {
            throw exception;
        }
    }
    /**
     * Deletes a file. If file is a directory, delete it and all
     * sub-directories.
     * <p>
     * The difference between File.delete() and this method are:
     * <ul>
     * <li>A directory to be deleted does not have to be empty.</li>
     * <li>You get exceptions when a file or directory cannot be deleted.
     * (java.io.File methods returns a boolean)</li>
     * </ul>
     *
     * @param file
     *            file or directory to delete, must not be {@code null}
     * @throws NullPointerException
     *             if the directory is {@code null}
     * @throws FileNotFoundException
     *             if the file was not found
     * @throws IOException
     *             in case deletion is unsuccessful
     */
    public static void forceDelete(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectory(file);
        } else {
            boolean filePresent = file.exists();
            if (!file.delete()) {
                if (!filePresent) {
                    throw new FileNotFoundException("File does not exist: " + file);
                }
                String message = "Unable to delete file: " + file;
                throw new IOException(message);
            }
        }
    }

    /**
     * Schedules a file to be deleted when JVM exits. If file is directory
     * delete it and all sub-directories.
     *
     * @param file
     *            file or directory to delete, must not be {@code null}
     * @throws NullPointerException
     *             if the file is {@code null}
     * @throws IOException
     *             in case deletion is unsuccessful
     */
    public static void forceDeleteOnExit(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectoryOnExit(file);
        } else {
            file.deleteOnExit();
        }
    }

    /**
     * Schedules a directory recursively for deletion on JVM exit.
     *
     * @param directory
     *            directory to delete, must not be {@code null}
     * @throws NullPointerException
     *             if the directory is {@code null}
     * @throws IOException
     *             in case deletion is unsuccessful
     */
    private static void deleteDirectoryOnExit(File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }

        directory.deleteOnExit();
        if (!isSymlink(directory)) {
            cleanDirectoryOnExit(directory);
        }
    }

    /**
     * Cleans a directory without deleting it.
     *
     * @param directory
     *            directory to clean, must not be {@code null}
     * @throws NullPointerException
     *             if the directory is {@code null}
     * @throws IOException
     *             in case cleaning is unsuccessful
     */
    private static void cleanDirectoryOnExit(File directory) throws IOException {
        if (!directory.exists()) {
            String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()) {
            String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }

        File[] files = directory.listFiles();
        if (files == null) { // null if security restricted
            throw new IOException("Failed to list contents of " + directory);
        }

        IOException exception = null;
        for (File file : files) {
            try {
                forceDeleteOnExit(file);
            } catch (IOException ioe) {
                exception = ioe;
            }
        }

        if (null != exception) {
            throw exception;
        }
    }

    /**
     * Determines whether the specified file is a Symbolic Link rather than an
     * actual file.
     * <p>
     * Will not return true if there is a Symbolic Link anywhere in the path,
     * only if the specific file is.
     * <p>
     * <b>Note:</b> the current implementation always returns {@code false} if
     * the system is detected as Windows using
     * {@link FilenameUtils#isSystemWindows()}
     *
     * @param file
     *            the file to check
     * @return true if the file is a Symbolic Link
     * @throws IOException
     *             if an IO error occurs while checking the file
     * @since 2.0
     */
    public static boolean isSymlink(File file) throws IOException {
        if (file == null) {
            throw new NullPointerException("File must not be null");
        }
        if (FilenameUtils.isSystemWindows()) {
            return false;
        }
        File fileInCanonicalDir = null;
        if (file.getParent() == null) {
            fileInCanonicalDir = file;
        } else {
            File canonicalDir = file.getParentFile().getCanonicalFile();
            fileInCanonicalDir = new File(canonicalDir, file.getName());
        }

        if (fileInCanonicalDir.getCanonicalFile().equals(fileInCanonicalDir.getAbsoluteFile())) {
            return false;
        } else {
            return true;
        }
    }
    //--------------------------------------------------------------------------------
    /**
     * Moves a directory.
     * <p>
     * When the destination directory is on another file system, do a
     * "copy and delete".
     *
     * @param srcDir
     *            the directory to be moved
     * @param destDir
     *            the destination directory
     * @throws NullPointerException
     *             if source or destination is {@code null}
     * @throws FileExistsException
     *             if the destination directory exists
     * @throws IllegalArgumentException
     *             if source or destination is invalid
     * @throws IOException
     *             if an IO error occurs moving the file
     * @since 1.4
     */
    public static void moveDirectory(File srcDir, File destDir) throws IOException {
        if (srcDir == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (destDir == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (!srcDir.exists()) {
            throw new FileNotFoundException("Source '" + srcDir + "' does not exist");
        }
        if (!srcDir.isDirectory()) {
            throw new IllegalArgumentException("Source '" + srcDir + "' is not a directory");
        }
        if (destDir.exists()) {
            throw new FileExistsException("Destination '" + destDir + "' already exists");
        }
        boolean rename = srcDir.renameTo(destDir);
        if (!rename) {
            if (destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath())) {
                throw new IOException("Cannot move directory: " + srcDir + " to a subdirectory of itself: " + destDir);
            }
            copyDirectory(srcDir, destDir);
            deleteDirectory(srcDir);
            if (srcDir.exists()) {
                throw new IOException("Failed to delete original directory '" + srcDir + "' after copy to '" + destDir
                        + "'");
            }
        }
    }

    /**
     * Moves a directory to another directory.
     *
     * @param src
     *            the file to be moved
     * @param destDir
     *            the destination file
     * @param createDestDir
     *            If {@code true} create the destination directory, otherwise if
     *            {@code false} throw an IOException
     * @throws NullPointerException
     *             if source or destination is {@code null}
     * @throws FileExistsException
     *             if the directory exists in the destination directory
     * @throws IOException
     *             if source or destination is invalid
     * @throws IOException
     *             if an IO error occurs moving the file
     * @since 1.4
     */
    public static void moveDirectoryToDirectory(File src, File destDir, boolean createDestDir) throws IOException {
        if (src == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (destDir == null) {
            throw new NullPointerException("Destination directory must not be null");
        }
        if (!destDir.exists() && createDestDir) {
            destDir.mkdirs();
        }
        if (!destDir.exists()) {
            throw new FileNotFoundException("Destination directory '" + destDir + "' does not exist [createDestDir="
                    + createDestDir + "]");
        }
        if (!destDir.isDirectory()) {
            throw new IOException("Destination '" + destDir + "' is not a directory");
        }
        moveDirectory(src, new File(destDir, src.getName()));

    }

    /**
     * Moves a file.
     * <p>
     * When the destination file is on another file system, do a
     * "copy and delete".
     *
     * @param srcFile
     *            the file to be moved
     * @param destFile
     *            the destination file
     * @throws NullPointerException
     *             if source or destination is {@code null}
     * @throws FileExistsException
     *             if the destination file exists
     * @throws IOException
     *             if source or destination is invalid
     * @throws IOException
     *             if an IO error occurs moving the file
     * @since 1.4
     */
    public static void moveFile(File srcFile, File destFile) throws IOException {
        if (srcFile == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (destFile == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (!srcFile.exists()) {
            throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
        }
        if (srcFile.isDirectory()) {
            throw new IOException("Source '" + srcFile + "' is a directory");
        }
        if (destFile.exists()) {
            throw new FileExistsException("Destination '" + destFile + "' already exists");
        }
        if (destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' is a directory");
        }
        boolean rename = srcFile.renameTo(destFile);
        if (!rename) {
            copyFile(srcFile, destFile);
            if (!srcFile.delete()) {
                FileUtils.deleteQuietly(destFile);
                throw new IOException("Failed to delete original file '" + srcFile + "' after copy to '" + destFile
                        + "'");
            }
        }
    }

    /**
     * Moves a file to a directory.
     *
     * @param srcFile
     *            the file to be moved
     * @param destDir
     *            the destination file
     * @param createDestDir
     *            If {@code true} create the destination directory, otherwise if
     *            {@code false} throw an IOException
     * @throws NullPointerException
     *             if source or destination is {@code null}
     * @throws FileExistsException
     *             if the destination file exists
     * @throws IOException
     *             if source or destination is invalid
     * @throws IOException
     *             if an IO error occurs moving the file
     * @since 1.4
     */
    public static void moveFileToDirectory(File srcFile, File destDir, boolean createDestDir) throws IOException {
        if (srcFile == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (destDir == null) {
            throw new NullPointerException("Destination directory must not be null");
        }
        if (!destDir.exists() && createDestDir) {
            destDir.mkdirs();
        }
        if (!destDir.exists()) {
            throw new FileNotFoundException("Destination directory '" + destDir + "' does not exist [createDestDir="
                    + createDestDir + "]");
        }
        if (!destDir.isDirectory()) {
            throw new IOException("Destination '" + destDir + "' is not a directory");
        }
        moveFile(srcFile, new File(destDir, srcFile.getName()));
    }

    /**
     * Moves a file or directory to the destination directory.
     * <p>
     * When the destination is on another file system, do a "copy and delete".
     *
     * @param src
     *            the file or directory to be moved
     * @param destDir
     *            the destination directory
     * @param createDestDir
     *            If {@code true} create the destination directory, otherwise if
     *            {@code false} throw an IOException
     * @throws NullPointerException
     *             if source or destination is {@code null}
     * @throws FileExistsException
     *             if the directory or file exists in the destination directory
     * @throws IOException
     *             if source or destination is invalid
     * @throws IOException
     *             if an IO error occurs moving the file
     * @since 1.4
     */
    public static void moveToDirectory(File src, File destDir, boolean createDestDir) throws IOException {
        if (src == null) {
            throw new NullPointerException("Source must not be null");
        }
        if (destDir == null) {
            throw new NullPointerException("Destination must not be null");
        }
        if (!src.exists()) {
            throw new FileNotFoundException("Source '" + src + "' does not exist");
        }
        if (src.isDirectory()) {
            moveDirectoryToDirectory(src, destDir, createDestDir);
        } else {
            moveFileToDirectory(src, destDir, createDestDir);
        }
    }


}
