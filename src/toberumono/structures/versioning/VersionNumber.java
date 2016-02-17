package toberumono.structures.versioning;

import java.io.Serializable;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * A container for working with version numbers that comply with the format specified by
 * <a href="http://semver.org/">Semantic Versioning 2.0.0</a>.
 * 
 * @author Toberumono
 */
public class VersionNumber implements Comparable<VersionNumber>, Serializable {
	private static final Pattern versionSplitter = Pattern.compile(".", Pattern.LITERAL);
	private final long[] version;
	private final String[] prerelease, metadata;
	private final String strValue;
	private int hash;
	
	/**
	 * Constructs a new {@link VersionNumber} from a {@link String} using loose parsing.
	 * 
	 * @param version
	 *            the {@link String} on which the {@link VersionNumber} will be based
	 * @see #VersionNumber(String, boolean)
	 */
	public VersionNumber(String version) {
		this(version, true);
	}
	
	/**
	 * Constructs a new {@link VersionNumber} from a {@link String}.
	 * 
	 * @param version
	 *            the {@link String} on which the {@link VersionNumber} will be based
	 * @param looseParsing
	 *            if {@code true}, the parser will clean up the input {@link String} before parsing it
	 */
	public VersionNumber(String version, boolean looseParsing) {
		strValue = looseParsing ? looseParsingCleanup(version) : version;
		String[] sections = {"", "", ""};
		int prs = strValue.indexOf('-'), ms = strValue.indexOf('+');
		if (prs > -1) {
			sections[0] = strValue.substring(0, prs);
			if (ms > -1) {
				if (prs > ms)
					throw new InvalidVersionFormatException("Cannot have the prerelease section start after the metadata section.");
				sections[1] = strValue.substring(prs + 1, ms);
				sections[2] = strValue.substring(ms + 1);
				if (sections[2].length() == 0)
					throw new InvalidVersionFormatException("If a metadata section is specified (the version string contains a '+'), then its length must be greater than 0.");
			}
			else
				sections[1] = strValue.substring(prs + 1);
			if (sections[1].length() == 0)
				throw new InvalidVersionFormatException("If a prerelease section is specified (the version string contains a '-'), then its length must be greater than 0.");
		}
		else if (ms > -1) {
			sections[0] = strValue.substring(0, ms);
			sections[2] = strValue.substring(ms + 1);
			if (sections[2].length() == 0)
				throw new InvalidVersionFormatException("If a metadata section is specified (the version string contains a '+'), then its length must be greater than 0.");
		}
		else
			sections[0] = strValue;
		try {
			this.version = processSemanticVersion(sections[0]);
		}
		catch (NumberFormatException e) {
			throw new InvalidVersionFormatException("Found a non-numeric value in the first component of a version string.", e);
		}
		if (sections[1].length() > 0)
			prerelease = versionSplitter.split(sections[1]);
		else
			prerelease = new String[]{};
		if (sections[2].length() > 0)
			metadata = versionSplitter.split(sections[2]);
		else
			metadata = new String[]{};
	}
	
	private static String looseParsingCleanup(String version) {
		version = version.replaceAll(" ", "");
		while (version.length() > 0 && (version.charAt(0) < 48 || version.charAt(0) > 57))
			version = version.substring(1);
		if (version.length() == 0)
			throw new InvalidVersionFormatException("The first section of the version string must be of the form, major.minor.patch, where all three values are non-negative integers.");
		return version;
	}
	
	private static long[] processSemanticVersion(String input) {
		long[] version = {0l, 0l, 0l};
		String[] vn = versionSplitter.split(input);
		if (vn.length != 3)
			throw new InvalidVersionFormatException("The first section of the version string must be of the form, major.minor.patch, where all three values are non-negative integers.");
		for (int i = 0; i < 3; i++)
			version[i] = Long.parseLong(vn[i]);
		return version;
	}
	
	@Override
	public int compareTo(VersionNumber o) {
		int comp = 0;
		for (int i = 0; i < version.length; i++) {
			comp = Long.compare(version[i], o.version[i]);
			if (comp != 0)
				return comp;
		}
		if (prerelease.length == 0 && o.prerelease.length > 0) {
			if (o.prerelease.length == 0)
				return 0;
			else
				return 1;
		}
		else if (prerelease.length > 0 && o.prerelease.length == 0)
			return -1;
		for (int i = 0; i < prerelease.length && i < o.prerelease.length; i++) {
			try {
				long p = Long.parseLong(prerelease[i]), op = Long.parseLong(o.prerelease[i]);
				if (p < op)
					return -1;
				else if (p > op)
					return 1;
			}
			catch (NumberFormatException e) {
				int cmp = prerelease[i].compareTo(o.prerelease[i]);
				if (cmp != 0)
					return cmp;
			}
		}
		if (prerelease.length < o.prerelease.length)
			return 1;
		else
			return -1;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof VersionNumber))
			return false;
		VersionNumber o = (VersionNumber) other;
		if (version.length != o.version.length)
			return false;
		for (int i = 0; i < version.length; i++)
			if (version[i] != o.version[i])
				return false;
		if (prerelease.length != o.prerelease.length)
			return false;
		for (int i = 0; i < prerelease.length; i++)
			if (!prerelease[i].equals(o.prerelease[i]))
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
		return strValue;
	}
	
	@Override
	public int hashCode() {
		if (hash == 0) {
			hash = 17;
			hash = hash * 31 + Arrays.hashCode(version);
			hash = hash * 31 + Arrays.hashCode(prerelease);
			hash = hash * 31 + Arrays.hashCode(metadata);
		}
		return hash;
	}
}
