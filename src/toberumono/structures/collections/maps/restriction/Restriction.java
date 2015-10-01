package toberumono.structures.collections.maps.restriction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Restriction<K> implements Predicate<K> {
	public static final int FOLLOWED_BY = 1;
	public static final int PRECEDED_BY = 2;
	private final String name;
	private final int direction;
	private final List<Pattern> inclusions, exclusions;
	
	private static final String quotedItem = "([^\"][^\\s]*|\"([^\"]|(?<!\\\\)\")\"))\\s*";
	private static final Pattern applicableKey = Pattern.compile(quotedItem);
	private static final Pattern icPattern = Pattern.compile("(+|-)?" + quotedItem);
	
	protected Restriction(String name, int direction, List<Pattern> inclusions, List<Pattern> exclusions) {
		this.name = name;
		this.direction = direction;
		this.inclusions = inclusions;
		this.exclusions = exclusions;
	}
	
	public abstract boolean doesApply(K key);
	
	public static <K> Restriction<K> getRestriction(String name, String restriction, int regexFlags, RestrictionFlag... flags) {
		List<RestrictionFlag> usedFlags = Arrays.asList(flags);
		List<String> keys = new ArrayList<>();
		Matcher key = Restriction.applicableKey.matcher(restriction);
		while (key.find() && !key.group(1).equals("->") && !key.group(1).equals("<-"))
			keys.add(key.group(2) == null ? key.group(1) : key.group(2));
		if (keys.size() < 1) {
			//TODO throw an exception here
		}
		
		int direction = key.group(1).equals("->") ? FOLLOWED_BY : PRECEDED_BY;
		
		key.usePattern(icPattern);
		List<Pattern> includes = new ArrayList<>(), excludes = new ArrayList<>();
		while (key.find()) {
			String pattern = key.group(3) == null ? key.group(2) : key.group(3);
			if (pattern.startsWith("\\"))
				pattern = pattern.substring(1);
			pattern.replaceAll("\\\\\"", "\"");
			if (key.group(1) != null && key.group(1).equals("-"))
				excludes.add(Pattern.compile(pattern, regexFlags));
			else
				includes.add(Pattern.compile(pattern, regexFlags));
		}
		if (includes.size() + excludes.size() < 1) {
			//TODO throw an exception here
		}
		return usedFlags.contains(RestrictionFlag.PATTERNED_KEYS) ? new PatternedKeys<>(name, direction, keys, includes, excludes) : new UnpatternedKeys<>(name, direction, keys, includes, excludes);
	}
}

class UnpatternedKeys<K> extends Restriction<K> {
	private final List<String> keys;
	
	protected UnpatternedKeys(String name, int direction, List<String> keys, List<Pattern> inclusions, List<Pattern> exclusions) {
		super(name, direction, inclusions, exclusions);
		this.keys = keys;
	}
	
	@Override
	public boolean doesApply(K key) {
		return keys.contains(key.toString());
	}
	
	@Override
	public boolean test(K t) {
		// TODO Auto-generated method stub
		return false;
	}
}

class PatternedKeys<K> extends Restriction<K> {
	protected final List<Pattern> keys;
	
	protected PatternedKeys(String name, int direction, List<String> keys, List<Pattern> inclusions, List<Pattern> exclusions) {
		super(name, direction, inclusions, exclusions);
		this.keys = new ArrayList<>();
		for (String key : keys)
			this.keys.add(Pattern.compile(key));
	}
	
	@Override
	public boolean doesApply(K key) {
		String str = key.toString();
		for (Pattern p : keys)
			if (p.matcher(str).find())
				return true;
		return false;
	}
	
	@Override
	public boolean test(K t) {
		// TODO Auto-generated method stub
		return false;
	}
}
