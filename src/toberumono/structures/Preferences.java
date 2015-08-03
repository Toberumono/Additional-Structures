package toberumono.structures;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * A data structure for storing simple configuration data.<br>
 * This structure is one-dimensional - it does not allow for nesting. However, it simple to edit, so it is good for when
 * end-users need to be able to quickly edit a configuration file.<br>
 * The structure is as follows:<br>
 * Sections are defined by lines that end with a colon (':').<br>
 * Values are defined by lines that do not end with a colon.<br>
 * A section can have multiple values - just add additional lines.<br>
 * Comments are prefixed with a '#' sign, and always end the line that they are on.<br>
 * 
 * @author Toberumono
 */
public class Preferences extends TreeMap<String, List<String>> {
	
	/**
	 * Creates an empty {@link Preferences} instance.
	 */
	public Preferences() {
		super();
	}
	
	/**
	 * Creates a {@link Preferences} instance with the given initial fields.
	 * 
	 * @param init
	 *            the initial fields
	 */
	public Preferences(Map<String, List<String>> init) {
		super(init);
	}
	
	/**
	 * Loads the given <tt>text</tt> into a {@link Preferences} object.
	 * 
	 * @param text
	 *            the text to load
	 * @return a {@link Preferences} object representing the given <tt>text</tt>
	 */
	public static Preferences read(String text) {
		return read(Arrays.asList(text.split(System.lineSeparator())).stream());
	}
	
	/**
	 * Converts the {@link Stream} of <tt>lines</tt> of a preferences file into a {@link Preferences} object.
	 * 
	 * @param lines
	 *            a {@link Stream} of lines (e.g. from {@link List#stream()})
	 * @return a {@link Preferences} object representing the given <tt>lines</tt>
	 * @see Arrays#asList(Object...)
	 */
	public static Preferences read(Stream<String> lines) {
		Preferences out = new Preferences();
		lines.forEachOrdered(new Consumer<String>() {
			List<String> head = null;
			
			@Override
			public void accept(String line) {
				line = line.trim();
				int commentIndex = line.indexOf('#');
				if (commentIndex > -1)
					line = line.substring(0, commentIndex).trim();
				else
					line = line.trim();
				if (line.endsWith(":")) {
					head = new ArrayList<>();
					out.put(line.substring(0, line.length() - 1), head);
				}
				else
					head.add(line);
			}
		});
		return out;
	}
	
	/**
	 * Loads a {@link Preferences} object from the given {@link Path}.
	 * 
	 * @param file
	 *            a {@link Path} to the file from which to load the {@link Preferences} object
	 * @return a {@link Preferences} object representing the given <tt>file</tt>
	 * @throws IOException
	 *             if the file could not be read
	 */
	public static Preferences read(Path file) throws IOException {
		return read(Files.lines(file));
	}
	
	/**
	 * Writes a {@link Preferences} object to a file. This will completely overwrite the file specified by <tt>file</tt> if
	 * it exists.
	 * 
	 * @param prefs
	 *            a {@link Preferences} object to write
	 * @param file
	 *            a {@link Path} the file to which the {@link Preferences} object should be written
	 * @throws IOException
	 *             if the file could not be opened and written to
	 */
	public static void write(Preferences prefs, Path file) throws IOException {
		List<String> lines = new ArrayList<>();
		for (Map.Entry<String, List<String>> key : prefs.entrySet()) {
			lines.add(key.getKey() + ":");
			for (String value : key.getValue())
				lines.add(value);
		}
		Files.write(file, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
	}
}
