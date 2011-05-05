package us.altio.sm;

import java.util.regex.Pattern;

public class GrammarRules {
	// ^\s*((?:\w+:)?)\s*(function|push|pop|dup|add|sub|div|mul|goto|ifeq|ifgr|call)(?:\s+(\d+|[\w\$]+)+)?$
	static final Pattern lineRule = Pattern.compile("^\\s*((?:\\w+:)?)\\s*(function|push|pop|dup|add|sub|div|mul|goto|ifeq|ifgr|call)(?:\\s+(\\d+|[\\w\\$]+)+)?\\s*$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
}
