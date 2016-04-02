package toberumono.structures.versioning;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A container for storing and working with version numbers that comply with the <a href="http://semver.org/">Semantic
 * Versioning 2.0.0</a> specification.<br>
 * {@link VersionNumber} is immutable for all practical purposes (the lazily computed fields are not considered for any comparison methods) 
 * 
 * @author Toberumono
 */
public class VersionNumber implements Comparable<VersionNumber>, Serializable, Cloneable {
	private static final Pattern versionSplitter = Pattern.compile(".", Pattern.LITERAL), validElement = Pattern.compile("[0-9A-Za-z\\-]+"), validNumeric = Pattern.compile("[0-9]+");
	private static final Pattern strictVersionPattern = Pattern.compile( //group 4 = prerelease, group 9 = metadata
			"(0|[1-9][0-9]*)\\.(0|[1-9][0-9]*)\\.(0|[1-9][0-9]*)(\\-((0|[1-9][0-9]*|[0-9A-Za-z\\-]+)(\\.(0|[1-9][0-9]*|[0-9A-Za-z\\-]+))*))?(\\+([0-9A-Za-z\\-]+(\\.[0-9A-Za-z\\-]+)*))?");
	private static final Pattern looseVersionPattern = //group 6 = prerelease, group 11 = metadata
			Pattern.compile("([0-9]+)(\\.([0-9]+)(\\.([0-9]+))?)?(\\-(([0-9A-Za-z\\-]+)(\\.([0-9A-Za-z\\-]+))*))?(\\+([0-9A-Za-z\\-]+(\\.[0-9A-Za-z\\-]+)*))?");
	private final int[] version;
	private final String[] prerelease, metadata;
	private transient List<String> unmodifiablePrerelease, unmodifiableMetadata;
	private int[] prereleaseNumbers;
	private String representation;
	private int hash;
	
	/**
	 * Constructs a new {@link VersionNumber} from a {@link String} using loose parsing.<br>
	 * Loose parsing uses a pattern that allows for leading zeros in numeric elements and makes the minor and patch version
	 * numbers optional (they default to 0).
	 * 
	 * @param version
	 *            the {@link String} on which the {@link VersionNumber} will be based
	 * @see #VersionNumber(String, boolean)
	 */
	public VersionNumber(String version) {
		this(version, true);
	}
	
	/**
	 * Constructs a new {@link VersionNumber} from a {@link String}.<br>
	 * Loose parsing uses a pattern that allows for leading zeros in numeric elements and makes the minor and patch version
	 * numbers optional (they default to 0).
	 * 
	 * @param version
	 *            the {@link String} on which the {@link VersionNumber} will be based
	 * @param looseParsing
	 *            if {@code true}, the parser will clean up the input {@link String} before parsing it
	 * @see #VersionNumber(String)
	 */
	public VersionNumber(String version, boolean looseParsing) {
		if (looseParsing)
			version = version.replaceAll("\\s", "");
		Matcher m = (looseParsing ? looseVersionPattern : strictVersionPattern).matcher(version);
		if (!m.matches())
			throw new InvalidVersionFormatException();
		if (looseParsing) {
			this.version = new int[]{Integer.parseInt(m.group(1)), m.group(3) == null ? 0 : Integer.parseInt(m.group(3)), m.group(5) == null ? 0 : Integer.parseInt(m.group(5))};
			prerelease = m.group(6) == null ? new String[0] : versionSplitter.split(m.group(6));
			metadata = m.group(11) == null ? new String[0] : versionSplitter.split(m.group(11));
		}
		else {
			this.version = new int[]{Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3))};
			prerelease = m.group(4) == null ? new String[0] : versionSplitter.split(m.group(4));
			metadata = m.group(9) == null ? new String[0] : versionSplitter.split(m.group(9));
		}
		prereleaseNumbers = null;
		representation = null;
		unmodifiablePrerelease = null;
		unmodifiableMetadata = null;
	}
	
	/**
	 * Constructs a new {@link VersionNumber} with the given fields.<br>
	 * Forwards to {@link #VersionNumber(int, int, int, String[]) VersionNumber(major, minor, patch, null)}
	 * 
	 * @param major
	 *            the major version number; must be an integer greater than or equal to 0
	 * @param minor
	 *            the minor version number; must be an integer greater than or equal to 0
	 * @param patch
	 *            the patch version number; must be an integer greater than or equal to 0
	 * @throws InvalidVersionFormatException
	 *             if {@code major}, {@code minor}, or {@code patch} are less than 0
	 * @see #VersionNumber(int, int, int, String[])
	 * @see #VersionNumber(int, int, int, String[], String[])
	 */
	public VersionNumber(int major, int minor, int patch) {
		this(major, minor, patch, null);
	}
	
	/**
	 * Constructs a new {@link VersionNumber} with the given fields.<br>
	 * Forwards to {@link #VersionNumber(int, int, int, String[], String[]) VersionNumber(major, minor, patch, prerelease,
	 * null)}
	 * 
	 * @param major
	 *            the major version number; must be an integer greater than or equal to 0
	 * @param minor
	 *            the minor version number; must be an integer greater than or equal to 0
	 * @param patch
	 *            the patch version number; must be an integer greater than or equal to 0
	 * @param prerelease
	 *            the prerelease values; the array can be {@code null} (indicating no prerelease section), but individual
	 *            elements cannot be {@code null}
	 * @throws InvalidVersionFormatException
	 *             if {@code major}, {@code minor}, or {@code patch} are less than 0
	 * @see #VersionNumber(int, int, int)
	 * @see #VersionNumber(int, int, int, String[], String[])
	 */
	public VersionNumber(int major, int minor, int patch, String[] prerelease) {
		this(major, minor, patch, prerelease, null);
	}
	
	/**
	 * Constructs a new {@link VersionNumber} with the given fields.
	 * 
	 * @param major
	 *            the major version number; must be an integer greater than or equal to 0
	 * @param minor
	 *            the minor version number; must be an integer greater than or equal to 0
	 * @param patch
	 *            the patch version number; must be an integer greater than or equal to 0
	 * @param prerelease
	 *            the prerelease values; the array can be {@code null} (indicating no prerelease section), but individual
	 *            elements cannot be {@code null}
	 * @param metadata
	 *            the metadata elements; the array can be {@code null} (indicating no metadata section), but individual
	 *            elements cannot be {@code null}
	 * @throws InvalidVersionFormatException
	 *             if {@code major}, {@code minor}, or {@code patch} are less than 0
	 * @see #VersionNumber(int, int, int, String[])
	 * @see #VersionNumber(int, int, int, String[], String[])
	 */
	public VersionNumber(int major, int minor, int patch, String[] prerelease, String[] metadata) {
		if (major < 0)
			throw new InvalidVersionFormatException("The major version cannot be less than 0");
		if (minor < 0)
			throw new InvalidVersionFormatException("The minor version cannot be less than 0");
		if (patch < 0)
			throw new InvalidVersionFormatException("The patch version cannot be less than 0");
		version = new int[]{major, minor, patch};
		if (prerelease != null) {
			for (String str : prerelease)
				if (str == null || !validElement.matcher(str).matches())
					throw new InvalidVersionFormatException(str + " is not a valid prerelease value");
			this.prerelease = prerelease == null ? new String[0] : Arrays.copyOf(prerelease, prerelease.length);
		}
		else
			this.prerelease = new String[0];
		if (metadata != null) {
			for (String str : metadata)
				if (str == null || !validElement.matcher(str).matches())
					throw new InvalidVersionFormatException(str + " is not a valid metadata value");
			this.metadata = metadata == null ? new String[0] : Arrays.copyOf(metadata, metadata.length);
		}
		else
			this.metadata = new String[0];
		prereleaseNumbers = null;
		representation = null;
		unmodifiablePrerelease = null;
		unmodifiableMetadata = null;
	}
	
	/**
	 * Computes and caches the numeric versions of applicable prerelease values and cleans up the {@link String} versions of
	 * those values (removes leading zeroes).
	 */
	private void cachePrereleaseNumbers() {
		if (prereleaseNumbers != null)
			return;
		prereleaseNumbers = new int[prerelease.length];
		for (int i = 0; i < prerelease.length; i++) {
			if (validNumeric.matcher(prerelease[i]).matches()) { //Enforces that all numbers are positive (as per the spec on semver.org)
				prereleaseNumbers[i] = Integer.parseInt(prerelease[i]);
				prerelease[i] = String.valueOf(prereleaseNumbers[i]); //This automatically strips any leading zeroes in numeric fields
			}
		}
	}
	
	/**
	 * Computes and caches the syntactically correct {@link String} representation of the {@link VersionNumber}.
	 */
	private void cacheSyntacticallyCorrectString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.version[0]).append('.').append(this.version[1]).append('.').append(this.version[2]);
		if (prerelease.length > 0) {
			cachePrereleaseNumbers();
			appendArray(sb.append('-'), prerelease);
		}
		if (metadata.length > 0)
			appendArray(sb.append('+'), metadata);
		representation = sb.toString();
	}
	
	private static <T> void appendArray(StringBuilder sb, T[] array) {
		for (int i = 0, lim = array.length - 1; i < lim; i++)
			sb.append(array[i]).append('.');
		sb.append(array[array.length - 1]); //Don't append a '.' after the last element
	}
	
	/**
	 * @return the major version
	 */
	public int getMajor() {
		return version[0];
	}
	
	/**
	 * @return the minor version
	 */
	public int getMinor() {
		return version[1];
	}
	
	/**
	 * @return the patch version
	 */
	public int getPatch() {
		return version[2];
	}
	
	/**
	 * @return an <i>unmodifiable</i> {@link List} view of the {@link VersionNumber VersionNumber's} prerelease section. If
	 *         the {@link VersionNumber} did not have a prerelease section, the {@link List} will be empty
	 */
	public List<String> getPrerelease() {
		if (unmodifiablePrerelease == null) {
			cachePrereleaseNumbers();
			unmodifiablePrerelease = Collections.unmodifiableList(Arrays.asList(prerelease));
		}
		return unmodifiablePrerelease;
	}
	
	/**
	 * @return an <i>unmodifiable</i> {@link List} view of the {@link VersionNumber VersionNumber's} metadata section. If the
	 *         {@link VersionNumber} did not have a metadata section, the {@link List} will be empty
	 */
	public List<String> getMetadata() {
		if (unmodifiableMetadata == null)
			unmodifiableMetadata = Collections.unmodifiableList(Arrays.asList(metadata));
		return unmodifiableMetadata;
	}
	
	/**
	 * Compares the {@link #getMajor() major} version of the {@link VersionNumber} on which this method was called and the
	 * {@code other} {@link VersionNumber}.
	 * 
	 * @param other
	 *            the {@link VersionNumber} to which the {@link VersionNumber} on which this method was called will be
	 *            compared
	 * @return {@code true} iff {@code other} has the same {@link #getMajor() major} version as the {@link VersionNumber} on
	 *         which this method was called
	 */
	public boolean hasSameMajorVersion(VersionNumber other) {
		return getMajor() == other.getMajor();
	}
	
	/**
	 * Compares the {@link #getMajor() major} and {@link #getMinor() minor} version of the {@link VersionNumber} on which
	 * this method was called and the {@code other} {@link VersionNumber}.
	 * 
	 * @param other
	 *            the {@link VersionNumber} to which the {@link VersionNumber} on which this method was called will be
	 *            compared
	 * @return {@code true} iff {@code other} has the same {@link #getMajor() major} and {@link #getMinor() minor} version as
	 *         the {@link VersionNumber} on which this method was called
	 */
	public boolean hasSameMinorVersion(VersionNumber other) {
		return getMajor() == other.getMajor() && getMinor() == other.getMinor();
	}
	
	/**
	 * Compares the {@link #getMajor() major}, {@link #getMinor() minor}, and {@link #getPatch() patch} version of the
	 * {@link VersionNumber} on which this method was called and the {@code other} {@link VersionNumber}.
	 * 
	 * @param other
	 *            the {@link VersionNumber} to which the {@link VersionNumber} on which this method was called will be
	 *            compared
	 * @return {@code true} iff {@code other} has the same {@link #getMajor() major}, {@link #getMinor() minor}, and
	 *         {@link #getPatch()} version as the {@link VersionNumber} on which this method was called
	 */
	public boolean hasSamePatchVersion(VersionNumber other) {
		return getMajor() == other.getMajor() && getMinor() == other.getMinor() && getPatch() == other.getPatch();
	}
	
	/**
	 * Compares the {@link #getMajor() major}, {@link #getMinor() minor}, {@link #getPatch() patch}, and
	 * {@link #getPrerelease() prerelease} version of the {@link VersionNumber} on which this method was called and the
	 * {@code other} {@link VersionNumber}.
	 * 
	 * @param other
	 *            the {@link VersionNumber} to which the {@link VersionNumber} on which this method was called will be
	 *            compared
	 * @return {@code true} iff {@code other} has the same {@link #getMajor() major}, {@link #getMinor() minor},
	 *         {@link #getPatch()}, and {@link #getPrerelease() prerelease} version as the {@link VersionNumber} on which
	 *         this method was called
	 */
	public boolean hasSamePrereleaseVersion(VersionNumber other) {
		if (getMajor() != other.getMajor() || getMinor() != other.getMinor() || getPatch() != other.getPatch())
			return false;
		if (prerelease.length != other.prerelease.length)
			return false;
		cachePrereleaseNumbers();
		other.cachePrereleaseNumbers();
		for (int i = 0; i < prerelease.length; i++)
			if (prereleaseNumbers[i] < 0 ? !prerelease[i].equals(other.prerelease[i]) : prereleaseNumbers[i] != other.prereleaseNumbers[i])
				return false;
		return true;
	}
	
	/**
	 * Compares the {@link VersionNumber} to the other {@link VersionNumber} ({@code o}) by their {@link #getMajor() major},
	 * {@link #getMinor() minor}, and {@link #getPatch() patch} versions. If no differences are found, the prerelease values
	 * are compared. If no differences are found in the prerelease values, the {@link VersionNumber} with less prerelease
	 * values is considered to be greater.<br>
	 * As per <a href="http://semver.org/">Semantic Versioning 2.0.0</a> specification, <i>the metadata values are <b>not</b>
	 * considered for comparison.</i>
	 */
	@Override
	public int compareTo(VersionNumber o) {
		int comp = 0;
		for (int i = 0; i < version.length; i++) {
			comp = Integer.compare(version[i], o.version[i]);
			if (comp != 0)
				return comp;
		}
		if (prerelease.length == 0)
			return o.prerelease.length == 0 ? 0 : 1;
		if (prerelease.length > 0) {
			for (int i = 0; i < prerelease.length && i < o.prerelease.length; i++) {
				if (prereleaseNumbers[i] > -1 && o.prereleaseNumbers[i] > -1)
					comp = Integer.compare(prereleaseNumbers[i], o.prereleaseNumbers[i]);
				else
					comp = prerelease[i].compareTo(o.prerelease[i]);
				if (comp != 0)
					return comp;
			}
			if (prerelease.length > o.prerelease.length)
				return -1;
			if (o.prerelease.length > prerelease.length)
				return 1;
		}
		return 0;
	}
	
	@Override
	public VersionNumber clone() {
		try {
			return (VersionNumber) super.clone();
		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e); //This shouldn't happen because we are Cloneable
		}
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof VersionNumber))
			return false;
		VersionNumber o = (VersionNumber) other;
		if (!hasSamePrereleaseVersion(o))
			return false;
		if (metadata.length != o.metadata.length)
			return false;
		for (int i = 0; i < metadata.length; i++)
			if (!metadata[i].equals(o.metadata[i]))
				return false;
		return true;
	}
	
	@Override
	public String toString() {
		if (representation == null) {
			cachePrereleaseNumbers();
			cacheSyntacticallyCorrectString();
		}
		return representation;
	}
	
	@Override
	public int hashCode() {
		if (hash == 0) {
			cachePrereleaseNumbers();
			hash = Arrays.deepHashCode(new Object[]{version, prerelease, metadata});
		}
		return hash;
	}
}
